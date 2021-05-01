from environment import Environment

class searchUtils():
    env = None
    def __init__(self, environment=None):
        self.env = environment
    
    def retrieveActionSequenceFromState(self,state):
        visitedstatelist = []
        action_sequence=[]
        visitedstatelist.append(state)
        #state.printState()
        prev = state["previous"]
        counter = 0
        while(prev != None):
            visitedstatelist.append(prev)
            if "previous" in prev:
                prev = prev["previous"]
            else:
                prev = None
            counter +=1
        visitedstatelist.reverse()
        for i in range(len(visitedstatelist)-1):
            action_sequence.append(self.env.getAction(visitedstatelist[i],visitedstatelist[i+1]))
            #print(visitedstatelist[i]["location"])
        #print("Size of path is ",counter)
        return action_sequence

    def isPresentStateInList(self,state,searchList):
        for elem in searchList:
            if(state["location"] == elem["location"]):
                return 1

        return 0

    def isPresentStateInPriorityList(self,state,searchList):
        for [elem, dist] in searchList:
            if(state["location"] == elem["location"]):
                return 1

        return 0

    def insertStateInPriorityQueue(self,searchList,state,distanceToGoal):
        index = -1
        for i in range(len(searchList)):
            if(distanceToGoal < searchList[i][1]):
                index = i
                break

        if(len(searchList) == 0 or index == -1):
            searchList.append([state,distanceToGoal])
        else:
            searchList.insert(index, [state,distanceToGoal])
        return searchList

    def checkAndUpdateStateInPriorityQueue(self,searchList,state,distanceToGoal):
        index = -1
        for i in range(len(searchList)):
            if(state["location"] ==(searchList[i][0]["location"])):
                if distanceToGoal < searchList[i][1]:
                    index = i
                break

        if index!=-1:
            searchList.remove([searchList[index][0],searchList[index][1]])
            self.insertStateInPriorityQueue(searchList, state, distanceToGoal)
        return searchList

