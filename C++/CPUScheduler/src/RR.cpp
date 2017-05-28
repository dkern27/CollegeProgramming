//
//  RR.cpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/29/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#include "RR.hpp"
#include <climits>
#include <algorithm>

using namespace std;

RR::RR(vector<Process*> processes, vector<Event*> events, bool verbose, bool perThread)
{
    this->processes = processes;
    this->events = events;
    this->verbose = verbose;
    this->perThread = perThread;
}

void RR::processEvent(Event* e)
{
    updateThreadState(e->getThread(), e->getEventType(), e->getTime());
    
    switch (e->getEventType())
    {
        case EventType::TA:
            //Do nothing
            break;
            
        case EventType::TDC:
        case EventType::PDC:
            if(e->getThread()->timesPreempted == 0)
            {
                e->getThread()->nextBurst();
                info.cpuTime += e->getThread()->currentBurst()->getCpu();
                e->getThread()->remainingCPUTime = e->getThread()->currentBurst()->getCpu();
            }
            if(e->getThread()->remainingCPUTime > TIME_SLICE)
            {
                e->getThread()->remainingCPUTime -= TIME_SLICE;
                events.push_back(new Event(EventType::TP, e->getTime()+TIME_SLICE, e->getThread(), e->getProcess()));
            }
            else
            {
                events.push_back(new Event(EventType::CPU, e->getTime() + e->getThread()->remainingCPUTime, e->getThread(), e->getProcess()));
                e->getThread()->remainingCPUTime = INT_MIN;
            }
            break;
            
        case EventType::CPU:
            if(e->getThread()->currentBurst()->getIo() > 0)
            {
                info.ioTime += e->getThread()->currentBurst()->getIo();
                events.push_back(new Event(EventType::IO, e->getTime() + e->getThread()->currentBurst()->getIo(), e->getThread(), e->getProcess()));
                invokeDispatcherEvent(e);
            }
            else
            {
                events.push_back(new Event(EventType::TC, e->getTime(), e->getThread(), e->getProcess()));
            }
            break;
            
        case EventType::IO:
            e->getThread()->timesPreempted = 0;
            break;
            
        case EventType::TC:
            e->getThread()->setCompleteTime(e->getTime());
            if(!anyProcessesLeft()) //Exit if no processes/threads remain
            {
                return;
            }
            else //Process/Thread remaining
            {
                invokeDispatcherEvent(e);
            }
            break;
            
        case EventType::TP:
        {
            e->getThread()->timesPreempted++;
            invokeDispatcherEvent(e);
            break;
        }
            
        case EventType::DI:
            if(isDiffProcess) //Next Process
            {
                info.dispatchTime += Process::overhead;
                events.push_back(new Event(EventType::PDC, e->getTime() + Process::overhead, e->getThread(), e->getProcess()));
            }
            else
            {
                info.dispatchTime += Thread::overhead;
                events.push_back(new Event(EventType::TDC, e->getTime() + Thread::overhead, e->getThread(), e->getProcess()));
            }
            break;
    }
    sort(events.begin(), events.end(), Event::compArrivalTime);
}

Thread* RR::selectThread(Process*& process)
{
    Thread* nextThread = new Thread();
    for (Process* p : processes)
    {
        for (Thread* t : p->threads)
        {
            if (t->state == State::READY && t->timeSinceReady < nextThread->timeSinceReady)
            {
                nextThread = t;
                process = p;
            }
        }
    }
    return nextThread;
}
