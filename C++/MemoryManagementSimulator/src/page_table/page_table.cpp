/**
 * This file contains implementations for methods in the PageTable class.
 *
 * You'll need to add code here to make the corresponding tests pass.
 */

#include "page_table/page_table.h"
#include <climits>
#include <iostream>

using namespace std;


size_t PageTable::get_present_page_count() const {
    int numPresent = 0;
    for (Row r : rows)
    {
        if (r.present)
        {
            numPresent++;
        }
    }
  return numPresent;
}


size_t PageTable::get_oldest_page() const {
    int la = INT_MAX;
    int index = -1;
    for (int i = 0; i < rows.size(); i++)
    {
        if (rows[i].present && rows[i].loaded_at < la)
        {
            la = rows[i].loaded_at;
            index = i;
        }
    }
  return index;
}


size_t PageTable::get_least_recently_used_page() const {
    int lat = INT_MAX;
    int index = -1;
    for (int i = 0; i < rows.size(); i++)
    {
        if (rows[i].present && rows[i].last_accessed_at < lat)
        {
            lat = rows[i].last_accessed_at;
            index = i;
        }
    }
  return index;
}
