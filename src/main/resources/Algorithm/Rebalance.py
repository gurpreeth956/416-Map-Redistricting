from random import randint
from Cluster import Cluster
import sys
import json

terminationCondition = 10
populationThreshold = float(sys.argv[4])

# gets updated first thing in the rebalance method.
idealPopulation = 0
compactnessThreshold = int(sys.argv[3])

# Map Id or rebalance Id
RebalanceId = 0
JobId = sys.argv[1]


def rebalance(graph):
    counter = 0  # counter for the no of times we are rebalancing
    range = determineIdealPopulationRange(graph)
    compactnessRange = determineCompactnessRange()
    print("Range of the population is ", range[0], range[1])

    while counter < terminationCondition:

        # Randomly picking a cluster and its neighbor
        index = randint(0, len(graph.clusters) - 1)
        cluster1 = graph.clusters[index]
        if len(cluster1.neighbors) > 1:
            index2 = randint(0, len(cluster1.neighbors) - 1)
            cluster2 = list(cluster1.neighbors)[index2]
        else:
            cluster2 = list(cluster1.neighbors)[0]

        # Creating a spanning tree with the two clusters
        tree = createSpanningTree(graph, cluster1, cluster2)
        print('Returned from spanning tree, and the tree is: ')
        tree.toString()

        counter2 = 0  # counter for the following loop
        # going through all the edges in the spanning tree till we find a feasible one
        while counter2 < len(tree.edges):
            [new1, new2] = cutRandomEdge(graph, tree)

            # if population has improved
            if populationImproved(range, cluster1, cluster2, new1, new2):
                # update the compactness and check only if it passes population test first
                updateClusterCompactness(new1)
                updateClusterCompactness(new2)

                # updating the graph if both population and compactness improved
                if compactnessImproved(compactnessRange, cluster1, cluster2, new1, new2):
                    updateClusterNeighborFromTree(tree, new1)
                    updateClusterNeighborFromTree(tree, new2)

                    # adding both the new clusters to the graph
                    graph.clusters.append(new1)
                    graph.clusters.append(new2)

                    # removing both the cluster to the graph
                    removeClusterFromGraph(graph, cluster1)
                    removeClusterFromGraph(graph, cluster2)
                    print('Tree is ')
                    tree.toString()
                    graph.toString()
                    break
                else:
                    print("Compactness not improved")
            else:
                print("not improved!")
            counter2 = counter2 + 1
        counter = counter + 1

    # writing the map to Json
    writeToJson(graph)

    return graph


def updateClusterCompactness(cluster):
    cluster.edgeNodes = set()
    for i in cluster.nodes:
        for j in i.neighbors:
            if j not in cluster.nodes:
                cluster.edgeNodes.add(i)

    cluster.compactness = round((len(cluster.edgeNodes)/len(cluster.nodes)), 2) * 100     # -------- new ones


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
    # creating the spanning tree - Breadth First Search
    while index < len(done):
        current = done[index]
        tree.nodes.add(current)
        for i in current.neighbors:
            if i not in tree.nodes and i in joint.nodes:
                tree.nodes.add(i)
                done.append(i)
                addInternalEdge(tree, i, current)
        index = index + 1

    # updating the neighbor list and population of the spanning tree
    for i in cluster1.neighbors:
        tree.neighbors.add(i)
    for i in cluster2.neighbors:
        tree.neighbors.add(i)
    tree.neighbors.remove(cluster1)
    tree.neighbors.remove(cluster2)
    tree.population = cluster1.population + cluster2.population
    
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
    cluster1 = Cluster(randint(30000, 40000))
    cluster2 = Cluster(randint(40000, 50000))

    # picking a random edge to cut
    index = randint(0, len(cluster.edges) - 1)
    edge = cluster.edges[index]
    print("cutting edges: ", edge[0].id, edge[1].id)

    edges = list()
    for i in cluster.edges:
        edges.append(i)
    edges.remove(edge)

    # Breadth first search for Subgraph 1
    done1 = [edge[0]]
    index1 = 0
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
                done1.append(i)
                edges.remove([current, i])
        index1 = index1 + 1

    # adding rest of the nodes from the tree in cluster 2
    for i in cluster.nodes:
        if i not in cluster1.nodes:
            cluster2.nodes.add(i)

    cluster1.neighbors.add(cluster2)
    cluster2.neighbors.add(cluster1)

    updateClusterPopulation(cluster1)
    cluster2.population = cluster.population - cluster1.population

    return [cluster1, cluster2]


def updateClusterNeighborFromTree(tree, cluster):
    # iterates through edge nodes to find cluster neighbors
    for i in cluster.nodes:
        for j in i.neighbors:
            if j not in cluster.nodes:
                for k in tree.neighbors:
                    if j in k.nodes:
                        cluster.neighbors.add(k)
                        k.neighbors.add(cluster)
                        break


def isPopulationAcceptable(cluster, range):
    if range[0] <= cluster.population <= range[1]:
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


def compactnessImproved(compactnessRange, old1, old2, new1, new2):
    if compactnessRange[0] <= new1.compactness <= compactnessRange[1] and compactnessRange[0] <= new2.compactness <= compactnessRange[1]:
        return True
    elif compactnessRange[0] <= old1.compactness <= compactnessRange[1] and compactnessRange[0] <= old2.compactness <= compactnessRange[1]:
        return False

    idealCompactness = int((compactnessRange[0] + compactnessRange[1])/2)
    # if both the pairs are not acceptable, will choose the better of the pair of the two
    if (abs(idealCompactness - new1.compactness) + abs(idealCompactness - new2.compactness)) <= (abs(idealCompactness - old1.compactness) + abs(idealCompactness - old2.compactness)):
        return True
    return False


def determineIdealPopulationRange(graph):
    global idealPopulation
    idealPopulation = graph.population / len(graph.clusters)
    variation = ((populationThreshold / 2) / 100) * idealPopulation
    return [idealPopulation-variation, idealPopulation+variation]


def determineCompactnessRange():
    global compactnessThreshold
    if compactnessThreshold == 0:
        return [0, 30]
    elif compactnessThreshold == 1:
        return [31, 60]
    else:
        return [61, 100]

def writeToJson(graph):
    global RebalanceId

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
            'DistrictingId': RebalanceId,
            'Overall Compactness': totalCompactness/len(graph.clusters),
            'Max Pop Difference': maxPopulation - minPopulation,
            'Districts': districts,
        })
    with open('./src/main/resources/Algorithm/Results/{}.json'.format(JobId), 'w+') as outfile:
        json.dump(data, outfile)

    # updating map id for the next Rebalance.
    RebalanceId = RebalanceId + 1