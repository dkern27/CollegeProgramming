/**
 * This file contains implementations for methods in the Process class.
 *
 * You'll need to add code here to make the corresponding tests pass.
 */

#include "process/process.h"
#include <iostream>

using namespace std;

int Process::MAX_FRAMES = 10;

Process* Process::read_from_input(istream& in) {
    string buffer;
    int numBytes = 0;
    vector<Page*> newPages;
    getline(in, buffer);
    numBytes = buffer.size();
    while(buffer.size() > 0)
    {
        istringstream stream(buffer);
        Page* page = Page::read_from_input(stream);
        newPages.push_back(page);
        if(page->size() >= buffer.size())
            break;
        buffer = buffer.substr(page->size(), string::npos);
    }
    Process* p = new Process(numBytes, newPages);
    return p;
}


size_t Process::size() const {
    return num_bytes;
}


bool Process::is_valid_page(size_t index) const {
    if (index < pages.size() && index >= 0)
        return true;
  return false;
}


size_t Process::get_rss() const {
  return page_table.get_present_page_count();
}


double Process::get_fault_percent() const {
    if(memory_accesses == 0)
        return 0.0;
    double faultPercent = double(page_faults)/double(memory_accesses);
    return faultPercent*100;
}
