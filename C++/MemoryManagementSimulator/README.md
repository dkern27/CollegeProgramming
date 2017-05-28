# Project 3
# Dylan Kern

A simulation of a trival OS memory manager.
-------------------------------------------------------------
List of files
-------------------------------------------------------------
src/flag_parser/flag_parser.cpp
src/flag_parser/flag_parser.h
src/flag_parser/flag_parser_tests.cpp
Flag_parser class. Reads flags from command line arguments.

src/frame/frame.cpp
src/frame/frame.h
src/frame/frame_tests.cpp
Frame of memory that holds a page

src/main.cpp
Calls flag parser and runs simulation

src/page/page.cpp
src/page/page.h
src/page/page_tests.cpp
Page of a process. Reads input stream to intialize it self.

src/page_table/page_table.cpp
src/page_table/page_table.h
src/page_table/page_table_tests.cpp
Contains status of each page in a process

src/physical_address/physical_address.cpp
src/physical_address/physical_address.h
src/physical_address/physical_address_tests.cpp
Physical address associated with a virtual address

src/process/process.cpp
src/process/process.h
src/process/process_tests.cpp
Process being handled by simulation. Reads from input stream and sets up pages.

src/simulation/simulation.cpp
src/simulation/simulation.h
Runs the simulation. Handles memory access and page faults. Also does output.

src/virtual_address/virtual_address.cpp
src/virtual_address/virtual_address.h
src/virtual_address/virtual_address_tests.cpp
Virtual address being managed. Read in from the input file.

-------------------------------------------------------------
Interesting features
-------------------------------------------------------------
None

-------------------------------------------------------------
Hours Spent
-------------------------------------------------------------
D1 - 7 hours
Final - 7 hours
Total - 14 hours

-------------------------------------------------------------
Belady's Anomaly
-------------------------------------------------------------
Belady's anomaly is an anomaly where increasing the number of frames increases the number of page faults. This can occur for certain patterns of memory management. Changing the number of frames will change the order that items are removed, which can lead to worse performance. For example, a pattern with three page frames may have 9 page faults, but using 4 page frames will result in 10 page faults.

With my input example in the inputs folder, there will be an increase from 9 to 10 page faults for an increase of 3 to 4 frames.
Commands to invoke:
./mem-sim -f 3 inputs/belady
./mem-sim -f 4 inputs/belady

Belady's anomaly can occur because FIFO uses a queue to decide which items to remove. Therefore, certain access patterns can lead to a removal pattern that increases page faults wit hmore frames. Newer items can stay at the bottom of the queue longer.

