//
//  Event.cpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/18/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#include "Event.hpp"
#include <string>
#include <iostream>

using namespace std;

Event::Event(EventType type, int time, Thread* thread, Process* process)
{
    eventType = type;
    this->time = time;
    this->thread = thread;
    this->process = process;
}

void Event::verboseOutput()
{
    string output = "At time " + to_string(time) + ":\n";
    output += "\t" + getEventTypeString() +"\n";
    output += "\tThread " + to_string(thread->getId()) + " in process " + to_string(process->getId()) + " [" + process->getPriorityName() + "]\n";
    output += getEventDescription();
    cout << output << endl;
    
}

bool Event::compArrivalTime(Event* e1, Event* e2)
{
    if( e1->time == e2->time)
    {
        if(e1->eventType == e2->eventType)
            return e1->getThread()->getId() < e2->getThread()->getId();
        if (e1->getEventType() == EventType::IO)
            return false;
        if(e2->getEventType() == EventType::IO)
            return true;
        if (e1->getEventType() == EventType::CPU)
            return true;
        if(e2->getEventType() == EventType::CPU)
            return false;
        if (e1->getEventType() == EventType::DI && e2->getEventType() == EventType::CPU)
            return false;
        if(e1->getEventType() == EventType::DI && e2->getEventType() == EventType::IO)
            return true;
        if (e2->getEventType() == EventType::DI && e1->getEventType() == EventType::CPU)
            return true;
        if(e2->getEventType() == EventType::DI && e1->getEventType() == EventType::IO)
            return false;
    }
    
            
    return e1->time < e2->time;
}

EventType Event::getEventType()
{
    return eventType;
}

int Event::getTime()
{
    return time;
}

Thread* Event::getThread()
{
    return thread;
}

Process* Event::getProcess()
{
    return process;
}

string Event::getEventTypeString()
{
    string eventString;
    switch (eventType)
    {
        case EventType::TA:
            eventString = "THREAD_ARRIVED";
            break;
        case EventType::TDC:
            eventString = "THREAD_DISPATCH_COMPLETED";
            break;
        case EventType::PDC:
            eventString = "PROCESS_DISPATCH_COMPLETED";
            break;
        case EventType::CPU:
            eventString = "CPU_BURST_COMPLETED";
            break;
        case EventType::IO:
            eventString = "IO_BURST_COMPLETED";
            break;
        case EventType::TC:
            eventString = "THREAD_COMPLETED";
            break;
        case EventType::TP:
            eventString = "THREAD_PREEMPTED";
            break;
        case EventType::DI:
            eventString = "DISPATCHER_INVOKED";
            break;
    }
    return eventString;
}

string Event::getEventDescription()
{
    string eventString = "";
    switch (eventType)
    {
        case EventType::TA:
            eventString = "\tTransitioned from NEW to READY\n";
            break;
        case EventType::TDC:
        case EventType::PDC:
            eventString = "\tTransitioned from READY to RUNNING\n";
            break;
        case EventType::CPU:
            eventString = "\tTransitioned from RUNNING to BLOCKED\n";
            break;
        case EventType::IO:
            eventString = "\tTransitioned from BLOCKED to READY\n";
            break;
        case EventType::TC:
            eventString = "\tTransitioned from RUNNING to EXIT\n";
            break;
        case EventType::TP:
            eventString = "\tTransition from RUNNING to READY\n";
            break;
        case EventType::DI:
            break;
    }

    return eventString;
}

