//
//  Scheduler.cpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/23/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#include "Scheduler.hpp"
#include <iostream>
#include <stdio.h>
#include <iomanip>

using namespace std;

void Scheduler::runScheduler()
{
    events.push_back(new Event(EventType::DI, 0, events[0]->getThread(), events[0]->getProcess()));
    int index = 0;
    while(index < events.size())
    {
        Event* currentEvent = events[index];
        processEvent(currentEvent);
        index++;
        if(index == events.size() && anyProcessesLeft())
        {
            invokeDispatcherEvent(currentEvent);
        }
    }
    
    info.totalTime = events.back()->getTime();
    output();
}

void Scheduler::updateThreadState(Thread* thread, EventType e, int time)
{
    switch (e)
    {
        case EventType::CPU:
            if(thread->currentBurst()->getIo() == 0)
                thread->state = State::EXIT;
            else
                thread->state = State::BLOCKED;
            break;
            
        case EventType::TC:
            thread->state = State::EXIT;
            break;
            
        case EventType::TP:
        case EventType::TA:
        case EventType::IO:
            thread->state = State::READY;
            thread->timeSinceReady = time;
            break;
            
        case EventType::TDC:
        case EventType::PDC:
        case EventType::DI:
            thread->state = State::RUNNING;
            if(thread->getStartTime() == -1 && e != EventType::DI)
                thread->setStartTime(time);
            break;
    }
    if (allThreadsBlocked())
        timeBlocked = time;
    else if (timeBlocked != 0)
    {
        info.idleTime += time - timeBlocked;
        timeBlocked = 0;
    }
}

//Handles standard and perThread output
void Scheduler::output()
{
    if(verbose)
    {
        doVerboseOutput();
    }
    if(perThread)
        perThreadOutput();
    doStandardOutput();
}

//Handles the satndard output
void Scheduler::doStandardOutput()
{
    cout << setprecision(2) << fixed;
    vector<Process*> system, interactive, normal, batch;
    makeProcessGroups(system, interactive, normal, batch);
    outputPriorityStats("SYSTEM", system);
    outputPriorityStats("INTERACTIVE", interactive);
    outputPriorityStats("NORMAL", normal);
    outputPriorityStats("BATCH", batch);
    processOutputInfo();
}

void Scheduler::doVerboseOutput()
{
    for( int i = 0; i < events.size(); i++)
    {
        if( events[i]->getEventType() == EventType::CPU && events[i+1]->getEventType() == EventType::TC)
            continue;
        events[i]->verboseOutput();
    }
}

//Separates processes by priority
void Scheduler::makeProcessGroups(std::vector<Process*> &system, std::vector<Process*> &interactive, std::vector<Process*> &normal, std::vector<Process*> &batch)
{
    for (Process* p : processes)
    {
        switch(p->getPriority())
        {
            case Priority::SYSTEM:
                system.push_back(p);
                break;
            case Priority::INTERACTIVE:
                interactive.push_back(p);
                break;
            case Priority::NORMAL:
                normal.push_back(p);
                break;
            case Priority::BATCH:
                batch.push_back(p);
                break;
        }
    }
}

//perThread output
void Scheduler::outputPriorityStats(string priority, vector<Process*> pProcesses)
{
    cout << priority << " THREADS:" << endl;
    int threadCount = 0;
    for (Process* p : pProcesses)
    {
        threadCount += p->threads.size();
    }
    cout << "Total count: " << threadCount << endl;
    cout << "Avg Response Time: " << getResponseTime(pProcesses) << endl;
    cout << "Avg Turnaround time: " << getTurnaroundTime(pProcesses) << endl;
    cout << endl;

}

float Scheduler::getResponseTime(vector<Process*> pProcesses)
{
    if(pProcesses.size() == 0)
        return 0;
    float rTime = 0;
    int numThreads = 0;
    for (Process* p : pProcesses)
    {
        for (Thread *t : p->threads)
        {
            rTime += (t->getStartTime() - t->getArrivalTime());
        }
        numThreads += p->getNumThreads();
    }
    return float(rTime)/float(numThreads);
}

float Scheduler::getTurnaroundTime(vector<Process*> pProcesses)
{
    if(pProcesses.size() == 0)
        return 0;
    float taTime = 0;
    int numThreads = 0;
    for (Process* p : pProcesses)
    {
        for (Thread* t : p->threads)
        {
            taTime += t->getCompleteTime() - t->getArrivalTime();
        }
        numThreads += p->getNumThreads();
    }
    return float(taTime)/float(numThreads);
}

void Scheduler::processOutputInfo()
{
    cout << "Total elapsed time: " << info.totalTime << endl;
    cout << "Total service time: " << info.cpuTime << endl;
    cout << "Total I/O time: " << info.ioTime << endl;
    cout << "Total dispatch time: " << info.dispatchTime << endl;
    cout << "Total idle time: " << info.idleTime << endl;
    
    cout << "\nCPU Utilization: " << (1-float(info.idleTime)/float(info.totalTime))*100 << "%" << endl;
    cout << "CPU Efficiency: " << float(info.cpuTime)/float(info.totalTime)*100 << "%" << endl;
}

void Scheduler::perThreadOutput()
{
    for (Process* p : processes)
    {
        cout << "Process " << p->getId() << " [" << p->getPriorityName() << "]:" << endl;
        for (Thread* t : p->threads)
        {
            cout << "\tThread " << t->getId() << ": ";
            cout << "ARR: " << t->getArrivalTime()
             << setw(10) << "CPU: " << setw(4) << t->getCpuTime()
             << setw(10) << "I/O: " << setw(4) << t->getIoTime()
             << setw(10) << "TRT: " << setw(4) << t->getCompleteTime() - t->getArrivalTime()
             << setw(10) << "END: " << setw(4) << t->getCompleteTime() << endl;;
        }
        cout << endl;
    }
}

bool Scheduler::anyProcessesLeft()
{
    for (Process* p : processes)
    {
        if (!p->isProcessDone())
            return true;
    }
    return false;
}

bool Scheduler::allThreadsBlocked()
{
    for (Process* p : processes)
    {
        for (Thread* t : p->threads)
        {
            if ( t-> state == State::READY || t->state == State::RUNNING)
                return false;
        }
    }
    return true;
}

void Scheduler::invokeDispatcherEvent(Event* e)
{
    Process *p = new Process();
    Thread* t = selectThread(p);
    if( t->getArrivalTime() != INT_MAX)
    {
        events.push_back(new Event(EventType::DI, e->getTime(), t, p));
        if( p->getId() == e->getProcess()->getId())
        {
            isDiffProcess = false;
        }
        else
        {
            isDiffProcess = true;
        }
    }
}
