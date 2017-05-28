Dylan Kern

FILES
------------------------------
Burst.cpp
Burst.hpp
Class to hold cpu time and io time about individual bursts

Thread.cpp
Thread.hpp
Class for individual threads. Holds bursts, arrival time, state, etc.

Event.cpp
Event.hpp
Class used for the various scheduler events. Also handles verbose output.

Process.cpp
Process.hpp
Class for handling Process. Holds priority, id, and threads.


Scheduler.cpp
Scheduler.hpp
Base class for each scheduling class: FCFS, RR, PriorityScheduler, and CustomScheduler. Handles running the scheduler and output.

FCFS.cpp
FCFS.hpp
First come first served scheduler. Schedules based on ready time

RR.cpp
RR.hpp
Round robin scheduler with time quantum of 3.

PriorityScheduler.cpp
PriorityScheduler.hpp
Priority scheduler. Schedules by the priority of the process.

CustomScheduler.cpp
CustomScheduler.hpp
Custom scheduler implementing a combination of feedback and HRRN.

main.cpp
Parses arguments, reads file, sets up initial event queue, runs scheduler.

makefile
Builds project

ex.txt
Sample input file

INTERESTING FEATURES
------------------------------
None

HOURS SPENT
------------------------------
Part 1: 6
Part 2: 14
Part 3: 10
Total: 30 hours

CUSTOM SCHEDULER
------------------------------
The custom scheduler is an implementation of a multilevel feedback queue and HRRN combined. Threads are placed into feedback queues and then HRRN is used to select from a queue.

Process priorities are directly used for the first time they are selected. If there are several processes that have never been run, the highest priority one is chosen. After this, when a thread is chosen, the priority decides the time slice for the thread. A higher priority has a longer time slice.

The idea was to optimize CPU utilization and to have a reasonable turnaround time for all process types. However, the scheduler tends to do poorly in both respects. The last process to finish will often cause the CPU to be idle for awhile. The turnaround time ended up being longer than desired for all processes.

It uses preemption with time slices as mentioned before. The time slices are as follows:
	System: 6
	Interactive: 4
	Normal: 2
	Batch: 1

There is a separate queue for each priority so that threads with the same priority will be chosen based on arrival time. There are also the feedback queues, which can be an arbitrary number of queues. 

Starvation is possible with a constant stream of processes because they are selected from before selecting for the feedback queues.

In theory, the idea is fair, but with the currently coded time slices for normal and batch threads, they are at a disadvantage because they progress slowly and are put into lower feedback queues. Within feedback queues, however, threads are chosen based on HRRN, so it is more fair at that level.
