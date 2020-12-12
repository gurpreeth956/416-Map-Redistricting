class Cluster:
    def __init__(self, id, population=0, compactness=0, edges=None, balanceRate=0, nodes=None, neighbors=None, edgeNodes = None):
        if neighbors is None:
            neighbors = set()
        if nodes is None:
            nodes = set()
        if edges is None:
            edges = []
        if edgeNodes is None:
            edgeNodes = set()
        self.id = id
        self.population = population
        self.compactness = compactness
        self.nodes = nodes
        self.edges = edges
        self.balanceRate = balanceRate
        self.neighbors = neighbors
        self.edgeNodes = edgeNodes

    def toString(self):
        print("cluster:", self.id, " population is:", self.population, " compactness:", self.compactness, "and neighbors:", [i.id for i in self.neighbors], " no of nodes:", [i.id for i in self.nodes if len(self.neighbors) == 0])
        # [i.id for i in self.nodes if len(self.neighbors) == 0]
