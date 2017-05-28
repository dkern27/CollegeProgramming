/**
 * This file contains implementations for methods in the flag_parser.h file.
 *
 * You'll need to add code here to make the corresponding tests pass.
 */

#include "flag_parser/flag_parser.h"
#include <iostream>
#include <getopt.h>
#include <cstring>

using namespace std;


void print_usage() {
  cout <<
      "Usage: mem-sim [options] filename\n"
      "\n"
      "Options:\n"
      "  -v, --verbose\n"
      "      Output information about every memory access.\n"
      "\n"
      "  -s, --strategy (FIFO | LRU)\n"
      "      The replacement strategy to use. One of FIFO or LRU.\n"
      "\n"
      "  -f, --max-frames <positive integer>\n"
      "      The maximum number of frames a process may be allocated.\n"
      "\n"
      "  -h --help\n"
      "      Display a help message about these flags and exit\n"
      "\n";
}


bool parse_flags(int argc, char** argv, FlagOptions& flags) {
    const struct option long_options[] = {
        {"verbose", no_argument, 0, 'v'},
        {"strategy", required_argument, 0, 's'},
        {"max-frames", required_argument, 0, 'f'},
        {"help", no_argument, 0, 'h'},
        {0,0,0,0}
    };
    
    int c = 0, option_index;
    while ((c = getopt_long(argc, argv, "vs:f:h", long_options, &option_index)) != -1)
    {
        switch (c)
        {
            case 'v':
                flags.verbose = true;
                break;
            case 's':
                if ( strcmp(optarg, "LRU") == 0)
                    flags.strategy = ReplacementStrategy::LRU;
                else if(strcmp( optarg, "FIFO") == 0)
                    flags.strategy = ReplacementStrategy::FIFO;
                else
                {
                    cout << "Invalid strategy specified" << endl;
                    return false;
                }
                break;
            case 'f':
                try
                {
                    flags.max_frames = stoi(optarg);
                }
                catch (invalid_argument& ia)
                {
                    cout << "Frames entered was not a number" << endl;
                    return false;
                }
                if(flags.max_frames <= 0)
                {
                    cout << "Invalid number of frames entered" <<endl;
                    return false;
                }
                break;
            case 'h':
                print_usage();
                return false;
                break;
            default: //Invalid option
                return false;
        }
    }
    
    if (optind < argc)
    {
        while (optind < argc)
            flags.filename = argv[optind++];
    }
    else
    {
        cout << "No file specified" << endl;
        return false;
    }
    
  return true;
}
