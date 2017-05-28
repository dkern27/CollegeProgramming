//
//  Process.cpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/18/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#include "Process.hpp"
#include <cstdlib>
#include <climits>
#include <string>

using namespace std;

int Process::overhead = 0;

Process::Process()
{
    numThreads = 0;
    id = INT_MAX;
    priority = Priority::BATCH;
}

Process::Process(int id, int priority, int numThreads)
{
    this->id = id;
    this->numThreads=numThreads;
    setPriority(priority);
}

bool Process::isProcessDone()
{
    for(Thread* t : threads)
    {
        if(t->state != State::EXIT)
            return false;
    }
    return true;
}

Priority Process::getPriority()
{
    return this->priority;
}

string Process::getPriorityName()
{
    string priorityName;
    switch (priority)
    {
        case Priority::SYSTEM:
            priorityName = "SYSTEM";
            break;
        case Priority::INTERACTIVE:
            priorityName = "INTERACTIVE";
            break;
        case Priority::NORMAL:
            priorityName = "NORMAL";
            break;
        case Priority::BATCH:
            priorityName = "BATCH";
            break;
    }
    return priorityName;
}

int Process::getId()
{
    return id;
}

int Process::getNumThreads()
{
    return numThreads;
}

int Process::getArrivalTime()
{
    int minTime = INT_MAX;
    for (Thread* t : threads)
    {
        if(t->getArrivalTime() < minTime)
            minTime = t->getArrivalTime();
    }
    return minTime;
}

void Process::setId(int id)
{
    this->id = id;
}

void Process::setPriority(int p)
{
    switch(p)
    {
        case 0:
            priority = Priority::SYSTEM;
            break;
        case 1:
            priority = Priority::INTERACTIVE;
            break;
        case 2:
            priority = Priority::NORMAL;
            break;
        case 3:
            priority = Priority::BATCH;
            break;
        default:
            break;
    }
}
