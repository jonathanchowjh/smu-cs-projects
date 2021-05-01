#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <time.h>
#include <setjmp.h>
#include <readline/readline.h>
#include <readline/history.h>

#define N 100
#define M 5
sigjmp_buf env;

pid_t processes[N];
time_t time_p[N];
bool running[N];
bool stopped[N];
// killed if not running or stopped
int n_process = 0;

int status(bool running[], bool stopped[], int num) {
    // printf("%d, %d, \n", running[num], stopped[num]);
    if (running[num]) return 0;
    if (stopped[num]) return 1;
    return 2;
}

int numProcesses (bool running[], int len) {
    int count = 0;
    for (int i = 0; i < len; i++) {
        if (running[i]) {
            count++;
        }
    }
    return count;
}

int whichEarliest(time_t time_p[], bool isState[], int n_process) {
    int earliest = -1;
    for (int i = 0; i < n_process; i++) {
        if (earliest == -1) {
            if (isState[i]) {
                earliest = i;
            }
        } else if (isState[i] && time_p[i] < time_p[earliest]) {
            earliest = i;
        }
    }
    return earliest;
}

// evaluateProcesses(processes, time_p, running, stopped, n_process);
void removeProcess(pid_t processes[], time_t time_p[], bool running[], bool stopped[], int n_process, pid_t exception) {
    if (n_process == 0) return;

    int p_num = numProcesses (running, n_process);
    if (p_num > 3) {
        int earliest = whichEarliest(time_p, running, n_process);
        if (earliest != -1 && processes[earliest] != exception) {
            kill(processes[earliest], SIGSTOP);
            running[earliest] = 0;
            stopped[earliest] = 1;
            time_p[earliest] = time(NULL);
        }
    }
}

void addProcess(pid_t processes[], time_t time_p[], bool running[], bool stopped[], int n_process, pid_t exception) {
    if (n_process == 0) return;

    int p_num = numProcesses (running, n_process);
    // if less than 3, run earliest "stopped" process
    if (p_num < 3) {
        int earliest = whichEarliest(time_p, stopped, n_process);
        if (earliest != -1 && processes[earliest] != exception) {
            kill(processes[earliest], SIGCONT);
            running[earliest] = 1;
            stopped[earliest] = 0;
            time_p[earliest] = time(NULL);
        }
    }
}


void handleTermination() {
    // check for terminated processes
    int stat;
    for (int i = 0; i < n_process; i++) {
        pid_t pid = processes[i];
        if ((running[i] || stopped[i]) && waitpid(pid, &stat, WNOHANG) == pid) {
            running[i] = 0;
            stopped[i] = 0;
            time_p[i] = time(NULL);
            addProcess(processes, time_p, running, stopped, n_process, pid);
        }
    }
}

int main() {
    char *args[M];
    fflush(0);

    // struct sigaction sa;
    // sigemptyset(&sa.sa_mask);
    // sa.sa_handler = handleTerm;
    // sa.sa_flags = 0;
    // sigaction(SIGCHLD, &sa, NULL);
    // signal(SIGCHLD, SIG_IGN);
    // sigsetjmp(env, 1);

    while (n_process<N){

        // tokenize user commands
        char *input = NULL;
        input = readline("cs205$ ");
        char *p = strtok(input, " ");
        char *cmd = p;
        int arg_cnt = 0;
        while (p = strtok(NULL, " ")) {
            args[arg_cnt] = p;
            arg_cnt++;
        }

        handleTermination();

        if (strcmp(cmd,"run")==0) {
            pid_t pid = fork();
            if (pid==0){
                char file[50];
                strcpy(file,  "./");
                strcat(file, args[0]);
                char* arg_array[] = {file, NULL, NULL, NULL, NULL};
                for (int i=1; i<arg_cnt; i++) {
                    arg_array[i] = args[i];
                }
                execvp(arg_array[0], arg_array);
            }
            else if (pid>0){
                processes[n_process] = pid;
                running[n_process] = 1;
                stopped[n_process] = 0;
                time_p[n_process] = time(NULL);
                
                if (numProcesses(running, n_process) >= 3) {
                    kill(pid, SIGSTOP);
                    running[n_process] = 0;
                    stopped[n_process] = 1;
                    time_p[n_process] = time(NULL);
                }
                n_process++;
            }
        }
        else if (strcmp(cmd, "stop")==0){
            pid_t pid = atoi(args[0]);
            bool found = 0;
            for (int i=0; i<n_process; i++){
                if (processes[i]==pid){
                    found = 1;
                    kill(pid, SIGSTOP);
                    running[i] = 0;
                    stopped[i] = 1;
                    time_p[i] = time(NULL);
                    printf("%d stopped\n", pid);
                    break;
                }
            }
            if (!found){
                printf("pid not found\n");
            }
            addProcess(processes, time_p, running, stopped, n_process, pid);
        }
        else if (strcmp(cmd, "resume")==0){
            pid_t pid = atoi(args[0]);
            bool found = 0;
            for (int i=0; i<n_process; i++){
                if (processes[i]==pid){
                    found = 1;
                    kill(pid, SIGCONT);
                    running[i] = 1;
                    stopped[i] = 0;
                    time_p[i] = time(NULL);
                    printf("%d resumed\n", pid);
                    break;
                }
            }
            if (!found){
                printf("pid not found\n");
            }
            removeProcess(processes, time_p, running, stopped, n_process, pid);
        }
        else if (strcmp(cmd, "kill")==0){
            // check if pid exists and kill if found 
            pid_t pid = atoi(args[0]);
            bool found = 0;
            for (int i=0; i<n_process; i++){
                if (processes[i]==pid){
                    found = 1;
                    kill(pid, SIGTERM);
                    running[i] = 0;
                    stopped[i] = 0;
                    time_p[i] = time(NULL);
                    printf("%d killed\n", pid);
                    break;
                }
            }
            if (!found){
                printf("pid not found\n");
            }
            addProcess(processes, time_p, running, stopped, n_process, pid);
        }
        else if (strcmp(cmd, "list")==0){
            // loop through all child processes, display status
            for (int i=0;i<n_process;i++){
                printf("%d,%d,%ld\n", processes[i], status(running, stopped, i), time_p[i]);
            }
        }
        else if (strcmp(cmd, "exit")==0){
            for (int i=0;i<n_process;i++){
                kill(processes[i], SIGTERM);
                running[i] = 0;
                stopped[i] = 0;
                printf("%d killed\n", processes[i]);
            }
            break;
        }
        else {
            printf("invalid command\n");
        }
    }
    return 0;
}
