package ProtoAlgorithm;

import java.util.HashSet;
import java.util.Set;

public class Main {
    
    public static void main(String[] args) {
        
        // Test graph with all the nodes
        Graph graph = new Graph(1);
        
        // Set to hold all the test nodes
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
        AddNeighbor.addNeighborNodes(node1, node3);
        AddNeighbor.addNeighborNodes(node1, node4);
        AddNeighbor.addNeighborNodes(node1, node6);
        AddNeighbor.addNeighborNodes(node2, node3);
        AddNeighbor.addNeighborNodes(node2, node5);
        AddNeighbor.addNeighborNodes(node2, node7);
        AddNeighbor.addNeighborNodes(node2, node10);
        AddNeighbor.addNeighborNodes(node3, node10);
        AddNeighbor.addNeighborNodes(node4, node5);
        AddNeighbor.addNeighborNodes(node4, node9);
        AddNeighbor.addNeighborNodes(node5, node8);
        AddNeighbor.addNeighborNodes(node5, node9);
        AddNeighbor.addNeighborNodes(node6, node9);
        AddNeighbor.addNeighborNodes(node7, node8);
        AddNeighbor.addNeighborNodes(node7, node10);
        
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
        //graph.printGraph();
        //for (Node node : nodes) {
        //    node.printNode();
        //}
        
        // Set to hold all the test clusters
        Set<Cluster> clusters = new HashSet<Cluster>();
        
        // Create all the clusters
        Cluster cluster1 = new Cluster(1, node1);
        Cluster cluster2 = new Cluster(2, node2);
        Cluster cluster3 = new Cluster(3, node3);
        Cluster cluster4 = new Cluster(4, node4);
        Cluster cluster5 = new Cluster(5, node5);
        Cluster cluster6 = new Cluster(6, node6);
        Cluster cluster7 = new Cluster(7, node7);
        Cluster cluster8 = new Cluster(8, node8);
        Cluster cluster9 = new Cluster(9, node9);
        Cluster cluster10 = new Cluster(10, node10);
        
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
        AddNeighbor.addNeighborClusters(clusters);
        
        for (Cluster cluster : clusters) {
            cluster.printCluster();
        }
    }
}