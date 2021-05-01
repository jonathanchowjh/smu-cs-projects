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

}