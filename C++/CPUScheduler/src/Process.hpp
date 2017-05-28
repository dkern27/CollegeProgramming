//
//  Process.hpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/18/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#ifndef Process_hpp
#define Process_hpp

#include "Thread.hpp"
#include <stdio.h>
#include <vector>
#include <string>

enum Priority
{
    SYSTEM = 0,
    INTERACTIVE = 1,
    NORMAL = 2,
    BATCH = 3
};

class Process
{
public:
    Process();
    Process (int, int, int);
    
    bool isProcessDone();
    
    //Getters/Setters
    std::string getPriorityName();
    int getId();
    void setId(int);
    Priority getPriority();
    void setPriority(int);
    int getNumThreads();
    int getArrivalTime();
    
    std::vector<Thread*> threads;
    static int overhead;
    
private:
    int id;
    int numThreads;
    Priority priority;
};

#endif /* Process_hpp */
