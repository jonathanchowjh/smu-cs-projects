import copy 
import time
import random
import math
from collections import OrderedDict
from simulator import Simulator
import sys
class Environment(object):
    """Environment within which all agents operate."""
    #fixmovementlist = ['forward','left','forward','right','forward']
    fixmovementlist = [None]
    fixmovement = False
    valid_actions = [None,'forward-3x','forward-2x','forward', 'left', 'right']
    dummy_agent_location_list =[]
    primary_agent_loc=()
    observability = 0 
    visibility_range = 5 
    xadd = 2
    yadd = 3
    goal_list=[]
    def parseConfigFile(self):
        f=open(self.config_file,"r")
        line = f.readline()
        while line:
                print(line)
                linearr=line.split(" ")
                if linearr[0] == "NUMDUMMYAGENTS":
                        num_dummies = int(linearr[1].strip())
                        
                        self.num_dummies = num_dummies
                        dummy_locs_str = linearr[2].split(":")
                        for k in dummy_locs_str:
                            xloc = int(k.split(",")[0])
                            yloc = int(k.split(",")[1])
                            self.dummy_agent_location_list.append((xloc,yloc))
                if linearr[0] == "PRIMARY_AGENT":
                        xloc = int(linearr[1].split(",")[0])
                        yloc = int(linearr[1].split(",")[1])
                        self.primary_agent_loc=(xloc,yloc)
                if linearr[0] == "FIX_MOVEMENT":
                        self.fixmovement=True
                        self.fixmovementlist=[]
                        moves_str = linearr[1].split(":")
                        for k in moves_str:
                                if k.strip()!="None":
                                        self.fixmovementlist.append(k)
                                else:
                                        self.fixmovementlist.append(None)
                if linearr[0] == "VISIBILITY":
                      self.visibility_range = int(linearr[1].strip())
                if linearr[0] == "GOAL":
                      glist=linearr[1].strip().split(",")
                      for k in glist:
                          self.goal_list.append(int(k))
                        
                line=f.readline()
        f.close()
        if len(self.goal_list)==0:
            self.goal_list.append(0)
    
    def __init__(self, verbose=False, num_dummies=20, grid_size = (8, 28),config_file="default_config.txt",fixmovement=False):
        self.num_dummies = num_dummies  # Number of dummy driver agents in the environment
        self.verbose = verbose # If debug output should be given
        self.initial_grid_status = []
        self.config_file=config_file
        self.parseConfigFile()
        for i in range(grid_size[0]-1):
                self.initial_grid_status.append([])
                for j in range(grid_size[1]-1):
                        if self.observability == 1:
                                self.initial_grid_status[i].append(0)
                        else:
                                self.initial_grid_status[i].append(-1)

        self.goal_states=[]
        for i in range(len(self.goal_list)):
                self.goal_states.append({"location":(self.goal_list[i],grid_size[1]-2)})
        self.xmin= 0
        self.xmax = grid_size[0] - 1
        self.ymin = 0
        self.ymax = grid_size[1] - 1
        # Initialize simulation variables
        self.done = False
        self.t = 0
        self.agent_states = OrderedDict()
        self.step_data = {}
        self.success = None
        self.numroads = grid_size[0] - 1;
        self.numroads_h = grid_size[1] - 1;
        # Road network
        self.grid_size = grid_size  # (columns, rows)
        self.bounds = (1, 2, self.grid_size[0], self.grid_size[1] + 1)
        self.block_size = 25
        self.hang = 0.6
        self.roads = []
        self.roads_h = []
        for k in range(self.numroads):
                self.roads.append(((self.bounds[0]+k, self.bounds[1]), (self.bounds[0]+k, self.bounds[3])))
        for k in range(self.numroads_h):
                self.roads_h.append(((self.bounds[0], self.bounds[1]+k), (self.bounds[2], self.bounds[1]+k)))
        #self.roads.append(((self.bounds[0], self.bounds[1]), (self.bounds[0], self.bounds[3])))



        # Create dummy agents
        for i in range(self.num_dummies):
            self.create_agent(DummyAgent,location=self.dummy_agent_location_list[i],fixmovement=self.fixmovement)
                

        # Primary agent and associated parameters
        self.primary_agent = None  # to be set explicitly


    def create_agent(self, agent_class, *args, **kwargs):
        """ When called, create_agent creates an agent in the environment. """

        agent = agent_class(self, *args, **kwargs)
        self.agent_states[agent] = agent.state 
        return agent

    def set_primary_agent(self, agent):
        """ When called, set_primary_agent sets 'agent' as the primary agent.
            The primary agent is the self driving car that is followed in the environment. """

        self.primary_agent = agent
        agent.primary_agent = True
        agent.state={"location":self.primary_agent_loc}
        self.agent_states[agent] = agent.state 

    def reset(self, testing=False):
        """ This function is called at the beginning of a new trial. """

        self.done = False
        self.t = 0
        # Reset status text
        self.step_data = {}
        
    def applyAction(self,agent,state,action):
        inputs = self.sense(agent)
        newstate=copy.deepcopy(state)
        newstate["previous"]=state
        location = state["location"]
        if action is not None:
                newloc = location
                xloc = location[0]
                yloc = location[1]
                if action == "forward":
                        if self.isvalidloc(xloc,yloc+1,inputs):
                                newloc=(xloc,yloc+1)
                elif action == "forward-2x":
                        if self.isvalidloc(xloc,yloc+2,inputs) and self.isvalidloc(xloc,yloc+1,inputs):
                                newloc=(xloc,yloc+2)
                elif action == "forward-3x":
                        if self.isvalidloc(xloc,yloc+3,inputs) and self.isvalidloc(xloc,yloc+2,inputs) and self.isvalidloc(xloc,yloc+1,inputs):
                                newloc=(xloc,yloc+3)
                elif action == "left":
                        if self.isvalidloc(xloc-1,yloc+1,inputs) and self.isvalidloc(xloc-1,yloc,inputs):
                                newloc=(xloc - 1,yloc+1)
                        
                if action == "right":
                        if self.isvalidloc(xloc+1,yloc+1,inputs) and self.isvalidloc(xloc+1,yloc,inputs):
                                newloc=(xloc + 1,yloc+1)
                newstate['location'] = newloc
        return newstate        
    def getGoalStates(self):
        return self.goal_states
    def step(self):
        """ This function is called when a time step is taken turing a trial. """

        # Pretty print to terminal
        print("")
        print("/-------------------")
        print("| Step {} Results".format(self.t))
        print("\-------------------")
        print("")

        if(self.verbose == True): # Debugging
            print("Environment.step(): t = {}".format(self.t))

        # Update agents, primary first
        if self.primary_agent is not None:
            self.primary_agent.update()

        for agent in self.agent_states.keys():
            if agent is not self.primary_agent:
                agent.update()


        self.t += 1

    def sense(self, agent):
        """ This function is called when information is requested about the sensor
            inputs from an 'agent' in the environment. """

        assert agent in self.agent_states, "Unknown agent!"
        grid_status=copy.deepcopy(self.initial_grid_status)
        #state = self.agent_states[agent]
        #location = state['location']
        agent_loc = agent.state["location"]
                
        for other_agent, other_state in self.agent_states.items():
                loc = other_state["location"] 
                if self.observability == 1:
                    grid_status[loc[0]][loc[1]]=1
                elif math.fabs(loc[0] - agent_loc[0]) < self.visibility_range and math.fabs(loc[1] - agent_loc[1]) < self.visibility_range:
                    grid_status[loc[0]][loc[1]]=1
                
        if self.observability == 0:
                for k1 in range(self.visibility_range):
                    for k2 in range(self.visibility_range):
                        if agent_loc[0]-k1 >= 0 and agent_loc[1]+k2 < self.ymax:
                            if grid_status[agent_loc[0] - k1][agent_loc[1]+k2] != 1:
                                grid_status[agent_loc[0] - k1][agent_loc[1] +k2 ] = 0
                        if agent_loc[0]+k1 < self.xmax and agent_loc[1]+k2 < self.ymax:
                            if grid_status[agent_loc[0] + k1][agent_loc[1] + k2] != 1:
                                grid_status[agent_loc[0] + k1][agent_loc[1] + k2] = 0
                    
        return grid_status        


    def isvalidloc(self,xloc,yloc,inputs):
        
        if xloc >= self.xmin and xloc <self.xmax and yloc >=self.ymin and yloc <self.ymax:
                if inputs[xloc][yloc]==-1 or inputs[xloc][yloc]==0:
                        return 1

    def getAction(self,state,nextstate):
        loc1 = state["location"]
        loc2 = nextstate["location"]
        print(loc1,loc2)
        action = None
        if loc1[0] >= self.xmin and loc1[0] <self.xmax and loc1[1] >=self.ymin and loc1[1] <self.ymax:
                if loc2[0] >= self.xmin and loc2[0] <self.xmax and loc2[1] >=self.ymin and loc2[1] <self.ymax:
                        if loc1[0] == loc2[0]:
                            if loc2[1] == loc1[1] + 1:
                                action="forward"
                            elif loc2[1] == loc1[1] + 2:
                                action="forward-2x"
                            elif loc2[1] == loc1[1] + 3:
                                action="forward-3x"
                        elif loc2[1]==loc1[1] +1:
                            if loc1[0] == loc2[0] +1:
                                action = "left"
                            elif loc1[0]+1 ==  loc2[0]:
                                action = "right"
        
        print(action)
        return action
     
    def act(self, agent, action):
        """ Consider an action and perform the action if it is legal.
            Receive a reward for the agent based on traffic laws. """
        assert agent in self.agent_states, "Unknown agent!"
        assert action in self.valid_actions, "Invalid action!"
        
        if agent is self.primary_agent:
                if action is not None:
                    print("agent taking action "+action)
                
        state=agent.state
        location = agent.state['location']
        inputs = self.sense(agent)
        newstate=copy.deepcopy(state)
        agent.action = action
        # Move the agent
        if action is not None:
                
                newloc = location
                xloc = location[0]
                yloc = location[1]
                if action == "forward":
                        if self.isvalidloc(xloc,yloc+1,inputs):
                                newloc=(xloc,yloc+1)
                elif action == "forward-2x":
                        if self.isvalidloc(xloc,yloc+2,inputs) and self.isvalidloc(xloc,yloc+1,inputs):
                                newloc=(xloc,yloc+2)
                elif action == "forward-3x":
                        if self.isvalidloc(xloc,yloc+3,inputs) and self.isvalidloc(xloc,yloc+2,inputs) and self.isvalidloc(xloc,yloc+1,inputs):
                                newloc=(xloc,yloc+3)
                elif action == "left":
                        if self.isvalidloc(xloc-1,yloc+1,inputs):
                                newloc=(xloc - 1,yloc+1)
                        
                if action == "right":
                        if self.isvalidloc(xloc+1,yloc+1,inputs):
                                newloc=(xloc + 1,yloc+1)
                newstate['location'] = newloc
                

        # Did agent reach the goal after a valid move?
        if agent is self.primary_agent:
            for k in self.goal_states:
                    if k["location"] == newstate["location"]:
                        # Stop the trial
                        self.done = True
                        self.success = True
        
                        if(self.verbose == True): # Debugging
                            print("Environment.act(): Primary agent has reached destination!")

            if(self.verbose == True): # Debugging
                print("Environment.act() [POST]: location: {}, action: {}".format(location, action))

            # Update metrics
            self.step_data['t'] = self.t
            self.step_data['state'] = agent.state
            self.step_data['inputs'] = inputs
            self.step_data['action'] = action
            

            if(self.verbose == True): # Debugging
                print("Environment.act(): Step data: {}".format(self.step_data))
        if agent is not self.primary_agent:
            for k in self.goal_states:
                    if k["location"] == newstate["location"]:
                        #Restart the dummy agent from the beginning in same lane
                        loc_x = newstate["location"][0] 
                        loc_y = 0 
                        newstate['location'] = (loc_x,loc_y)
        if agent is self.primary_agent:
                if action is not None:
                    print("Magent taking action "+action+" "+repr(state["location"])+repr(newstate["location"]))
                
        self.agent_states[agent] = newstate
        return newstate


class Agent(object):
    """Base class for all agents."""

    def __init__(self, env,location=None):
        self.env = env
        self.state = {"location":location}
        self.action = None
        self.color = 'white'
        self.primary_agent = False
        self.next_action = None;

    def reset(self, destination=None):
        pass

    def update(self):
        pass

    def get_next_action(self):
        return self.next_action;


class DummyAgent(Agent):
    color_choices = ['cyan', 'red', 'blue', 'green', 'orange', 'magenta', 'yellow']

    def __init__(self, env,location=None,fixmovement=False):
        super(DummyAgent, self).__init__(env,location) 
        loc_x=location[0]
        loc_y=location[1]
        self.state={"location":(loc_x,loc_y)}
        self.fixmovement = fixmovement
        self.actionindex = 0
        self.next_action = random.choice(Environment.valid_actions[3:])
        if self.fixmovement==True:
                self.next_action = env.fixmovementlist[self.actionindex]
        random.seed(self.env.t)
        self.color = random.choice(self.color_choices)

    def update(self):
        """ Update a DummyAgent to move randomly. """


        inputs = self.env.sense(self)
         
        # Check if the chosen waypoint is safe to move to.
        action_okay = True
        action = self.next_action
        self.state=self.env.act(self,action)
        random.seed(self.env.t)
        self.next_action = random.choice(Environment.valid_actions[3:])
        if self.fixmovement==True:
                self.next_action = self.env.fixmovementlist[self.actionindex]
                self.actionindex = (self.actionindex+1)%len(self.env.fixmovementlist)
