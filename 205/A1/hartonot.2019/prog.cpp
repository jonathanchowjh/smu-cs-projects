#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <iostream>
#include <fstream>
#include <unistd.h>

using namespace std;

int main(int argc, char *argv[]) {
  if (argc!=3){
    fprintf(stderr, "usage: ./prog <filename> <sleep secs>\n");
  }
  else{
    ofstream myfile;
    string filename(argv[1]);
    int n = atoi(argv[2]);
    for (int i=1; i<=n; i++){
      myfile.open(filename);
      myfile << "Process ran " << i << " out of " << n << " secs\n";
      myfile.close();
      sleep(1);
    }
  }
  return 0;
}



