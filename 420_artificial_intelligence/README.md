# Assignment 2 CS420 AI

### Questions
- Question 1: Heuristic Search
  - BFS (Queue)
  - DFS (Stack)
  - BFS Optimal (Priority Queue)
  - Best First (Priority Queue)
  - A Star (Priority Queue)
- Question 2: Self Driving Car (A* Search)
  - Drive Function: this function is called on every iteration.
    - It first gets the start and end locations from the environment variable.
    - Then runs the Astar algorithm to get the optimal path between current location
    and the goal
    - Lastly, it converts the path given by the A star algorithm to an action sequence
  - A* Algorithm: Find optimum path, given maze, start, and end
- Question 3: Taxi Driver MDP
  - state, action, new_state, P(s'|s,a), R(s,a,s')
- Question 4: OpenAIGym
  - (1) Initializing Environment
  - (2) Initializing FrozenLake Class
  - (3) Run (Learning)
  - (4) Printing Graph
  - (5) Graph: Rewards
- Question 5: SVD Word Embeddings
  - (1) Load Data
  - (2) Data Preprocessing
  - (3) Co-occurrence matrix Class
  - (4) Create SVD Word Embedding
  - (5) Create Word2Vec Word Embedding
  - (6) EDA of Data Embeddings
