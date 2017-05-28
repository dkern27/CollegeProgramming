//
//  Thread.cpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/18/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#include "Thread.hpp"
#include <climits>

int Thread::overhead = 0;

Thread::Thread()
{
    arrivalTime = INT_MAX;
    numBursts = 0;
    processId = 0;
}

Thread::Thread(int id, int arrivalTime, int numBursts, int processId)
{
    this->id = id;
    this->arrivalTime=arrivalTime;
    this->numBursts = numBursts;
    this->processId = processId;
}

Burst* Thread::currentBurst()
{
    return bursts[burstIndex];
}

Burst* Thread::nextBurst()
{
    burstIndex++;
    return currentBurst();
}

int Thread::getId()
{
    return id;
}

int Thread::getArrivalTime()
{
    return arrivalTime;
}

int Thread::getBurstIndex()
{
    return burstIndex;
}

int Thread::getCpuTime()
{
    int time = 0;
    for (Burst* b : bursts)
        time += b->getCpu();
    return time;
}

int Thread::getIoTime()
{
    int time = 0;
    for (Burst* b : bursts)
        time += b->getIo();
    return time;
}

int Thread::getStartTime()
{
    return startTime;
}

int Thread::getCompleteTime()
{
    return completeTime;
}

int Thread::getProcessId()
{
    return processId;
}

void Thread::setStartTime(int t)
{
    startTime = t;
}

void Thread::setCompleteTime(int t)
{
    completeTime = t;
}
