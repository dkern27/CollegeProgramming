/**
 * This file contains the implementations of the builtin functions provided by
 * the shell (or will, once you've finished implementing them all).
 */

#include "shell.h"
#include <cstdlib>
#include <iostream>
#include <dirent.h>
#include <sys/stat.h>
#include <readline/history.h>

using namespace std;


int Shell::com_ls(vector<string>& argv) {
    if (argv.size() > 2) //Do not allow more than one argument for now
        return 1;
    DIR* dir;
    dirent* dirItem;
    if(argv.size() > 1) //Directory specified
    {
        string path = pathExists(argv[1]);
        if(path.size() == 0)
        {
            return 2;
        }
        if((dir = opendir(path.c_str())) == NULL)
            return 2;
    }
    else //No directory specified
    {
        char* path = getenv("PATH");
        if((dir = opendir(path)) == NULL)
            return 2;
    }
    while((dirItem = readdir(dir)) != NULL){
        string name = dirItem -> d_name;
        if(name[0] != '.') //do not show hidden files
        {
            cout << name + " ";
        }
    }
    closedir(dir);
    return 0;
}


int Shell::com_cd(vector<string>& argv) {
    if(argv.size() > 2) //Do not allow more than one argument for now
        return 1;
    if(argv.size() > 1)
    {
        string path = pathExists(argv[1]);
        if(path.size() == 0)
        {
            return 2;
        }
        setenv("PATH", path.c_str(), 1);
    }
    else
    {
        char* homeDir = getenv("HOME");
        setenv("PATH", homeDir, 1);
    }
    return 0;
}


int Shell::com_pwd(vector<string>& argv) {
    if(argv.size() > 1) //No arguments allowed
        return 1;
    char* wd = getenv("PATH");
    if(wd != NULL)
    {
        cout << wd;
        return 0;
    }
  return 1;
}


int Shell::com_alias(vector<string>& argv) {
    if (argv.size() > 2)
        return 1;
    if (argv.size() == 1)
    {
        for (std::map<string,string>::iterator it = aliases.begin(); it != aliases.end(); it++)
        {
            cout << it->first << " -> " << it->second << endl;
        }
        return 0;
    }
    if(argv[1].find("=") != -1)
    {
        size_t equalSign = argv[1].find("=");
        string key = argv[1].substr(0, equalSign);
        string value = argv[1].substr(equalSign+1);
        aliases.insert(make_pair(key, value));
        return 0;
    }
  return 1;
}


int Shell::com_unalias(vector<string>& argv) {
    if(argv.size() > 2)
        return 1;
    if(argv.size() > 1)
    {
        if (argv[1] == "-a")
        {
            aliases.clear();
            return 0;
        }
        map<string,string>::iterator it = aliases.find(argv[1]);
        if(it != aliases.end())
        {
            aliases.erase(it);
            return 0;
        }
    }
    return 1;
}


int Shell::com_echo(vector<string>& argv) {
    for(int i = 1; i < argv.size(); i++)
    {
        cout << argv[i] << " ";
    }
    return 0;
}


int Shell::com_history(vector<string>& argv) {
    if(argv.size() > 1)
        return 1;
    HIST_ENTRY** list = history_list();
    for (int i=0; list[i]; i++)
    {
        cout << i+1 << " " << list[i]->line << endl;
    }
  return 0;
}


int Shell::com_exit(vector<string>& argv) {
    exit(EXIT_SUCCESS);
    return 0;
}

//Check if file exists and is a directory
string Shell::pathExists(string path)
{
    struct stat st;
    if(stat(path.c_str(), &st) == 0 && S_ISDIR(st.st_mode))
        return path;
    string wd(getenv("PATH"));
    wd += "/" + path;
    if(stat(wd.c_str(), &st) == 0 && S_ISDIR(st.st_mode))
        return wd;
    return "";
}
