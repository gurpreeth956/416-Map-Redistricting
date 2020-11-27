from random import randint
from Node import Node
from Cluster import Cluster
from Graph import Graph
import SeedDistricting as SeedDistricting

terminationCondition = 2
populationThreshold = 0.03
idealPopulation = 755501


# compactness is ration of totalEdges to outgoing edges
def updateClusterCompactness(graph, cluster):
    outwardEdge = 0
    for i in cluster.nodes:
        for j in i.neighbors:
            if j not in cluster.nodes:
                outwardEdge = outwardEdge + 1

    if outwardEdge == 0:
        cluster.compactness = 100       # --------- perfect compactness
    else:
        totalEdge = len(graph.edges)
        cluster.compactness = round(totalEdge/outwardEdge, 3)


def updateClusterPopulation(cluster):
    population = 0
    for i in cluster.nodes:
        population = population + i.population
    cluster.population = population


def removeClusterFromGraph(graph, cluster):
    print("removing ", cluster.id)

    for i in graph.clusters:
        if cluster in i.neighbors:
            i.neighbors.remove(cluster)

    graph.clusters.remove(cluster)


def addClusterToGraph(graph, cluster):
    for i in cluster.neighbors:
        i.neighbors.add(cluster)

    graph.clusters.append(cluster)


def createSpanningTree(graph, cluster1, cluster2):
    print('Create spanning tree entered with ', cluster1.id, ' and ', cluster2.id)
    id = cluster1.id + cluster2.id
    joint = Cluster(id)
    for i in cluster1.nodes:
        joint.nodes.add(i)
    for i in cluster2.nodes:
        joint.nodes.add(i)
    tree = Cluster(randint(2000, 20000))   # ------- don't forget id

    # randomly picking a starting root for the spanning tree
    randIndex = randint(0, len(joint.nodes)-1)
    done = [list(joint.nodes)[randIndex]]

    index = 0
    # creating the spanning tree
    while index < len(done):
        current = done[index]
        tree.nodes.add(current)
        #joint.nodes.remove(current)
        for i in current.neighbors:
            if i not in tree.nodes and i in joint.nodes:
                tree.nodes.add(i)
                #joint.nodes.remove(i)
                done.append(i)
                addInternalEdge(tree, i, current)
        index = index + 1

    # not necessary --------------
    # don't call update internal edges
    updateClusterPopulation(tree)
    updateClusterCompactness(graph, tree)

    for i in cluster1.neighbors:
        tree.neighbors.add(i)
    for i in cluster2.neighbors:
        tree.neighbors.add(i)

    tree.neighbors.remove(cluster1)
    tree.neighbors.remove(cluster2)
    return tree


def addInternalEdge(cluster, node1, node2):
    if [node1, node2] not in cluster.edges and [node2, node1] not in cluster.edges:
        cluster.edges.append([node1, node2])


def updateInternalEdge(cluster):
    for i in cluster.nodes:
        for j in i.neighbors:
            if j in cluster.nodes:
                addInternalEdge(cluster, i, j)


def cutRandomEdge(graph, cluster):
    print("cut random edge entered with", cluster.id)
    cluster1 = Cluster(randint(30000, 40000))
    cluster2 = Cluster(randint(40000, 50000))
    index = randint(0, len(cluster.edges) - 1)
    edge = cluster.edges[index]
    print("cutting edges: ", edge[0].id, edge[1].id)
    #cluster.edges.remove(edge)
    done1 = [edge[0]]
    index1 = 0
    edges = list()
    for i in cluster.edges:
        edges.append(i)
    edges.remove(edge)

    while index1 < len(done1):
        current = done1[index1]
        cluster1.nodes.add(current)
        for i in current.neighbors:
            if [i, current] in edges:
                cluster1.nodes.add(i)
                done1.append(i)
                edges.remove([i, current])
            if [current, i] in edges:
                cluster1.nodes.add(i)
                done1.done(i)
                edges.remove([current, i])
        index1 = index1 + 1

    done2 = [edge[1]]
    index2 = 0
    while index2 < len(done2):
        current = done2[index2]
        cluster2.nodes.add(current)
        for i in current.neighbors:
            if [i, current] in edges:
                cluster2.nodes.add(i)
                done2.append(i)
                edges.remove([i, current])
            if [current, i] in edges:
                cluster2.nodes.add(i)
                done2.append(i)
                edges.remove([current, i])
        index2 = index2 + 1

    '''for i in cluster1.nodes:
        for j in i.neighbors:
            if j not in cluster1.nodes:
                for k in cluster.neighbors:
                    if j in k.nodes:
                        cluster1.neighbors.add(k)
                        break

    for i in cluster2.nodes:
        for j in i.neighbors:
            if j not in cluster2.nodes:
                for k in cluster.neighbors:
                    if j in k.nodes:
                        cluster2.neighbors.add(k)
                        break'''

    cluster1.neighbors.add(cluster2)
    cluster2.neighbors.add(cluster1)

    updateClusterPopulation(cluster1)
    updateClusterPopulation(cluster2)
    updateClusterCompactness(graph, cluster1)
    updateClusterCompactness(graph, cluster2)

    # updateInternalEdge(cluster1)
    # updateInternalEdge(cluster2)

    return [cluster1, cluster2]


def updateClusterNeighborFromTree(tree, cluster):
    for i in cluster.nodes:
        for j in i.neighbors:
            if j not in cluster.nodes:
                for k in tree.neighbors:
                    if j in k.nodes:
                        cluster.neighbors.add(k)
                        break

def rebalance(graph):
    counter = 0  # counter for the no of times we are rebalancing
    while counter <= terminationCondition:
        index = randint(0, len(graph.clusters) - 1)
        cluster1 = graph.clusters[index]
        if len(cluster1.neighbors) > 1:
            index2 = randint(0, len(cluster1.neighbors) - 1)
            cluster2 = list(cluster1.neighbors)[index2]
        else:
            cluster2 = list(cluster1.neighbors)[0]

        tree = createSpanningTree(graph, cluster1, cluster2)
        print('Returned from spanning tree, and the tree is: ')
        tree.toString()
        counter2 = 0  # no of times we are trying to find a feasible edge

        # might change this to go through all edge and pick a random one.
        while counter2 < len(tree.edges):
            [new1, new2] = cutRandomEdge(graph, tree)
            improved = hasImproved(cluster1, cluster2, new1, new2)
            if improved:
                print("Improved!")
                updateClusterNeighborFromTree(tree, new1)
                updateClusterNeighborFromTree(tree, new2)
                addClusterToGraph(graph, new1)
                addClusterToGraph(graph, new2)
                removeClusterFromGraph(graph, cluster1)
                removeClusterFromGraph(graph, cluster2)

                graph.toString()
                break
            else:
                print("not improved!")
            counter2 = counter2 + 1
        counter = counter + 1

    return graph


def hasImproved(old1, old2, new1, new2):
    if (max(new1.compactness, new2.compactness) - max(old1.compactness, old2.compactness)) >= (min(new1.compactness, new2.compactness) - min(old1.compactness, old2.compactness)):
        if (abs(idealPopulation - new1.population) + abs(idealPopulation - new2.population)) <= (abs(idealPopulation - old1.population) + abs(idealPopulation - old2.population)):
            return True
    return False

