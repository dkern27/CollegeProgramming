//
//  Scheduler.hpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/22/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#ifndef Scheduler_hpp
#define Scheduler_hpp

#include "Event.hpp"
#include "Process.hpp"
#include <vector>
#include <stdio.h>

struct OutputInfo
{
    int totalTime = 0;
    int cpuTime = 0;
    int ioTime = 0;
    int dispatchTime = 0;
    int idleTime = 0;
};

class Scheduler
{
public:
    void runScheduler();
    void output();
    void updateThreadState(Thread*, EventType, int);
    bool anyProcessesLeft();
    void invokeDispatcherEvent(Event*);
    
private:
    virtual Thread* selectThread(Process*&) = 0;
    virtual void processEvent(Event*) = 0;
    
    void doStandardOutput();
    void doVerboseOutput();
    void makeProcessGroups(std::vector<Process*>&, std::vector<Process*>&, std::vector<Process*>&, std::vector<Process*>&);
    void outputPriorityStats(std::string, std::vector<Process*>);
    void perThreadOutput();
    float getResponseTime(std::vector<Process*>);
    float getTurnaroundTime(std::vector<Process*>);
    void processOutputInfo();
    bool allThreadsBlocked();
    
    
    int timeBlocked = 0;

protected:
    std::vector<Event*> events;
    std::vector<Process*> processes;
    OutputInfo info;
    bool verbose = false;
    bool perThread = false;
    bool isDiffProcess = true;
};

#endif /* Scheduler_hpp */
