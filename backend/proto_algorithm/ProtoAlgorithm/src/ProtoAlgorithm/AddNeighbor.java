package ProtoAlgorithm;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class AddNeighbor {

    /*
    This method is to add two existing nodes as each other's neighbor.
    */
    static void addNeighborNodes(Node n1, Node n2) {
        n1.addNeighbor(n2);
        n2.addNeighbor(n1);
    }
    
    /*
    This method is to add the neighbors of all the newly created clusters of 
    size 1. That means each cluster in the cluster set only has 1 node in it. 
    This method should be called after adding each node to its own cluster, 
    since that is the first part of the algorithm. This separate method had to 
    be created since we are creating new clusters and neighboring nodes may not 
    have had their respective cluster created yet so they can not be added right 
    away as neighboring clusters.
    */
    static void addNeighborClusters(Set<Cluster> clusters) {
        // Clone clusters set
        Set<Cluster> clonedClusters = new HashSet<Cluster>();
        clonedClusters.addAll(clusters);
        
        // Return if cluster sizes not currently size 1
        for (Cluster c1 : clusters) {
            Set<Node> nodes = c1.getNodes();
            
            if (nodes.size() != 1) {
                System.out.println("Only call this method when all clusters have a single node, "
                        + "else call the addNeighborClusters(Cluster c1, Cluster c2) method.");
                return;
            }
        }
        
        // Compare all the clusters with each other
        /*
        NOTE: Right now there is O(n^2) comparisons where n is the number of
        nodes (since each node is it's own cluster in the beginning. Need to 
        make this efficient by using distance of how far two clusters are 
        from each other!
        */
        for (Cluster c1 : clusters) {
            // Get first cluster node
            Set<Node> nodes1 = c1.getNodes();
            Node node1 = nodes1.iterator().next();
            
            for (Cluster c2 : clonedClusters) {
                // Get second cluster node
                Set<Node> nodes2 = c2.getNodes();
                Node node2 = nodes2.iterator().next();
                
                // Add neighbor if the nodes are neighbors and the clusters are
                // two different clusters
                if (c1.getUniqueId() != c2.getUniqueId() && node1.isNeighbor(node2)) {
                    addNeighborClusters(c1, c2);
                }
            }
            
            // Remove c1 cluster from clonedCluster for slightly more efficiency
            clonedClusters.remove(c1);
        }
    }
    
    /* 
    This method is to add two existing clusters as each other's neighbor.
    */
    static void addNeighborClusters(Cluster c1, Cluster c2) {
        c1.addNeighbor(c2);
        c2.addNeighbor(c1);
    }
}
