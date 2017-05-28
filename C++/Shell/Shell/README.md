Dylan Kern
Operating Systems
Shell Project

Files: 
    main.cpp - Runs the shell

    shell_builtins.cpp - Implements various builtins such as cd, ls, and pwd

    shell_cmd_execution.cpp - Allows execution of external commands. Checks if input is valid, then moves on to check if command is in the /bin folder. If it is not in the /bin folder, then the input command must be the path to the command. Pipes and file redirection are implemented for external commands. Only one file redirection is allowed per input, but infinite pipes are allowed.

    shell_core.cpp - core features such as reading in input, assigning variables, history management, aliases.

    shell_tab_completion.cpp - If $ is typed in, it suggests/fills in variables. Empty input line will complete commands. Also matches directories and files by default from readline.

    shell.h - header file. My additional functions are at the very bottom.

    makefile - type "make" to compile project"

Other Information:
    When the shell starts, I set the directory to the home directory because otherwise it is was in a weird nonexistent directory in some cases, which is why some of the D1 tests fail.

    An input such as cat < file | cat succeeds and outputs, but for some reason the shell believes it failed, so you get :(

    Many of the external commands, piping, and file redirection tests fail, but as far as I can tell, it behaves properly when I run it.

Hours spent:
    Deliverable 1 = ~8 hours
    Deliverable 2 = ~10 hours
    Deliverable 3 = ~12 hours
    Total = 30 hours
