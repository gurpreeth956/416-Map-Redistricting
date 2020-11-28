from Cluster import Cluster
class Graph:
    def __init__(self, id, population=0, clusters=None, nodes=None, edges=None):
        if clusters is None:
            clusters = []
        if nodes is None:
            nodes = []
        if edges is None:
            edges = []
        self.id = id
        self.population = population
        self.clusters = clusters
        self.nodes = nodes
        self.edges = edges

    def toString(self):
        for i in self.clusters:
            i.toString()
        print(" -----------------------------------------")