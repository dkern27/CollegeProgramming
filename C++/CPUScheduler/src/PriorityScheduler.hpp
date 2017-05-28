//
//  PriorityScheduler.hpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/29/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#ifndef PriorityScheduler_hpp
#define PriorityScheduler_hpp

#include "Scheduler.hpp"
#include "Process.hpp"
#include "Event.hpp"
#include "Thread.hpp"
#include "Burst.hpp"

#include <stdio.h>
#include <vector>
#include <queue>

class PriorityScheduler : public Scheduler
{
public:
    PriorityScheduler(std::vector<Process*>, std::vector<Event*>, bool, bool);
    
private:
    Thread* selectThread(Process*&);
    void processEvent(Event*);
    
    void addThreadToQueue(Thread*, Priority);
    
    std::queue<Thread*> sysThreads;
    std::queue<Thread*> intThreads;
    std::queue<Thread*> norThreads;
    std::queue<Thread*> batThreads;
    
    bool isFirstThread = true;;
    
};

#endif /* PriorityScheduler_hpp */
