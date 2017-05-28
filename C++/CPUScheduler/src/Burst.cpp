//
//  Burst.cpp
//  CPUScheduler
//
//  Created by Dylan Kern on 10/18/16.
//  Copyright © 2016 Dylan Kern. All rights reserved.
//

#include "Burst.hpp"

Burst::Burst(int cpu, int io)
{
    this->cpu=cpu;
    this->io=io;
}


int Burst::getCpu()
{
    return cpu;
}

int Burst::getIo()
{
    return io;
}
