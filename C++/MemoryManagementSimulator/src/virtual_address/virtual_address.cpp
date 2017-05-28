/**
 * This file contains implementations for methods in the VirtualAddress class.
 *
 * You'll need to add code here to make the corresponding tests pass.
 */

#include "virtual_address/virtual_address.h"
#include <bitset>

using namespace std;


VirtualAddress VirtualAddress::from_string(int process_id, string address) {
    int _page = stoi(address.substr(0, 10), nullptr, 2);
    int _offset = stoi(address.substr(10, -1), nullptr, 2);
    return VirtualAddress(process_id, _page, _offset);
}


string VirtualAddress::to_string() const {
    string pageBin = bitset<10>(page).bitset::to_string();
    string offsetBin = bitset<6>(offset).bitset::to_string();
    return pageBin + offsetBin;
}


ostream& operator <<(ostream& out, const VirtualAddress& address) {
    out << "PID " << address.process_id << " ";
    out << "@ " << address.to_string() << " ";
    out << "[page: " << address.page << "; ";
    out << "offset: " << address.offset << "]";
    return out;
}
