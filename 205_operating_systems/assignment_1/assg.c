#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <readline/readline.h>
#include <readline/history.h>

/**
 * run [programme] [arguments]
 * stop [PID]
 * kill [PID]
 * resume [PID]
 * list
 * exit
 */
#define N 3 // number of processes
#define MAX_N 100 // max number of processes
#define VAR_LENGTH 10 // Variable Length
#define LENGTH 5 // Number of Variables

void err_msg(void) {
  printf("INVALID ARGUMENT. Use one of the following commands\n");
  printf("* run [programme] [arguments]\n* stop [PID]\n* kill [PID]\n* resume [PID]\n* list\n* exit\n");
}

int main(void){
  int max_n = 0;
  while (max_n++ < MAX_N) {
    // USER COMMANDS INPUT
    char input[VAR_LENGTH * LENGTH];
    printf("cs205$ ");
    fgets(input, VAR_LENGTH * LENGTH, stdin);
    
    // splitting commands into array of strings
    char* cmd[LENGTH];
    int c_count = 0;
    cmd[c_count++] = strtok(input, " ");
    while (cmd[c_count - 1] != NULL) {
      cmd[c_count++] = strtok(NULL, " ");
    }

    // Printing splitted cmd
    c_count = 0;
    while (cmd[c_count] != NULL) {
      printf("%s\n", cmd[c_count++]);
    }

    if (strcmp(cmd[0], "run") == 0) {
      if (cmd[1] == NULL) {
        err_msg();
        continue;
      }
      pid_t pid = fork();
      if (pid == 0){
          // child process executes file with arguments
          char file[] = "./";
          strcat(file, cmd[1]);
          char* arg_array[] = {file, NULL, NULL, NULL, NULL};
          c_count = 2;
          while (cmd[c_count] != NULL) {
            arg_array[c_count - 1] = cmd[c_count++];
          }
          execvp(arg_array[0], arg_array);
      } else if (pid>0){
        // parent process keep track of process states
        processes[n_process] = pid;
        killed[n_process] = 0;
        n_process++;
        printf("pid %d created\n", pid);

    }
    } else if (strcmp(cmd[0], "stop") == 0) {
    } else if (strcmp(cmd[0], "kill") == 0) {
    } else if (strcmp(cmd[0], "resume") == 0) {
    } else if (strcmp(cmd[0], "list") == 0) {
    } else if (strcmp(cmd[0], "exit") == 0) {
    } else {
      err_msg();
    }
  }
}
