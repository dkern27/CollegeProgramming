//
//  Thread.hpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/18/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#ifndef Thread_hpp
#define Thread_hpp

#include "Burst.hpp"
#include <stdio.h>
#include <vector>
#include <climits>

enum State { NEW, READY, RUNNING, BLOCKED, EXIT };

class Thread
{
public:
    Thread();
    Thread(int, int, int, int);
    
    int getId();
    int getArrivalTime();
    int getBurstIndex();
    int getStartTime();
    int getCompleteTime();
    int getCpuTime();
    int getIoTime();
    int getProcessId();
    
    State state = State::NEW;
    std::vector<Burst*> bursts;
    int timesPreempted = 0;
    int remainingCPUTime = INT_MIN;
    
    Burst* currentBurst();
    Burst* nextBurst();
    
    void setStartTime(int);
    void setCompleteTime(int);
    
    int timeSinceReady = INT_MAX;
    int waitTime = 0;
    static int overhead;
private:
    int id;
    int arrivalTime;
    int numBursts;
    int burstIndex = -1;
    int startTime = -1;
    int completeTime = 0;
    int processId;
};

#endif /* Thread_hpp */
