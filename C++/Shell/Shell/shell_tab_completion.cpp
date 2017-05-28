/**
 * This file contains implementations of the functions that provide
 * tab-completion for the shell.
 *
 * You will need to finish the implementations of the completion functions,
 * though you're spared from implementing the high-level readline callbacks with
 * their weird static variables...
 */

#include "shell.h"
#include <cstdlib>
#include <iostream>
#include <readline/readline.h>
#include <readline/history.h>
#include <sys/stat.h>
#include <dirent.h>
#include <unistd.h>

using namespace std;


void Shell::get_env_completions(const char* text, vector<string>& matches) {
    string t(text);
    if(t.size()==1)
    {
        for(int i=0; environ[i]!=NULL; i++)
        {
            string name(environ[i]);
            matches.push_back("$" + name.substr(0, name.find("=")));
        }
        for (map<string,string>::iterator it = localvars.begin(); it != localvars.end(); it++)
        {
            matches.push_back("$" + it->first);
        }
    }
    else
    {
        t = t.substr(1, string::npos);
        for(int i=0; environ[i]!=NULL; i++)
        {
            string name(environ[i]);
            if(name.find(t) == 0)
                matches.push_back("$" + name.substr(0, name.find("=")));
        }
        for (map<string,string>::iterator it = localvars.begin(); it != localvars.end(); it++)
        {
            string varName = it->first;
            if(varName.find(t) == 0)
                matches.push_back("$" + varName);
        }
    }
}


void Shell::get_command_completions(const char* text, vector<string>& matches) {
    string t(text);

    if(t.size() == 0)
    {
        for (std::map<string,builtin_t>::iterator it = builtins.begin(); it != builtins.end(); it++)
        {
            matches.push_back(it -> first);
        }
        for (std::map<string,string>::iterator it = aliases.begin(); it != aliases.end(); it++)
        {
            matches.push_back(it -> first);
        }
        getExternalCommands(matches);
    }
    else
    {
        for (std::map<string,builtin_t>::iterator it = builtins.begin(); it != builtins.end(); it++)
        {
            if(it->first.find(text) == 0)
                matches.push_back(it -> first);
        }
        for (std::map<string,string>::iterator it = aliases.begin(); it != aliases.end(); it++)
        {
            if(it->first.find(text) == 0)
                matches.push_back(it -> first);
        }
        getExternalCommands(matches, text);
    }
}


char** Shell::word_completion(const char* text, int start, int end) {
  char** matches = NULL;

  if (text[0] == '$') {
    matches = rl_completion_matches(text, env_completion_generator);
  } else if (start == 0) {
    matches = rl_completion_matches(text, command_completion_generator);
  } else {
      chdir(getenv("PATH"));// We get directory matches for free (thanks, readline!).
  }

  return matches;
}


char* Shell::env_completion_generator(const char* text, int state) {
  // A list of all the matches.
  // Must be static because this function is called repeatedly.
  static vector<string> matches;

  // If this is the first time called, construct the matches list with
  // all possible matches.
  if (state == 0) {
    getInstance().get_env_completions(text, matches);
  }

  // Return a single match (one for each time the function is called).
  return pop_match(matches);
}


char* Shell::command_completion_generator(const char* text, int state) {
  // A list of all the matches.
  // Must be static because this function is called repeatedly.
  static vector<string> matches;

  // If this is the first time called, construct the matches list with
  // all possible matches.
  if (state == 0) {
    getInstance().get_command_completions(text, matches);
  }

  // Return a single match (one for each time the function is called).
  return pop_match(matches);
}


char* Shell::pop_match(vector<string>& matches) {
  if (matches.size() > 0) {
    const char* match = matches.back().c_str();

    // Delete the last element.

    // We need to return a copy, because readline deallocates when done.
    char* copy = (char*) malloc(strlen(match) + 1);
    strcpy(copy, match);

    matches.pop_back();
    return copy;
  }

  // No more matches.
  return NULL;
}

void Shell::getExternalCommands(vector<string>& matches, string input)
{
    DIR* dir;
    dirent* dirItem;
    char* path = getenv("PATH");
    string pathString(path);
    dir = opendir(path);
    while((dirItem = readdir(dir)) != NULL)
    {
        struct stat st;
        string name = dirItem->d_name;
        string file = pathString + "/" + name;
        if(stat(file.c_str(), &st) == 0 && (st.st_mode & S_IEXEC) && !S_ISDIR(st.st_mode))
        {
            if(input == "" || name.find(input) == 0)
                matches.push_back(name);
        }
    }
    closedir(dir);
}
