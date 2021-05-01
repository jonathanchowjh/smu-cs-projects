import os
import time
import random
import importlib
import csv
x = 100
y = 25
os.environ['SDL_VIDEO_WINDOW_POS'] = "%d,%d" % (x,y)

class Simulator(object):
    """Simulates agents in a dynamic selfdriving environment.

       Uses PyGame to display GUI, if available.
    """

    colors = {
        'black'   : (  0,   0,   0),
        'white'   : (255, 255, 255),
        'red'     : (255,   0,   0),
        'green'   : (  0, 255,   0),
        'dgreen'  : (  0, 228,   0),
        'blue'    : (  0,   0, 255),
        'cyan'    : (  0, 200, 200),
        'magenta' : (200,   0, 200),
        'yellow'  : (255, 255,   0),
        'mustard' : (200, 200,   0),
        'orange'  : (255, 128,   0),
        'maroon'  : (200,   0,   0),
        'crimson' : (128,   0,   0),
        'gray'    : (155, 155, 155)
    }

    def __init__(self, env, size=None, update_delay=2.0, display=True):
        
        self.env = env
        self.size = size if size is not None else ((3*self.env.grid_size[0] + 1) * self.env.block_size, (self.env.grid_size[1] + 2) * self.env.block_size)
        self.width, self.height = self.size
        self.road_width = self.env.block_size

        self.bg_color = self.colors['gray']
        self.road_color = self.colors['black']
        self.line_color = self.colors['mustard']
        self.boundary = self.colors['black']
        self.stop_color = self.colors['crimson']

        self.quit = False
        self.start_time = None
        self.current_time = 0.0
        self.last_updated = 0.0
        self.update_delay = update_delay  # duration between each step (in seconds)
        self.paused = False

        self.display = display
        if self.display:
            try:
                self.pygame = importlib.import_module('pygame')
                self.pygame.init()
                self.screen = self.pygame.display.set_mode(self.size)


                self.frame_delay = max(1, int(self.update_delay/4 * 1000))  # delay between GUI frames in ms (min: 1)
                self.agent_sprite_size = (self.env.block_size-5, self.env.block_size-5)
                self.primary_agent_sprite_size = (self.env.block_size-5, self.env.block_size-5)
                self.agent_circle_radius = 20  # radius of circle, when using simple representation
                for agent in self.env.agent_states:
                    if agent.color == 'white':
                        agent._sprite = self.pygame.transform.smoothscale(self.pygame.image.load(os.path.join("images", "car-{}.png".format(agent.color))), self.primary_agent_sprite_size)
                    else:
                        agent._sprite = self.pygame.transform.smoothscale(self.pygame.image.load(os.path.join("images", "car-{}.png".format(agent.color))), self.agent_sprite_size)
                    agent._sprite_size = (agent._sprite.get_width(), agent._sprite.get_height())

                self.font = self.pygame.font.Font(None, 30)
                self.paused = False
            except ImportError as e:
                self.display = False
                print("Simulator.__init__(): Unable to import pygame; display disabled.\n{}: {}".format(e.__class__.__name__, e))
            except Exception as e:
                self.display = False
                print("Simulator.__init__(): Error initializing GUI objects; display disabled.\n{}: {}".format(e.__class__.__name__, e))


    def run(self):
        """ Run a simulation of the environment.""" 

        self.quit = False

        # Get the primary agent
        a = self.env.primary_agent
        self.env.reset()
        self.current_time = 0.0
        self.last_updated = 0.0
        self.start_time = time.time()

        #while True:
        while True:
                try:
                    # Update current time
                    self.current_time = time.time() - self.start_time

                    # Handle GUI events
                    if self.display:
                        for event in self.pygame.event.get():
                            if event.type == self.pygame.QUIT:
                                self.quit = True
                            elif event.type == self.pygame.KEYDOWN:
                                if event.key == 27:  # Esc
                                    self.quit = True
                                elif event.unicode == u' ':
                                    self.paused = True

                        if self.paused:
                            self.pause()

                    # Update environment
                    if self.paused == False and self.env.done == False and (self.current_time - self.last_updated) >= self.update_delay:
                        self.env.step()
                        self.last_updated = self.current_time
                    
                        # Render text
                        self.render_text()

                    # Render GUI and sleep
                    if self.display:
                        self.render()
                        self.pygame.time.wait(self.frame_delay)

                except KeyboardInterrupt:
                    self.quit = True
                finally:
                    if self.quit or self.env.done or self.env.t > 100:
                        break


        if self.env.success == True:
                print("Agent reached the destination.")
        else:
                print("Aborted. Agent did not reach the destination.")

                
        print("\nSimulation ended. . . ")

        # Report final metrics
        if self.display:
            self.pygame.display.quit()  # shut down pygame

    def render_text(self):
        primaryagent=None
        for agent, state in self.env.agent_states.items():
            if agent is self.env.primary_agent:
                    primaryagent=agent
        if self.env.done==True:
                print("Reached Goal!!! in "+repr(self.env.t)+" steps")
        else:
                print("Simulation Running for "+repr(self.env.t)+" steps")
                print("Agent action "+repr(primaryagent.action))
                
    def render(self):
        """ This is the GUI render display of the simulation. 
            Supplementary trial data can be found from render_text. """
        
        # Reset the screen.
        self.screen.fill(self.bg_color)

        # Draw elements
        # * Static elements

        # Boundary
        self.pygame.draw.rect(self.screen, self.boundary, ((self.env.bounds[0] - self.env.hang)*self.env.block_size, (self.env.bounds[1]-self.env.hang)*self.env.block_size, (self.env.bounds[2] +self.env.hang/3)*self.env.block_size, (self.env.bounds[3] - 1 + self.env.hang/3)*self.env.block_size), 4)
        
        for road in self.env.roads:
            # Road
            self.pygame.draw.rect(self.screen, self.road_color, (road[0][0] * self.env.block_size, road[0][1] * self.env.block_size, self.road_width, (road[1][1] -road[0][1])* self.env.block_size),0)
            # Center line
            #self.pygame.draw.line(self.screen, self.road_color, (5 * self.env.block_size, 2 * self.env.block_size), (5 * self.env.block_size, 18 * self.env.block_size), 2)
            self.pygame.draw.line(self.screen, self.line_color, (road[0][0] * self.env.block_size, road[0][1] * self.env.block_size), (road[1][0] * self.env.block_size, road[1][1] * self.env.block_size), 1)
        for road_h in self.env.roads_h:
            self.pygame.draw.line(self.screen, self.line_color, (road_h[0][0] * self.env.block_size, road_h[0][1] * self.env.block_size), (road_h[1][0] * self.env.block_size, road_h[1][1] * self.env.block_size), 1)
        self.font = self.pygame.font.Font(None, 25)
        self.screen.blit(self.font.render("Start", True, self.colors['red'], self.bg_color), (90, (self.env.grid_size[1]+1+self.env.hang/6)*self.env.block_size))
        self.screen.blit(self.font.render("Finish", True, self.colors['red'], self.bg_color), (90, (self.env.bounds[0]+self.env.hang/6)*self.env.block_size))
       
        
            
        # * Dynamic elements
        self.font = self.pygame.font.Font(None, 20)
        primaryagent=None
        for agent, state in self.env.agent_states.items():
            # Compute precise agent location here (back from the intersection some)
            #agent_offset = (2 * state['heading'][0] * self.agent_circle_radius + self.agent_circle_radius * state['heading'][1] * 0.5, \
             #               2 * state['heading'][1] * self.agent_circle_radius - self.agent_circle_radius * state['heading'][0] * 0.5)
            
            agent_offset=(self.env.block_size/2,self.env.block_size/2)
            agent_pos = ((state['location'][0]+self.env.xadd )* self.env.block_size -agent_offset[0], (-state['location'][1] + self.env.ymax-1 + self.env.yadd )* self.env.block_size -agent_offset[1])
            if agent is self.env.primary_agent:
                    primaryagent=agent
                    #print(agent_pos)
                    #if len(self.env.optimalpath) == 0:
                     #   self.pygame.draw.circle(self.screen, self.colors['red'], agent_pos, self.agent_circle_radius+3)
            agent_color = self.colors[agent.color]

            if hasattr(agent, '_sprite') and agent._sprite is not None:
                # Draw agent spritpe (image), properly rotated
                if agent==primaryagent and primaryagent.action == None:
                      tempsprite=self.pygame.transform.smoothscale(self.pygame.image.load(os.path.join("images", "car-{}.png".format('redwhite'))), self.primary_agent_sprite_size)
                      rotated_sprite = self.pygame.transform.rotate(tempsprite, 90)
                      
                else:
                      rotated_sprite = self.pygame.transform.rotate(agent._sprite, 90)
                self.screen.blit(rotated_sprite,
                    self.pygame.rect.Rect(agent_pos[0] - agent._sprite_size[0] / 2, agent_pos[1] - agent._sprite_size[1] / 2,
                        agent._sprite_size[0], agent._sprite_size[1]))
            else:
                # Draw simple agent (circle with a short line segment poking out to indicate heading)
                self.pygame.draw.circle(self.screen, agent_color, agent_pos, self.agent_circle_radius)
                self.pygame.draw.line(self.screen, agent_color, agent_pos, (state['location'][0]+self.env.xadd,state['location'][1]+self.env.yadd), self.road_width)
        self.font = self.pygame.font.Font(None, 30)        





        
        self.screen.blit(self.font.render("Intro to AI", True, self.colors['black'], self.bg_color), (300, 300))
        self.screen.blit(self.font.render("Self-drive-car Simulation", True, self.colors['black'], self.bg_color), (250, 350))
        if self.env.done==True:
                self.screen.blit(self.font.render("Reached Goal!!! in "+repr(self.env.t)+" steps", True, self.colors['black'], self.bg_color), (250, 400))
        else:
                self.screen.blit(self.font.render("Simulation Running for "+repr(self.env.t)+" steps", True, self.colors['black'], self.bg_color), (250, 400))
                self.screen.blit(self.font.render("Agent action "+repr(primaryagent.action), True, self.colors['black'], self.bg_color), (250, 500))
            


        # Flip buffers
        self.pygame.display.flip()

    def pause(self):
        """ When the GUI is enabled, this function will pause the simulation. """
        
        abs_pause_time = time.time()
        self.font = self.pygame.font.Font(None, 30)
        pause_text = "Simulation Paused."
        self.screen.blit(self.font.render(pause_text, True, self.colors['red'], self.bg_color), (350, self.height - 50))
        pause_text = "Press any key to continue. . ."
        self.screen.blit(self.font.render(pause_text, True, self.colors['red'], self.bg_color), (350, self.height - 30))
        self.pygame.display.flip()
        print(pause_text)
        while self.paused:
            for event in self.pygame.event.get():
                if event.type == self.pygame.KEYDOWN:
                    self.paused = False
            self.pygame.time.wait(self.frame_delay)
        self.screen.blit(self.font.render(pause_text, True, self.bg_color, self.bg_color), (400, self.height - 30))
        self.start_time += (time.time() - abs_pause_time)
