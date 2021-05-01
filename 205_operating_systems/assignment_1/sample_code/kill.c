#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>

#define N 2

int main(int argc, char *argv[]){

  char *secs[N] = {"2", "2"};
  pid_t pids[N];
  int n = 0, t = 2;
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
  sleep(2);
  kill(pids[0], SIGTERM);
  printf("%d killed\n", pids[0]);
  kill(pids[1], SIGSTOP);
  printf("%d stopped\n", pids[1]);
  printf("wait for 1s\n");
  sleep(1);
  printf("resuming %d\n", pids[1]);
  kill(pids[1], SIGCONT);
  return 0;

}

