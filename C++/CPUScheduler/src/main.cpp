//
//  main.cpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/18/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#include "FCFS.hpp"
#include "RR.hpp"
#include "PriorityScheduler.hpp"
#include "CustomScheduler.hpp"
#include "Process.hpp"
#include "Thread.hpp"
#include "Event.hpp"
#include <cstdlib>
#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <algorithm>
#include <cstring>
#include <getopt.h>

using namespace std;

void Usage();
vector<Process*> readFile(const char*);
vector<Event*> initializeQueue(vector<Process*>);

void processEvent();

int main(int argc, char **argv)
{
    int c = 0;
    int option_index;
    bool verbose = false, perThread = false;
    bool useFCFS = true, useRR = false, usePriority = false, useCustom = false;
    
    const struct option long_options[] = {
        {"per_thread", no_argument, 0, 't'},
        {"verbose", no_argument, 0, 'v'},
        {"algorithm", required_argument, 0, 'a'},
        {"help", no_argument, 0, 'h'},
        {0,0,0,0}
    };
    
    //Parse options
    while ((c = getopt_long(argc, argv, "tva:h", long_options, &option_index)) != -1)
    {
        switch (c)
        {
            case 't':
                perThread = true;
                break;
            case 'v':
                verbose = true;
                break;
            case 'a':
                if ( strcmp(optarg, "RR") == 0)
                    useRR = true;
                else if(strcmp( optarg, "PRIORITY") == 0)
                    usePriority = true;
                else if (strcmp( optarg, "CUSTOM") == 0)
                    useCustom = true;
                else if (strcmp(optarg, "FCFS") == 0)
                    useFCFS = true;
                else
                {
                    cout << "Invalid algorithm specified" << endl;
                    return EXIT_FAILURE;
                }
                if (useRR || usePriority || useCustom)
                    useFCFS = false;
                break;
            case 'h':
                Usage();
                return EXIT_SUCCESS;
                break;
            default: //Invalid option
                return EXIT_FAILURE;
        }
    }
    //Get filename
    const char* fileName;
    if (optind < argc)
        fileName = argv[optind++];
    else
    {
        cout << "No file specified" << endl;
        return EXIT_FAILURE;
    }

    vector<Process*> processes = readFile(fileName);
    vector<Event*> events = initializeQueue(processes);
    
    Scheduler *scheduler = NULL;
    if(useFCFS)
    {
        scheduler = new FCFS(processes, events, verbose, perThread);
    }
    else if(useRR)
    {
        scheduler = new RR(processes, events, verbose, perThread);
    }
    else if (usePriority)
    {
        scheduler = new PriorityScheduler(processes, events, verbose, perThread);
    }
    else if (useCustom)
    {
        scheduler = new CustomScheduler(processes, events, verbose, perThread);
    }
    scheduler->runScheduler();
    return 0;
}

void Usage()
{
    cout << "To run the program, use './simulator [flags] simulation_file.txt'" << endl;
    cout << "Optional Flags:" << endl;
    cout << "\t-t, --per_thread" << endl;
    cout << "\t\t Output additional per-thread statistics for arrival time, service time, etc." << endl;
    cout << "\t-v, --verbose" << endl;
    cout << "\t\t Output information about every state-changing event and scheduling decision" << endl;
    cout << "\t-a, --algorithm" << endl;
    cout << "\t\t Scheduling algorithm to use. Valid arguments: FCFS, RR, PRIORTY, CUSTOM" << endl;
    cout << "\t-h, --help" << endl;
    cout << "\t\t Display help message" << endl;
}

vector<Process*> readFile(const char* fileName)
{
    vector<Process*> processes;
    
    ifstream file(fileName, ifstream::in);
    if (!file.is_open())
    {
        cout << "Error opening file '" << fileName << "'" << endl;
        exit(0);
    }
    int numProcesses;
    //Get file header info
    file >> numProcesses >> Thread::overhead >> Process::overhead;
    //Read for each process
    for (int i=0; i < numProcesses; i++)
    {
        int id = 0, priority = 0, numThreads = 0;
        file >> id >> priority >> numThreads;
        Process* p = new Process(id, priority, numThreads);
        //Read for each thread
        for(int j=0; j<numThreads; j++)
        {
            int arrivalTime, numBursts;
            file >> arrivalTime >> numBursts;
            Thread *t = new Thread(j, arrivalTime, numBursts, id);
            string line;
            file.ignore(256, '\n');
            //Get all lines for a thread
            for(int k=0; k < numBursts; k++)
            {
                getline(file, line);
                stringstream stream(line);
                int cpu=0, io=0;
                if(line.length() >= 3)
                    stream >> cpu >> io;
                else //last thread burst
                {
                    stream >> cpu;
                }
                t->bursts.push_back(new Burst(cpu, io));
            }
            p->threads.push_back(t);
        }
        processes.push_back(p);
    }
    file.close();
    return processes;
}

vector<Event*> initializeQueue(vector<Process*> processes)
{
    vector<Event*> events;
    for(Process* process : processes)
    {
        for(Thread* t : process->threads)
        {
            events.push_back(new Event(EventType::TA, t->getArrivalTime(), t, process));
        }
    }
    sort(events.begin(), events.end(), Event::compArrivalTime);
    return events;
}
