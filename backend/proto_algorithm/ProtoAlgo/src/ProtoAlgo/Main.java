package ProtoAlgo;

import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        // Test graph with all the nodes
        Graph graph = new Graph(1);

        // List to hold all the test nodes
        Set<Node> nodes = new HashSet<Node>();

        // Create all the nodes
        Node node1 = new Node(1, 3);
        Node node2 = new Node(2, 5);
        Node node3 = new Node(3, 6);
        Node node4 = new Node(4, 4);
        Node node5 = new Node(5, 9);
        Node node6 = new Node(6, 4);
        Node node7 = new Node(7, 7);
        Node node8 = new Node(8, 3);
        Node node9 = new Node(9, 2);
        Node node10 = new Node(10, 8);

        // Add each nodes neighbors, you only need to call addNeighborNodes once
        // for each pair of neighbors
        node1.addNeighbor(node3);
        node1.addNeighbor(node4);
        node1.addNeighbor(node6);
        node2.addNeighbor(node3);
        node2.addNeighbor(node5);
        node2.addNeighbor(node7);
        node2.addNeighbor(node10);
        node3.addNeighbor(node1);
        node3.addNeighbor(node2);
        node3.addNeighbor(node10);
        node4.addNeighbor(node1);
        node4.addNeighbor(node5);
        node4.addNeighbor(node9);
        node5.addNeighbor(node2);
        node5.addNeighbor(node4);
        node5.addNeighbor(node8);
        node5.addNeighbor(node9);
        node6.addNeighbor(node1);
        node6.addNeighbor(node9);
        node7.addNeighbor(node2);
        node7.addNeighbor(node8);
        node7.addNeighbor(node10);
        node8.addNeighbor(node5);
        node8.addNeighbor(node7);
        node9.addNeighbor(node4);
        node9.addNeighbor(node5);
        node9.addNeighbor(node6);
        node10.addNeighbor(node2);
        node10.addNeighbor(node3);
        node10.addNeighbor(node7);

        // Add each node to the node set
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);
        nodes.add(node6);
        nodes.add(node7);
        nodes.add(node8);
        nodes.add(node9);
        nodes.add(node10);

        // Add the node set to the graph
        graph.addNodes(nodes);

        // Print the graph and nodes here
        graph.printGraph();
        for (Node node : nodes) {
            node.printNode();
        }

        // Set to hold all the test clusters
        Set<Cluster> clusters = new HashSet<Cluster>();

        // Create all the clusters
        Cluster cluster1 = new Cluster(101, node1);
        Cluster cluster2 = new Cluster(102, node2);
        Cluster cluster3 = new Cluster(103, node3);
        Cluster cluster4 = new Cluster(104, node4);
        Cluster cluster5 = new Cluster(105, node5);
        Cluster cluster6 = new Cluster(106, node6);
        Cluster cluster7 = new Cluster(107, node7);
        Cluster cluster8 = new Cluster(108, node8);
        Cluster cluster9 = new Cluster(109, node9);
        Cluster cluster10 = new Cluster(110, node10);

        // Add each cluster to the cluster set
        clusters.add(cluster1);
        clusters.add(cluster2);
        clusters.add(cluster3);
        clusters.add(cluster4);
        clusters.add(cluster5);
        clusters.add(cluster6);
        clusters.add(cluster7);
        clusters.add(cluster8);
        clusters.add(cluster9);
        clusters.add(cluster10);

        // To add all the initial cluster neighbors, use addNeighborClusters(Set<Cluster> clusters)
        // else use addNeighborClusters(Cluster c1, Cluster c2)
        Algorithm.addNeighborClusters(clusters);

        for (Cluster cluster : clusters) {
            cluster.printCluster();
        }

        Set<Cluster> combinedClusters = Algorithm.combineClusters(clusters, 10);

        for (Cluster cluster : combinedClusters) {
            cluster.printCluster();
        }
    }
}
