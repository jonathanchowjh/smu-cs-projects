import random
import math
from environment import Agent, Environment
from simulator import Simulator
import sys
class RandomAgent(Agent):
    """ An agent that randomly drives the self driving car. """

    def __init__(self, env):
        super(RandomAgent, self).__init__(env)     # Set the agent in the evironment 
        self.valid_actions = self.env.valid_actions  # The set of valid actions


    def choose_action(self):
        """ The choose_action function is called when the agent is asked to choose
            which action to take"""

        # Set the agent state and default action
        action = random.choice(self.valid_actions) 

        return action


    def update(self):
        """ The update function is called when a time step is completed in the 
            environment for a given trial. This function will choose an action
            and update the agent's state """
        action = self.choose_action()  # Choose an action
        self.state = self.env.act(self,action)        
        return
        

def run(filename):
    """ Driving function for running the simulation. 
        Press ESC to close the simulation, or [SPACE] to pause the simulation. """
    #change fixmovement to True to fix the movement sequence of other cars across runs
    env = Environment(config_file=filename,fixmovement=False)
    
    agent = env.create_agent(RandomAgent)
    env.set_primary_agent(agent)

    ##############
    # Create the simulation
    # Flags:
    #   update_delay - continuous time (in seconds) between actions, default is 2.0 seconds
    #   display      - set to False to disable the GUI if PyGame is enabled
    sim = Simulator(env, update_delay=2)
    
    ##############
    # Run the simulator
    sim.run()


if __name__ == '__main__':
    run(sys.argv[1])
