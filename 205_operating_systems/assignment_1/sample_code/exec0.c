#include <stdio.h>
#include <unistd.h>
#include <string.h>

int main(int argc, char *argv[]){

    pid_t pid = fork();

    if (pid > 0){
        printf("parent> created process %d\n", pid);
    }
    else if (pid==0){
        char *arg_list[3] = {"./clock", "2", NULL};
        execvp(arg_list[0], arg_list);
    }
    else{
        fprintf(stderr, "fork failed\n");
    }

    return 0;
}





