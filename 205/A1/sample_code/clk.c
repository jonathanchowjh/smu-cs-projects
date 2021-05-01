#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(int argc, char *argv[]){
    int t = atoi(argv[1]);
    while(1){
        sleep(t);
    }
    return 0;
}


