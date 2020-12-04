from random import randint
from Node import Node
from Cluster import Cluster
from Graph import Graph
import Rebalance as Rebalance
import json
import sys
import copy

# the value is updated depending on the districting
RequestedNoOfDistrict = 0
JobId = sys.argv[1]
State = sys.argv[2]
NoOfMaps = int(sys.argv[5])


def seedDistricting():
    graph = Graph(1001)
    nodeInitialization(graph)
    graphInitialization(graph)

    while len(graph.clusters) > RequestedNoOfDistrict:
        index = randint(0, len(graph.clusters)-1)
        cluster1 = graph.clusters[index]
        if len(cluster1.neighbors) > 1:
            index2 = randint(0, len(cluster1.neighbors) - 1)
            cluster2 = list(cluster1.neighbors)[index2]
        else:
            cluster2 = list(cluster1.neighbors)[0]
        combineClusters(graph, cluster1, cluster2)

    # updating the compactness of each cluster before sending for rebalance
    for i in graph.clusters:
        Rebalance.updateClusterCompactness(graph, i)

    return graph


def nodeInitialization(graph):
    # checking the districting value and loading precinct data from appropriate json file
    global RequestedNoOfDistrict

    if State == "LA":
        RequestedNoOfDistrict = 6
        with open('./src/main/resources/Algorithm/PrecinctData/LouisianaNeighbors.json') as f:
            data = json.load(f)
    elif State == "PA":
        RequestedNoOfDistrict = 18
        with open('./src/main/resources/Algorithm/PrecinctData/PennsylvaniaNeighbors.json') as f:
            data = json.load(f)
    elif State == "CA":
        RequestedNoOfDistrict = 53
        with open('./src/main/resources/Algorithm/PrecinctData/CaliforniaNeighbors.json') as f:
            data = json.load(f)
    else:
        print("Choose the correct districting")
        exit(0)

    # adding all the nodes to the graph and updating the graph population
    for i in data:
        node = Node(i["unique_id"], population=i["total_pop"])
        graph.nodes.append(node)
        graph.population = graph.population + node.population

    # updating the precinct neighbors
    for i, j in zip(graph.nodes, data):
        neighbors = j["neighbors"]
        for k in graph.nodes:
            if k.id in neighbors:
                i.neighbors.add(k)
                k.neighbors.add(i)

    # updating the internal edges of the graph
    for i in graph.nodes:
        for j in i.neighbors:
            addGraphEdge(graph, i, j)

    print("Graph is: ", graph.id, "population is", graph.population)


def addGraphEdge(graph, node1, node2):
    if [node1, node2] not in graph.edges and [node2, node1] not in graph.edges:
        graph.edges.append([node1, node2])


def graphInitialization(graph):
    print("graph init entered!")
    # id used to initialize cluster
    clusterId = 100
    # initializing each node as a cluster and adding it to the graph
    for i in graph.nodes:
        j = Cluster(clusterId, nodes={i}, population=i.population)
        graph.clusters.append(j)
        clusterId = clusterId + 1

    # updating the neighbors of each cluster in the graph
    for i in graph.clusters:
        node = list(i.nodes)[0]
        for j in node.neighbors:
            for k in graph.clusters:
                if j.id == list(k.nodes)[0].id:
                    # if it's not itself
                    if i is not k:
                        i.neighbors.add(k)
                        k.neighbors.add(i)


def combineClusters(graph, cluster1, cluster2):

    cluster1.nodes = cluster1.nodes.union(cluster2.nodes)
    cluster1.population = cluster1.population + cluster2.population

    for i in cluster2.neighbors:
        cluster1.neighbors.add(i)
        i.neighbors.add(cluster1)

    if cluster1 in cluster1.neighbors:
        cluster1.neighbors.remove(cluster1)

    for i in graph.clusters:
        if cluster2 in i.neighbors:
            i.neighbors.remove(cluster2)

    graph.clusters.remove(cluster2)


def initializeSummaryJson():
    data = {'Districtings': []}
    with open('./src/main/resources/Algorithm/Results/{}.json'.format(JobId), 'w+') as outfile:
        json.dump(data, outfile)


if __name__ == "__main__":
    ourGraph = seedDistricting()
    print("our graph is ")
    ourGraph.toString()
    initializeSummaryJson()
    for i in range(0, NoOfMaps):
        # need to send a deep copy of our initial graph to rebalance every time
        Rebalance.rebalance(ourGraph)
        print("Final is: ")
        ourGraph.toString()


