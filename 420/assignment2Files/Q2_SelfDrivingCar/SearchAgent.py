import random
import math
from environment import Agent, Environment
from simulator import Simulator
import sys
from searchUtils import searchUtils
import heapq

class Node():
    def __init__(self, parent=None, loc=None):
        self.parent = parent
        self.loc = loc
        self.g = 0
        self.h = 0
        self.f = 0

    def __eq__(self, other):
        return self.loc[0] == other.loc[0] and self.loc[1] == other.loc[1]
    def __lt__(self, other):
        return self.f < other.f
    def __gt__(self, other):
        return self.f > other.f
    def __str__(self):
        return str(self.loc) + str(self.f)
    def __repr__(self):
        return str(self.loc) + str(self.f)

class SearchAgent(Agent):
    """ An agent that drives in the Smartcab world.
        This is the object you will be modifying. """ 

    def __init__(self, env,location=None):
        super(SearchAgent, self).__init__(env)     # Set the agent in the evironment 
        self.valid_actions = self.env.valid_actions  # The set of valid actions
        self.action_sequence=[]
        self.searchutil = searchUtils(env)

    def choose_action(self):
        """ The choose_action function is called when the agent is asked to choose
            which action to take next"""

        # Set the agent state and default action
        action=None
        if len(self.action_sequence) >=1:
            action = self.action_sequence[0] 
        if len(self.action_sequence) >=2:
            self.action_sequence=self.action_sequence[1:]
        else:
            self.action_sequence=[]
        return action

    def drive(self,goalstates,inputs):
        """Write your algorithm for self driving car"""
        # get start and end
        start = self.state['location']
        end = goalstates[0]['location']

        # run astar algorithm, get path
        goalReached, path = self.a_star(inputs, start, end)

        # return action sequence from path
        act_sequence=[]
        prev = None
        for step in path:
            if prev is None:
                prev = step
                continue
            x = prev[0] - step[0]
            y = prev[1] - step[1]
            if x == 1 and y == 1:
                act_sequence.append('right')
            elif x == -1 and y == 1:
                act_sequence.append('left')
            elif x == 0 and y == 1:
                act_sequence.append('forward')
            elif x == 0 and y == 2:
                act_sequence.append('forward-2x')
            elif x == 0 and y == 3:
                act_sequence.append('forward-3x')
        return act_sequence
    
    def a_star(self, maze, start, end):
        maze1dLength = len(maze)
        maze2dLength = len(maze[0])
        
        startNode = Node(None, start)
        endNode = Node(None, end)
        openList = []
        closedList = []
        
        openList.append(startNode)
        while len(openList) > 0:
            # get current Node
            currentNode = heapq.heappop(openList)
            closedList.append(currentNode)

            # create path
            if currentNode == endNode:
                return endNode, self.createPath(currentNode)

            # Generate children
            children = []
            for action in [(1, 1), (-1, 1), (0, 1), (0, 2), (0, 3)]: # actions
                loc = (currentNode.loc[0] + action[0], currentNode.loc[1] + action[1])
                if not (loc[0] < maze1dLength and loc[1] < maze2dLength and loc[0] >= 0 and loc[1] >= 0): # not in maze
                    continue
                if maze[loc[0]][loc[1]] == 1: # have car
                    continue
                if action[1] > 1 and maze[loc[0]][loc[1] - 1] == 1: # if moves 2 steps or more, check car between
                    continue
                if action[1] > 2 and maze[loc[0]][loc[1] - 2] == 1: # if moves 3 steps or more, check car between
                    continue
                newNode = Node(currentNode, loc)
                children.append(newNode)
            
            # add child to set
            for child in children:
                # not in closed List
                if self.isInList(child,closedList) is not None:
                    continue
                
                # derive cost
                child.g = currentNode.g + 1
                child.h = self.hCost(child,endNode)
                child.f = child.g + child.h
                
                # not in openList and not greaterCost than in openList
                childInOpenList = self.isInList(child,openList)
                if childInOpenList is not None and child.f > childInOpenList.f:
                    continue
                
                # add to openList
                heapq.heappush(openList, child)
        return None, [None]
    
    def hCost(self,child,endNode):
        return (abs(child.loc[0] - endNode.loc[0]) + abs(child.loc[1] - endNode.loc[1]))
    
    def createPath(self,currentNode):
        path = []
        current = currentNode
        while current is not None:
            path.append(current.loc)
            current = current.parent
        return path
    
    def isInList(self,x,xList):
        for item in xList:
            if x == item:
                return item
        return None

    def update(self):
        """ The update function is called when a time step is completed in the 
            environment for a given trial. This function will build the agent
            state, choose an action, receive a reward, and learn if enabled. """
        startstate = self.state
        goalstates =self.env.getGoalStates()
        inputs = self.env.sense(self)
        self.action_sequence = self.drive(goalstates,inputs)
        print(self.action_sequence)
        action = self.choose_action()  # Choose an action
        self.state = self.env.act(self,action)        
        return
        

def run(filename):
    """ Driving function for running the simulation. 
        Press ESC to close the simulation, or [SPACE] to pause the simulation. """

    env = Environment(config_file=filename,fixmovement=False)
    
    agent = env.create_agent(SearchAgent)
    env.set_primary_agent(agent)
    
    ##############
    # Create the simulation
    # Flags:
    #   update_delay - continuous time (in seconds) between actions, default is 2.0 seconds
    #   display      - set to False to disable the GUI if PyGame is enabled
    sim = Simulator(env, update_delay=2)
    
    ##############
    # Run the simulator
    ##############
    sim.run()


if __name__ == '__main__':
    run(sys.argv[1])

# CLOSED [(3, 0)0, (3, 3)14.0, (3, 6)13.5, (3, 9)13.0, (3, 12)12.5, (3, 15)12.0, (3, 18)11.5, (3, 21)11.0, (3, 24)10.5, (3, 26)10.5, (3, 23)11.0, (2, 24)11.0, (3, 25)11.0, (2, 26)11.0, (2, 26)11.0, (3, 25)11.0, (3, 20)11.5, (2, 21)11.5, (3, 22)11.5, (2, 25)11.5, (1, 26)11.5, (3, 22)11.5, (2, 23)11.5, (1, 26)11.5, (2, 25)11.5, (2, 23)11.5, (1, 24)11.5, (2, 25)11.5, (3, 17)12.0, (2, 18)12.0, (3, 19)12.0, (1, 25)12.0, (1, 25)12.0, (2, 20)12.0, (2, 22)12.0, (3, 19)12.0, (1, 23)12.0, (2, 22)12.0, (0, 24)12.0, (2, 22)12.0, (0, 26)12.0]