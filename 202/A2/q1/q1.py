# Name: Hartono Tjakrawinata @ Jonathan Chow
# Email ID: hartonot.2019@sis.smu.edu.sg
 
##############################################################
# find value of optimal solution using iterative DP
##############################################################
 
def find_cost(cost_grid):
    levels = len(cost_grid)
    if levels <= 0: return 0
    width = len(cost_grid[0])

    path = [[0 for i in range(width)] for i in range(levels)]
    path[0] = cost_grid[0]

    for i in range(0, levels):
        if i == 0: continue
        for j in range(0, width):
            loc = pathLoc(path[i - 1], j)
            path[i][j] = cost_grid[i][j] + path[i - 1][loc]
            # print(path, i, j, loc)
    return get_opt_path(path, cost_grid)

def pathLoc(level, j):
    loc = j if j - 1 < 0 else (j - 1 if level[j - 1] < level[j] else j)
    loc = loc if j + 1 >= len(level) else (j + 1 if level[j + 1] < level[loc] else loc)
    return loc
        
 
##############################################################
# find the optimal solution
##############################################################
def get_opt_path(final_values, cost_grid):
    path = [] # appropriately fill this with the final path from bottom to top, verify results with tester file
    prev = 0
    for i in reversed(range(0, len(final_values))):
        if len(final_values[i]) <= 0: return 0, 0
        # min value for start
        if i == (len(final_values) - 1):
            minimum = 0
            for j in range(1, len(final_values[i])):
                if final_values[i][j] < final_values[i][minimum]:
                    minimum = j
            path.append(cost_grid[i][minimum])
            prev = minimum
        else:
            # min value for each other rank
            loc = pathLoc(final_values[i], prev)
            path.append(cost_grid[i][loc])
            prev = loc
    return sum(path), path
