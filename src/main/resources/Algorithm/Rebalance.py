from random import randint
from Node import Node
from Cluster import Cluster
from Graph import Graph
import SeedDistricting as SeedDistricting
import sys
import json

terminationCondition = 3
JobId = sys.argv[1]
populationThreshold = float(sys.argv[4])
idealPopulation = 755501
compactnessThreshold = int(sys.argv[3])
RebalanceCounter = 0


def rebalance(graph):
    counter = 0  # counter for the no of times we are rebalancing
    range = determineIdealPopulationRange()
    print("Range of the population is ", range[0], range[1])
    while counter < terminationCondition:
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
            improved = populationImproved(range, cluster1, cluster2, new1, new2) and compactnessImproved(cluster1, cluster2, new1, new2)
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

    writeToJson(graph)

    return graph

'''
Compactness is ratio of totalEdges of the graph to outgoing edges of the cluster.
So, no outgoing edge means = compactness is perfect
more outgoing edges -> lower compactness
'''
def updateClusterCompactness(graph, cluster):
    outwardEdge = 0
    for i in cluster.nodes:
        for j in i.neighbors:
            if j not in cluster.nodes:
                outwardEdge = outwardEdge + 1

    totalEdge = len(graph.edges)
    cluster.compactness = round((totalEdge-outwardEdge)/totalEdge, 6) * 100


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
        for i in current.neighbors:
            if i not in tree.nodes and i in joint.nodes:
                tree.nodes.add(i)
                done.append(i)
                addInternalEdge(tree, i, current)
        index = index + 1

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


def isPopulationAcceptable(cluster, range):
    if range[0] < cluster.population < range[1]:
        return True
    return False


def populationImproved(range, old1, old2, new1, new2):
    if isPopulationAcceptable(new1, range) and isPopulationAcceptable(new2, range):
        return True
    elif isPopulationAcceptable(old1, range) and isPopulationAcceptable(old2, range):
        return False

    # if both pairs are not acceptable, will choose the better pair of the two
    if (abs(idealPopulation - new1.population) + abs(idealPopulation - new2.population)) <= (abs(idealPopulation - old1.population) + abs(idealPopulation - old2.population)):
        return True
    return False


def compactnessImproved(old1, old2, new1, new2):
    if new1.compactness >= compactnessThreshold and new2.compactness >= compactnessThreshold:
        return True
    elif old1.compactness >= compactnessThreshold and old2.compactness >= compactnessThreshold:
        return False

    # if both the pairs are not acceptable, will choose the better of the pair of the two
    if (compactnessThreshold - new1.compactness) + (compactnessThreshold - new2.compactness) <= (compactnessThreshold - old1.compactness) + (compactnessThreshold - old2.compactness):
        return True
    return False


def determineIdealPopulationRange():
    variation = ((populationThreshold / 2) / 100) * idealPopulation
    return [idealPopulation-variation, idealPopulation+variation]


def writeToJson(graph):
    global RebalanceCounter
    districts = list()
    totalCompactness = 0
    maxPopulation = list(graph.clusters)[0].population
    minPopulation = list(graph.clusters)[0].population
    for i in graph.clusters:
        totalCompactness = totalCompactness + i.compactness
        if i.population > maxPopulation:
            maxPopulation = i.population
        elif i.population < minPopulation:
            minPopulation = i.population
        districts.append({
            'id': i.id,
            'population': i.population,
            'precincts': list(map(lambda j: j.id, i.nodes))
        })
    with open('./src/main/resources/Algorithm/Results/{}.json'.format(JobId)) as f:
        data = json.load(f)
        data['Districtings'].append({
            'DistrictingId': RebalanceCounter,
            'Overall Compactness': totalCompactness/len(graph.clusters),
            'Max Pop Difference': maxPopulation - minPopulation,
            'Districts': districts,
        })
    with open('./src/main/resources/Algorithm/Results/{}.json'.format(JobId), 'w+') as outfile:
        json.dump(data, outfile)
    RebalanceCounter = RebalanceCounter + 1
