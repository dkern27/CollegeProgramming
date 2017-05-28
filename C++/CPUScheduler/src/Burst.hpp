//
//  Burst.hpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/18/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#ifndef Burst_hpp
#define Burst_hpp

#include <stdio.h>

class Burst
{
public:
    Burst(int, int=0);
    
    int getCpu();
    int getIo();
    
private:
    int cpu;
    int io;
};

#endif /* Burst_hpp */
