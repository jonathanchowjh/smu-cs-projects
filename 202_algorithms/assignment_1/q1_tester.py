##############################################################
# this is a tester file, you are not required to submit this
# file in the final solution
##############################################################
from q1 import encode, decode
import filecmp
import os

count_correct = 0
num_test_cases = 3

for num in range(1, num_test_cases + 1):
    try:
        filename = 'test_' + str(num)
        print('Test case ' + str(num) + ' : ' + filename + '.txt')
        print ('Encoding...')
        encode(filename)
        print ('Decoding...') 
        decode(filename)
        
        original_size = os.stat(filename+".txt").st_size
        print("Original size : ", end="")
        print(original_size)
        zipped_size = os.stat(filename+".payload").st_size
        print("Payload size : ", end="")
        print(zipped_size)
        
        original_file = open(filename + '.txt', "r")
        original_content = original_file.read() #this is a list
        unzipped_file = open(filename + '_unzipped.txt', "r")
        unzipped_content = unzipped_file.read() #this is a list

        if original_content == unzipped_content:
            print('Result : Pass')
            count_correct += 1
        else:
            print('Result : Fail')

        print('--------------------------------------')

    except Exception as e:
            print('Result  : Failed in some disk IO ' + str(e))
      
print ("Number of test cases tested : " + str(num_test_cases))
print ("Number of test cases passed : " + str(count_correct))
print('--------------------------------------')

