/**
 * This file contains the definition of the Simulation class.
 *
 * You're free to modify this class however you see fit. Add new methods to help
 * keep your code modular.
 */

#pragma once
#include "process/process.h"
#include "virtual_address/virtual_address.h"
#include "flag_parser/flag_parser.h"
#include "frame/frame.h"
#include "physical_address/physical_address.h"
#include <cstdlib>
#include <map>
#include <vector>

/**
 * Class responsible for running the memory simulation.
 */
class Simulation {
// PUBLIC CONSTANTS
public:

  /**
   * The maximum number of frames in the simulated system (512).
   */
  static const size_t NUM_FRAMES = 1 << 9;
  ReplacementStrategy strategy = ReplacementStrategy::FIFO;
  bool verbose = false;

// PUBLIC API METHODS
public:

  /**
   * Runs the simulation.
   */
  void run(FlagOptions flags);
    
// PRIVATE METHODS
private:

  /**
   * Performs a memory access for the given virtual address, translating it to
   * a physical address and loading the page into memory if needed. Returns the
   * byte at the given address.
   */
  void perform_memory_access(const VirtualAddress& va);

  /**
   * Handles a page fault, attempting to load the given page for the given
   * process into memory.
   */
  int handle_page_fault(Process* process, size_t page);
    
    /*
     * Reads file and sets up data
     */
    void readFile(std::string fileName);
    
    void standardOutput();
    
    void verboseOutput(VirtualAddress va, bool inMemory, PhysicalAddress pa, int rss);
    
    int alreadyLoaded(VirtualAddress va);
    
// INSTANCE VARIABLES
private:
    std::map<int, Process*> processes;
    std::vector<VirtualAddress> virtualAddresses;
    std::vector<Frame> frames;
    int time = 0;
};
