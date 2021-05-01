#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>

#define N 3

int main(int argc, char *argv[]){

    char *secs[N] = {"3", "1", "2"};
    pid_t pids[N];
    int n = 0;
    int status;

    for (int i=0;i<N;i++){

        pid_t pid = fork();

        if (pid > 0){
            printf("parent> created process %d\n", pid);
            pids[n++] = pid;
        }
        else if (pid==0){
            char *arg_list[3] = {"./clock", NULL, NULL};
            arg_list[1] = strdup(secs[i]);
            execvp(arg_list[0], arg_list);
        }
        else{
            fprintf(stderr, "fork failed\n");
        }
    }

    int num_done = 0;
    while(num_done<N){
        pid_t child_pid = waitpid(-1, &status, WNOHANG);
        if (child_pid>0){
            printf("parent> %d exited with code %d\n", child_pid, status);
            num_done++;
        }
    }
    return 0;

}

