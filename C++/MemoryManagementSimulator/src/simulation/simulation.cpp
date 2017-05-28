/**
 * This file contains implementations for the methods defined in the Simulation
 * class.
 *
 * You'll probably spend a lot of your time here.
 */

#include "simulation/simulation.h"
#include "process/process.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include <iomanip>

using namespace std;


void Simulation::run(FlagOptions flags) {
    Process::MAX_FRAMES = flags.max_frames;
    strategy = flags.strategy;
    verbose = flags.verbose;
    readFile(flags.filename);
    for(VirtualAddress va : virtualAddresses)
    {
        perform_memory_access(va);
        this->time++;
    }
    standardOutput();
}


void Simulation::perform_memory_access(const VirtualAddress& va) {
    Process* p = processes.at(va.process_id);
    p->memory_accesses++;
    int index = -1;
    bool inMemory = true;
    if((index = alreadyLoaded(va)) == -1)
    {
        inMemory = false;
        index = handle_page_fault(p, va.page);
    }
    PhysicalAddress pa(index, va.offset);
    p->page_table.rows[va.page].last_accessed_at = time;
    if(verbose)
        verboseOutput(va, inMemory, pa, p->get_rss());
}


int Simulation::handle_page_fault(Process* process, size_t page) {
    if(process->is_valid_page(page))
    {
        process->page_faults++;
        int index = frames.size();
        bool full = false;
        if (process->get_rss() >= Process::MAX_FRAMES)
        {
            full = true;
            int i = 0;
            if(strategy == ReplacementStrategy::FIFO)
                i = process->page_table.get_oldest_page();
            else if (strategy == ReplacementStrategy::LRU)
                i = process->page_table.get_least_recently_used_page();
            if(i < 0) {
                cerr << "Segmentation Fault. Exiting..." << endl;
                exit(1);
            }
            
            index = process->page_table.rows[i].frame;
            process->page_table.rows[i].present = false;
        }

        Frame f;
        f.set_page(process, page);
        if(index >= frames.size())
        {
            frames.push_back(f);
        }
        else
            frames[index] = f;
        process->page_table.rows[page].present = true;
        process->page_table.rows[page].frame = index;
        process->page_table.rows[page].loaded_at = time;
        return index;
    }
    else
    {
        cerr << "Invalid page. Exiting..." << endl;
        exit(1);
    }
}


void Simulation::readFile(string fileName)
{
    ifstream file(fileName, ifstream::in);
    int numProcesses;
    file >> numProcesses;
    for (int i = 0; i < numProcesses; i++)
    {
        int pid;
        string processImageFile;
        file >> pid >> processImageFile;
        ifstream stream(processImageFile, ifstream::in);
        Process* p = Process::read_from_input(stream);
        processes.emplace(pid, p);
    }
    int pid;
    while (file >> pid)
    {
        string virtualAddress;
        file >> virtualAddress;
        VirtualAddress va = VirtualAddress::from_string(pid, virtualAddress);
        virtualAddresses.push_back(va);
    }
}

void Simulation::standardOutput()
{
    int memAcc = 0;
    int pageFaults = 0;
    int freeFrames = NUM_FRAMES;
    cout << endl;
    for (map<int, Process*>::iterator it = processes.begin(); it != processes.end(); it++)
    {
        string output = "Process " + to_string(it->first);
        output += " ACCESSES: " + to_string(it->second->memory_accesses);
        output += " FAULTS: " + to_string(it->second->page_faults);
        stringstream stream;
        stream << fixed << setprecision(2) << it->second->get_fault_percent();
        output += " FAULT RATE: " + stream.str();
        output += " RSS: " + to_string(it->second->get_rss());
        cout << output << endl;
        memAcc += it->second->memory_accesses;
        pageFaults += it->second->page_faults;
        freeFrames -= it->second->get_rss();
    }
    cout << endl;

    cout << "Total memory accesses: " << to_string(memAcc) << endl;
    cout << "Total page faults: " << to_string(pageFaults) << endl;
    cout << "Free frames remaining: " << freeFrames << endl;
}

void Simulation::verboseOutput(VirtualAddress va, bool inMemory, PhysicalAddress pa, int rss)
{
    cout << va << endl;
    if(inMemory)
        cout << "\tIN MEMORY" << endl;
    else
        cout << "\tPAGE FAULT" << endl;
    cout << "\t" << pa << endl;
    cout << "\tRSS: " << rss << endl;
}

int Simulation::alreadyLoaded(VirtualAddress va)
{
    Process* p = processes.at(va.process_id);
    int i = 0;
    for (Frame f : frames)
    {
        if(f.process == p && f.page_number == va.page)
            return i;
        i++;
    }
    return -1;
}
