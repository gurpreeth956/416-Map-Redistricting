class Node:
    def __init__(self, id, population=0, neighbors=None):
        if neighbors is None:
            neighbors = set()
        self.id = id
        self.population = population
        self.neighbors = neighbors

    def toString(self):
        print("Node is ", self.id, " and neighbors: ", [i.id for i in self.neighbors])
