//
//  PriorityScheduler.cpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/29/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#include "PriorityScheduler.hpp"
#include <climits>
#include <algorithm>

using namespace std;

PriorityScheduler::PriorityScheduler(vector<Process*> processes, vector<Event*> events, bool verbose, bool perThread)
{
    this->processes = processes;
    this->events = events;
    this->verbose = verbose;
    this->perThread = perThread;
}

void PriorityScheduler::processEvent(Event* e)
{
    updateThreadState(e->getThread(), e->getEventType(), e->getTime());
    
    switch (e->getEventType())
    {
        case EventType::TA:
            addThreadToQueue(e->getThread(), e->getProcess()->getPriority());
            break;
            
        case EventType::TDC:
        case EventType::PDC:
            e->getThread()->nextBurst();
            info.cpuTime += e->getThread()->currentBurst()->getCpu();
            events.push_back(new Event(EventType::CPU, e->getTime() + e->getThread()->currentBurst()->getCpu(), e->getThread(), e->getProcess()));
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
            addThreadToQueue(e->getThread(), e->getProcess()->getPriority());
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
            //Not used in PRIORITY
            break;
            
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

Thread* PriorityScheduler::selectThread(Process* &process)
{
    Thread* nextThread = new Thread();
    if(sysThreads.size() > 0)
    {
        nextThread = sysThreads.front();
        sysThreads.pop();
    }
    else if(intThreads.size() > 0)
    {
        nextThread = intThreads.front();
        intThreads.pop();
    }
    else if(norThreads.size() > 0)
    {
        nextThread = norThreads.front();
        norThreads.pop();
    }
    else if(batThreads.size() > 0)
    {
        nextThread = batThreads.front();
        batThreads.pop();
    }
    process = processes[nextThread->getProcessId()];
    return nextThread;
}

void PriorityScheduler::addThreadToQueue(Thread* thread, Priority priority)
{
    if(isFirstThread)
    {
        isFirstThread = false;;
        return;
    }
    switch(priority)
    {
        case Priority::SYSTEM:
            sysThreads.push(thread);
            break;
        case Priority::INTERACTIVE:
            intThreads.push(thread);
            break;
        case Priority::NORMAL:
            norThreads.push(thread);
            break;
        case Priority::BATCH:
            batThreads.push(thread);
            break;
    }
}
