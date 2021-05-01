##############################################################
# this is a tester file, you are not required to submit this
# file in the final solution, please ensure your submitted q1 file
# can run without any issues with this tester file to avoid any penalty
##############################################################
from q1 import find_cost

score = 0
test_cases = [[[1, 2, 3, 4], [1, 2, 3, 4], [1, 2, 3, 4], [1, 2, 3, 4]],
              [[0, 1, 2, 3], [-1, 0, 1, 2], [-2, -1, 0, 1], [-3, -2, -1, 0]],
              [[2, 4, 9, 8, 8, 1, 3, 3, 5, 7], [4, 2, 2, 8, 3, 1, 6, 5, 4, 7], [4, 1, 6, 9, 4, 5, 5, 8, 2, 1]],
              [[9, 7], [1, 5], [9, 4], [4, 5], [3, 7], [5, 8]],
              [[8, 9, 5, 8], [4, 6, 2, 3], [7, 5, 6, 1], [2, 5, 4, 8]]]
expected_lowest_cost = [4, -6, 5, 24, 12]
expected_path = [[1, 1, 1, 1], [-3, -2, -1, 0], [1, 2, 2], [5, 3, 4, 4, 1, 7], [4, 1, 2, 5]]

num_test_cases = len(test_cases)
print("Number of test cases : " + str(num_test_cases))

for i in range(len(test_cases)):
    try:
        cost_grid = test_cases[i]
        lowest_cost, path = find_cost(cost_grid)
        print ("Test Case " + str(i+1))
        for row in cost_grid:
            print(row)
        
        print("Expected Lowest Cost: " + str(expected_lowest_cost[i]))
        print("         Lowest Cost: " + str(lowest_cost))
        print("Expected Path: ", end=" ")
        print(expected_path[i])
        print("         Path: ", end=" ")
        print(path)
        print('--------------------------------------')
        if lowest_cost == expected_lowest_cost[i] and path == expected_path[i]:
            score += 1

    except Exception as e:
            print('Result  : Failed - Exception ' + str(e))
      
print ("Number of test cases tested : " + str(num_test_cases))
print ("Number of test cases passed : " + str(score))
print('--------------------------------------')