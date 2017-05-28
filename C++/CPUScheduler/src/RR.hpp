//
//  RR.hpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/29/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#ifndef RR_hpp
#define RR_hpp

#include "Scheduler.hpp"
#include "Process.hpp"
#include "Event.hpp"
#include "Thread.hpp"
#include "Burst.hpp"

#include <stdio.h>
#include <vector>

class RR : public Scheduler
{
public:
    RR(std::vector<Process*>, std::vector<Event*>, bool, bool);
    
private:
    static const int TIME_SLICE = 3;
    Thread* selectThread(Process*&);
    void processEvent(Event*);
};


#endif /* RR_hpp */
