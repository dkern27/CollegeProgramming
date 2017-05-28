/**
 * This file contains implementations of the functions that are responsible for
 * executing commands that are not builtins.
 *
 * Much of the code you write will probably be in this file. Add helper methods
 * as needed to keep your code clean. You'll lose points if you write a single
 * monolithic function!
 */

#include "shell.h"
#include <cstdlib>
#include <cstdio>
#include <unistd.h>
#include <cstring>
#include <iostream>
#include <vector>
#include <sys/wait.h>
#include <cerrno>
#include <fcntl.h>
#include <sys/stat.h>

using namespace std;


int Shell::execute_external_command(vector<string>& tokens) {
    if(!validInput(tokens))
        return 1;
    checkBinForCommands(tokens);
    bool containsPipes = false;
    for (int i=0; i<tokens.size(); i++)
    {
        if (tokens[i] == "|")
        {
            containsPipes = true;
            break;
        }
    }
    if (containsPipes)
        return executePipedCommands(tokens);
    else
        return executeSingleCommand(tokens);
}

int Shell::executeSingleCommand(vector<string>& tokens)
{
    char** args = getExternalCommandArgs(tokens);
    int file;
    pid_t pid;
    int status;
    
    if((pid=fork()) < 0) //Try to create child process
    {
        perror("Error on fork()");
        exit(-1);
    }

    if(pid == 0)
    {
        chdir(getenv("PATH"));
        //File Redirection
        for (int i=0; i<tokens.size(); i++)
        {
            // > and >>
            if(tokens[i].find(">") != string::npos)
            {
                int oflag = O_CREAT | O_WRONLY | O_TRUNC;
                if(tokens[i] == ">>")
                    oflag = O_CREAT | O_WRONLY | O_APPEND;
                file = open(tokens[i+1].c_str(), oflag, S_IWRITE | S_IREAD);
                if(file == -1)
                {
                    perror("Error opening file");
                    _exit(-1);
                }
                dup2(file, STDOUT_FILENO);
                close(file);
                break;
            }
            // <
            if(tokens[i] == "<")
            {
                file = open(tokens[i+1].c_str(), O_RDONLY);
                if(file == -1)
                {
                    perror("Error opening file");
                    _exit(-1);
                }
                dup2(file, STDIN_FILENO);
                close(file);
                break;
            }
        }
        //Set child process to current path
        if(execvp(args[0], args) < 0) //Run Command
        {
            perror("Error executing external command");
            _exit(-1);
        }
    }
    
    //Make sure we don't proceed until the command finishes
    waitpid(pid, &status, WUNTRACED);
    return status;
}

char** Shell::getExternalCommandArgs(vector<string>& tokens)
{
    char** args = new char*[tokens.size()+1];
    for (int i = 0; i < tokens.size(); i++)
    {
        if(tokens[i] == ">" || tokens[i] == ">>" || tokens[i] == "<")
        {
            args[i] = NULL;
            break;;
        }
        char* cstr = new char[tokens[i].size()];
        strcpy(cstr, tokens[i].c_str());
        args[i]=cstr;
    }
    args[tokens.size()] = NULL;
    
    return args;
}

//These SO questions were very helpful to understand how dup2 was used and in general multiple pipes
//http://stackoverflow.com/questions/8389033/implementation-of-multiple-pipes-in-c
//http://stackoverflow.com/questions/8082932/connecting-n-commands-with-pipes-in-a-shell
int Shell::executePipedCommands(vector<string> tokens)
{
    //File Redirection
    int file;
    int oflag=0;
    bool redirectIn = false;
    bool redirectOut = false;
    int fileIndex=0;
    for (int i=0; i<tokens.size(); i++)
    {
        // > and >>
        if(tokens[i].find(">") != string::npos)
        {
            oflag = O_CREAT | O_WRONLY | O_TRUNC;
            if(tokens[i] == ">>")
                oflag = O_CREAT | O_WRONLY | O_APPEND;
            fileIndex = i+1;
            redirectOut = true;
            break;
        }
        // <
        if(tokens[i] == "<")
        {
            fileIndex = i+1;
            redirectIn = true;
            break;
        }
    }

    //Handle Pipes
    int numPipes = 0;
    for (int i = 0; i < tokens.size(); i++)
    {
        if(tokens[i].find("|") != string::npos)
            numPipes++;
    }
    vector<vector<string> > commands = makeCommands(tokens, numPipes);
    
    int status = 0;
    int pipes[numPipes*2];

    pid_t pids[commands.size()];
    
    //Start all the pipes
    for (int i = 0; i < numPipes*2; i += 2) {
        pipe(pipes + i);
    }
    
    for (int i = 0; i < commands.size(); i++) {
        if ((pids[i] = fork()) < 0)
        {
            perror("Error on fork()");
            exit(-1);
        }
        chdir(getenv("PATH"));
        if (pids[i] == 0)
        {
            if(i != commands.size()-1)
            {
                if( i==0 && redirectIn) //File redirection
                {
                    file = open(tokens[fileIndex].c_str(), O_RDONLY);
                    dup2(file, STDIN_FILENO);
                    if(file == -1)
                    {
                        perror("Error opening file");
                        _exit(-1);
                    }
                    close(file);
                }
                else
                    dup2(pipes[(2 * i) + 1], 1);
            }
            if( i != 0)
            {
                if ( i == commands.size()-1 && redirectOut) //File redirection
                {
                    file = open(tokens[fileIndex].c_str(), oflag, S_IWRITE | S_IREAD);
                    if(file == -1)
                    {
                        perror("Error opening file");
                        _exit(-1);
                    }
                    dup2(file, STDOUT_FILENO);
                    close(file);
                }
                dup2(pipes[2 * (i - 1)], 0);
            }
            for(int i=0; i < numPipes*2;i++) //Close all pipes
                close(pipes[i]);
            
            // Execute the command
            char ** cmd = getExternalCommandArgs(commands[i]);
            if(execvp(cmd[0], cmd) < 0) //Run Command
            {
                perror("Error executing external command");
                _exit(-1);
            }
        }
    }
    for(int i=0; i < numPipes*2;i++) //Close all pipes
        close(pipes[i]);
    for (int i = 0; i < commands.size(); i++) {
        waitpid(pids[i], &status, WUNTRACED); //Other wait function would hang and exit program, so I used this one
    }
    return status;
}

vector<vector<string> > Shell::makeCommands(vector<string>tokens, int numPipes)
{
    int pos = 0;
    vector<vector<string> > commands;
    while(commands.size() != numPipes+1)
    {
        vector<string> cmd;
        for (int i=pos; i  < tokens.size(); i++)
        {
            if(tokens[i] == "|")
            {
                pos = i + 1;
                break;
            }
            if(tokens[i].find(">") != string::npos || tokens[i] == "<")
            {
                pos = i + 2;
                break;
            }
            cmd.push_back(tokens[i]);
            
        }
        commands.push_back(cmd);
    }
    return commands;
}

bool Shell::validInput(vector<string> tokens)
{
    for (int i=0; i<tokens.size(); i++)
    {
        if(tokens[i] == "|")
        {
            if(i == tokens.size()-1 || tokens[i+1] == "|" || tokens[i+1] == "<" || tokens[i+1].find(">") != string::npos)
            {
                // |,<,> at end of input or consecutive |, <, >
                return false;
            }
            for (int j = i+1; j< tokens.size(); j++)
            {
                if (tokens[j] == "<")
                    return false; //file input after pipe
            }
        }
        if (tokens[i] == "<" || tokens[i].find(">") != string::npos)
        {
            if(i == tokens.size() -1 || tokens[i+1] == "|")
                return false; // File redirection followed by pipe
            for (int j = i+1; j< tokens.size(); j++)
            {
                if (tokens[j] == "<" || tokens[j].find(">") != string::npos)
                    return false; //More than one file redirection
            }
        }
    }
    return true;
}

void Shell::checkBinForCommands(vector<string>& tokens)
{
    for(int i=0; i<tokens.size(); i++)
    {
        if(tokens[i] == "|")
        {
            continue;
        }
        if(tokens[i] == "<" || tokens[i].find(">") != string::npos)
        {
            i++; //next token is file
            continue;
        }
        struct stat st;
        string path = "/bin/" + tokens[i];
        if(stat(path.c_str(), &st) == 0 && S_ISREG(st.st_mode))
        {
            tokens[i] = path;
            i++; //In case of scenario like "cat cat" where second cat is not meant to be command
        }
    }
}
