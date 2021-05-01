#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

int main(int argc, char *argv[]){
    int t=atoi(argv[1]);
    pid_t pid = getpid();
    for (int i=0; i<5; i++){
        printf("pid %d ticking every %d secs\n", pid, t);
        sleep(t);
    }
    return 0;
}

