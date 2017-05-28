/**
 * This file contains implementations for methods in the Page class.
 *
 * You'll need to add code here to make the corresponding tests pass.
 */

#include "page/page.h"
#include <iostream>

using namespace std;


// Ensure PAGE_SIZE is initialized.
const size_t Page::PAGE_SIZE;


Page* Page::read_from_input(std::istream& in) {
    vector<char> bytes;
    char byte;
    while(in.get(byte) && bytes.size() < PAGE_SIZE)
    {
        bytes.push_back(byte);
    }
    if(bytes.size() > 0)
        return new Page(bytes);
    return nullptr;
}


size_t Page::size() const {
  return bytes.size();
}


bool Page::is_valid_offset(size_t offset) const {
  if(offset >= 0 && offset < bytes.size())
     return true;
  return false;
}


char Page::get_byte_at_offset(size_t offset) {
  return bytes[offset];
}
