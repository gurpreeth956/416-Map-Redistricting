package ProtoAlgo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Graph {

    int summary, uniqueId;
    Set<Node> nodes;

    // The graph constructor.
    public Graph(int uniqueId) {
        this.summary = 0;
        this.uniqueId = uniqueId;
        this.nodes = new HashSet<Node>();
    }

    // Add node to the graph.
    public void addNode(Node node) {
        this.nodes.add(node);
        updateSummary(node);
    }

    // Add nodes to the graph.
    public void addNodes(Set<Node> nodes) {
        this.nodes.addAll(nodes);
        updateSummary(nodes);
    }

    // Update graph summary data after adding node.
    private void updateSummary(Node node) {
        this.summary += node.getSummary();
    }

    // Update graph summary data after adding nodes.
    private void updateSummary(Set<Node> nodes) {
        for (Node node : nodes) {
            this.summary += node.getSummary();
        }
    }

    // Get this graph's summary data.
    public int getSummary() {
        return this.summary;
    }

    // Get this graph's uniqueId.
    public int getUniqueId() {
        return this.uniqueId;
    }

    // Get this graph's list of nodes.
    public Set<Node> getNodes() {
        return this.nodes;
    }

    // Get the number of nodes in the graph.
    public int getNumberOfNodes() {
        return this.nodes.size();
    }

    // Clean way to print graph's data.
    public void printGraph() {
        System.out.println("This graph is " + this.uniqueId + " and its summary is " + this.summary);
        for (Node node : this.nodes) {
            System.out.println(node.getUniqueId());
        }
        System.out.println();
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
    public void addNeighborClusters(Set<Cluster> clusters) {
        // Clone the clusters set to clonedClusters set.
        HashSet<Cluster> clonedClusters = new HashSet<Cluster>(clusters);

        // Return if cluster sizes not currently size 1.
        for (Cluster c1 : clusters) {
            Set<Node> nodes = c1.getNodes();

            if (nodes.size() != 1) {
                System.out.println("Only call this method when all clusters have a single node, "
                        + "else call the addNeighborClusters(Cluster c1, Cluster c2) method.");
                return;
            }
        }

        // Compare all the clusters with each other.
        /*
        NOTE: Right now there is O(n^2) comparisons where n is the number of
        nodes (since each node is it's own cluster in the beginning. Need to
        make this efficient by using distance of how far two clusters are
        from each other!
        */
        for (Cluster c1 : clusters) {
            // Get the first cluster node.
            Set<Node> nodes1 = c1.getNodes();
            Node node1 = nodes1.iterator().next();

            for (Cluster c2 : clonedClusters) {
                // Get the second cluster node.
                Set<Node> nodes2 = c2.getNodes();
                Node node2 = nodes2.iterator().next();

                /*
                 Add neighbor if the nodes are neighbors and the clusters are
                 two different clusters.
                 */
                if (!c1.equals(c2) && node1.isNeighbor(node2)) {
                    c1.addNeighbor(c2);
                    c2.addNeighbor(c1);
                }
            }

            // Remove c1 cluster from clonedCluster for slightly more efficiency.
            clonedClusters.remove(c1);
        }
    }

    /*
    This method is for combining clusters until there is a total of n clusters
    left where n is equal to the target. It adds clusters together in a new
    cluster set called combinedClusters and returns that.
     */
    static Set<Cluster> combineClusters(Set<Cluster> clusters, int target) {
        // Check if target is too large or small.
        if (target <= 0 || target > clusters.size()) {
            System.out.println("Invalid target argument for combinedClusters().");
            return null;
        }

        // Clone the clusters set to combinedClusters set.
        List<Cluster> combinedClusters = new ArrayList<Cluster>(clusters);
        int randomNum = 0;

        while (combinedClusters.size() > target) {
            // Pick random cluster from clusters set as the child cluster.
            randomNum = ThreadLocalRandom.current().nextInt(0, combinedClusters.size());
            Cluster childCluster = combinedClusters.get(randomNum);

            // Pick random neighbor, the neighbor is the parent cluster.
            List<Cluster> parentClusterNeighbors = new ArrayList<Cluster>(childCluster.getNeighbors());
            randomNum = ThreadLocalRandom.current().nextInt(0, parentClusterNeighbors.size());
            Cluster parentCluster = parentClusterNeighbors.get(randomNum);

            // Combine the two clusters, combineClusters() takes care of neighbors.
            parentCluster.combineClusters(childCluster);

            // Remove child cluster from combinedClusters list.
            combinedClusters.remove(childCluster);
        }

        return new HashSet<Cluster>(combinedClusters);
    }

    // NEED REBALANCING METHOD

    // NEED METHOD FOR REMOVING ALL DISTRICTS

    // NEED METHOD FOR WRITING TO JSON
}