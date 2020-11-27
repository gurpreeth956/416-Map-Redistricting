from random import randint
from Node import Node
from Cluster import Cluster
from Graph import Graph
import Rebalance as Rebalance
import json
# ------- read these from a file
RequestedNoOfDistrict = 20
state = "LA"


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

    return graph


def nodeInitialization(graph):
    # checking the state value and loading precinct data from appropriate json file
    if state == "LA":
        with open('PrecinctData/LAAlgoNeighbors.json') as f:
            data = json.load(f)
    elif state == "PA":
        with open('PrecinctData/PAAlgoNeighbors.json') as f:
            data = json.load(f)
    elif state == "CA":
        with open('PrecinctData/CAAlgoNeighbors.json') as f:
            data = json.load(f)
    else:
        print("Choose the correct state")
        exit(0)
    for i in data['features']:
        node = Node(i["unique_id"], population=i["total_pop"])
        graph.nodes.append(node)

    # updating the precinct neighbors
    for i, j in zip(graph.nodes, data['features']):
        neighbors = j["neighbors"]
        for k in graph.nodes:
            if k.id in neighbors:
                i.neighbors.add(k)
                k.neighbors.add(i)

    # updating the internal edges of the graph
    for i in graph.nodes:
        for j in i.neighbors:
            addGraphEdge(graph, i, j)
    population = 0

    # updating the total population of the graph.
    for i in graph.nodes:
        population = population + i.population
    graph.population = population

    print("Graph is: ", graph.id, "population is", graph.population, "and nodes are: ", [i.id for i in graph.nodes])


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

    for i in graph.clusters:
        Rebalance.updateClusterCompactness(graph, i)


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


def writeToJson(graph):
    Districts = list()
    for i in graph.clusters:
        Districts.append({
            'id': i.id,
            'population': i.population,
            'precincts': list(map(lambda j: j.id, i.nodes))
        })

    data = {'Districtings': []}
    data['Districtings'].append({
        'DistrictingId': 1,
        'Overall Compactness': 1,
        'Max Pop Difference': 1,
        'Districts': Districts,
    })

    with open('data.json', 'w') as outfile:
        json.dump(data, outfile)


if __name__ == "__main__":
    ourGraph = seedDistricting()
    print("our graph is ")
    ourGraph.toString()
    Rebalance.rebalance(ourGraph)
    print("Final is: ")
    ourGraph.toString()
    writeToJson(ourGraph)


