//
//  Event.hpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/18/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#ifndef Event_hpp
#define Event_hpp

#include "Process.hpp"
#include "Thread.hpp"
#include <stdio.h>
#include <string>

enum EventType
{
    TA,
    TDC,
    PDC,
    CPU,
    IO,
    TC,
    TP,
    DI
};

class Event
{
public:
    //Constructor
    Event(EventType, int, Thread*, Process*);
    
    void verboseOutput();
    
    //For sorting event vector
    static bool compArrivalTime(Event*, Event*);
    
    //Getters/Setters
    EventType getEventType();
    int getTime();
    Thread* getThread();
    Process* getProcess();
    
private:
    //Helper functions
    std::string getEventTypeString();
    std::string getEventDescription();
    
    EventType eventType;
    int time;
    Thread* thread;
    Process* process;
};
#endif /* Event_hpp */
