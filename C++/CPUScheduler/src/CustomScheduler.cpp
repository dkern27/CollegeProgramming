//
//  CustomScheduler.cpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/30/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#include "CustomScheduler.hpp"
#include <climits>
#include <algorithm>

using namespace std;

CustomScheduler::CustomScheduler(vector<Process*> processes, vector<Event*> events, bool verbose, bool perThread)
{
    this->processes = processes;
    this->events = events;
    this->verbose = verbose;
    this->perThread = perThread;
    feedbackQueues.push_back(vector<Thread*>());
}

void CustomScheduler::processEvent(Event* e)
{
    updateThreadState(e->getThread(), e->getEventType(), e->getTime());
    updateWaitTimes(e->getTime());
    
    switch (e->getEventType())
    {
        case EventType::TA:
            addToPriorityQueue(e->getThread(), e->getProcess()->getPriority());
            break;
            
        case EventType::TDC:
        case EventType::PDC:
            if(e->getThread()->remainingCPUTime == INT_MIN)
            {
                e->getThread()->nextBurst();
                info.cpuTime += e->getThread()->currentBurst()->getCpu();
                e->getThread()->remainingCPUTime = e->getThread()->currentBurst()->getCpu();
            }
            if(e->getThread()->remainingCPUTime > getTimeSlice(e->getProcess()->getPriority()))
            {
                e->getThread()->remainingCPUTime -= getTimeSlice(e->getProcess()->getPriority());
                events.push_back(new Event(EventType::TP, e->getTime()+getTimeSlice(e->getProcess()->getPriority()), e->getThread(), e->getProcess()));
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
            addThreadToQueue(e->getThread());
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
            e->getThread()->timeSinceReady = e->getTime();
            addThreadToQueue(e->getThread());
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

int CustomScheduler::getTimeSlice(Priority priority)
{
    switch (priority)
    {
        case Priority::SYSTEM:
            return SYS_TIME_SLICE;
            break;
        case Priority::INTERACTIVE:
            return INT_TIME_SLICE;
            break;
        case Priority::NORMAL:
            return NOR_TIME_SLICE;
            break;
        case Priority::BATCH:
            return BAT_TIME_SLICE;
            break;
    }
}

Thread* CustomScheduler::selectThread(Process*& process)
{
    Thread* nextThread = new Thread();
    //Prioirty used for first time a thread runs
    if (sysThreads.size() > 0 || intThreads.size() > 0 || norThreads.size() > 0 || batThreads.size() > 0)
    {
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
    
    //feedback queue and HRRN used to choose thread
    for (int i=0; i < feedbackQueues.size(); i++)
    {
        if (feedbackQueues[i].size() > 0)
        {
            int highestHRRN = 0;
            int index = 0;
            for (int j=0; j<feedbackQueues[i].size(); j++)
            {
                float hrrn = calculateHRRN(feedbackQueues[i][j]);
                if(hrrn == highestHRRN)
                {
                    if(processes[feedbackQueues[i][j]->getProcessId()]->getPriority() < processes[nextThread->getProcessId()]->getPriority())
                    {
                        highestHRRN = hrrn;
                        nextThread = feedbackQueues[i][j];
                        process = processes[nextThread->getProcessId()];
                        index = j;
                        continue;
                    }
                }
                if(hrrn > highestHRRN)
                {
                    highestHRRN = hrrn;
                    nextThread = feedbackQueues[i][j];
                    process = processes[nextThread->getProcessId()];
                    index = j;
                }
            }
            feedbackQueues[i].erase(feedbackQueues[i].begin() + index);
            return nextThread;
        }
    }
    return nextThread;
}

void CustomScheduler::addThreadToQueue(Thread* thread)
{
    while(feedbackQueues.size() < thread->timesPreempted + 1)
        feedbackQueues.push_back(vector<Thread*>());
    feedbackQueues[thread->timesPreempted].push_back(thread);
}

float CustomScheduler::calculateHRRN(Thread* thread)
{
    int timeSlice = getTimeSlice(processes[thread->getProcessId()]->getPriority());
    int serviceTime = thread->remainingCPUTime;
    if(thread->remainingCPUTime >= timeSlice)
        serviceTime = timeSlice;
    float hrrn = float((thread->waitTime + serviceTime))/float(serviceTime);
    return hrrn;
}

void CustomScheduler::updateWaitTimes(int time)
{
    for (int i=0; i < feedbackQueues.size(); i++)
    {
        for (int j=0; j<feedbackQueues[i].size(); j++)
        {
            feedbackQueues[i][j]->waitTime += time-feedbackQueues[i][j]->timeSinceReady;
        }
        
    }

}

void CustomScheduler::addToPriorityQueue(Thread* thread, Priority priority)
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
