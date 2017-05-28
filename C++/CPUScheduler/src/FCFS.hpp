//
//  FCFS.hpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/22/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#ifndef FCFS_hpp
#define FCFS_hpp

#include "Scheduler.hpp"
#include "Process.hpp"
#include "Event.hpp"
#include "Thread.hpp"
#include "Burst.hpp"

#include <stdio.h>
#include <vector>

class FCFS : public Scheduler
{
public:
    FCFS(std::vector<Process*>, std::vector<Event*>, bool, bool);
    
private:
    Thread* selectThread(Process*&);
    void processEvent(Event*);
};

#endif /* FCFS_hpp */
