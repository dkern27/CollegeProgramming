//
//  CustomScheduler.hpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/30/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#ifndef CustomScheduler_hpp
#define CustomScheduler_hpp

#include <stdio.h>

#include "Scheduler.hpp"
#include "Process.hpp"
#include "Event.hpp"
#include "Thread.hpp"
#include "Burst.hpp"

#include <stdio.h>
#include <vector>
#include <queue>

class CustomScheduler : public Scheduler
{
public:
    CustomScheduler(std::vector<Process*>, std::vector<Event*>, bool, bool);
    
private:
    Thread* selectThread(Process*&);
    void processEvent(Event*);
    
    float calculateHRRN(Thread*);
    int getTimeSlice(Priority);
    
    const int SYS_TIME_SLICE = 6;
    const int INT_TIME_SLICE = 4;
    const int NOR_TIME_SLICE = 2;
    const int BAT_TIME_SLICE = 1;
    
    void addThreadToQueue(Thread*);
    void updateWaitTimes(int);
    
    void addToPriorityQueue(Thread*, Priority);
    
    std::queue<Thread*> sysThreads;
    std::queue<Thread*> intThreads;
    std::queue<Thread*> norThreads;
    std::queue<Thread*> batThreads;
    
    std::vector< std::vector<Thread*> > feedbackQueues;
    
    bool isFirstThread = true;
};

#endif /* CustomScheduler_hpp */
