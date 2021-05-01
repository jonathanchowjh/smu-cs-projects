#include <stdio.h>
#include <unistd.h>

int main(int argc, char *argv[]){

    int n = 0;
    pid_t pid = fork();

    if (pid >  0){
        n = 100;
        printf("parent> I just created child %d\n", pid);
        printf("parent> n = %d\n", n);
    }
    else if (pid == 0){
        pid_t mypid = getpid();
        n = 1;
        printf("child> My PID is %d\n", mypid);
        printf("child> n = %d\n", n);
    }
    else{
        fprintf(stderr, "fork failed\n");
    }
    return 0;
}