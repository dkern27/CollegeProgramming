/**
 * This file contains implementations for methods in the PhysicalAddress class.
 *
 * You'll need to add code here to make the corresponding tests pass.
 */

#include "physical_address/physical_address.h"
#include <bitset>

using namespace std;


string PhysicalAddress::to_string() const {
    string frameBin = bitset<10>(frame).bitset::to_string();
    string offsetBin = bitset<6>(offset).bitset::to_string();
    return frameBin + offsetBin;
}


ostream& operator <<(ostream& out, const PhysicalAddress& address) {
  out << "physical address " << address.to_string() << " [frame: " << address.frame << "; offset: " << address.offset << "]";
  return out;
}
