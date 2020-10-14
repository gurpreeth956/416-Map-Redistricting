package ProtoAlgo;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

public class Cluster {

    int summary, uniqueId;
    Set<Node> nodes;
    Set<Cluster> neighbors;
//    boolean balanced;

    // The cluster constructor.
    public Cluster(int uniqueId, Node node) {
        this.summary = node.getSummary();
        this.uniqueId = uniqueId;
        this.nodes = new HashSet<Node>();
        this.nodes.add(node);
        this.neighbors = new HashSet<Cluster>();
    }

    // Combines another cluster with current cluster.
    public void combineClusters(Cluster childCluster) {
        // Update this cluster's data.
        this.nodes.addAll(childCluster.getNodes());
        // this.neighbors.addAll(childCluster.getNeighbors());
        this.summary += childCluster.getSummary();

        // If child is a neighbor, remove it.
        if (isNeighbor(childCluster)) {
            this.removeNeighbor(childCluster);
            childCluster.removeNeighbor(this);
        }

        // Update child neighbors.
        updateChildNeighbors(childCluster);
    }

    // Add neighboring cluster as neighbor.
    public void addNeighbor(Cluster neighbor) {
        this.neighbors.add(neighbor);
    }

    // Add neighboring clusters as neighbors.
    public void addNeighbors(Set<Cluster> neighbors) {
        this.neighbors.addAll(neighbors);
    }

    // Remove neighboring cluster.
    public void removeNeighbor(Cluster neighbor) {
        this.neighbors.remove(neighbor);
    }

    // Remove neighboring clusters.
    public void removeNeighbors(Set<Cluster> neighbors) {
        this.neighbors.remove(neighbors);
    }

    // Update child neighbors when combining clusters to this.
    private void updateChildNeighbors(Cluster childCluster) {
        Set<Cluster> childNeighbors = childCluster.getNeighbors();
        Set<Cluster> childNeighborsDuplicate = new HashSet<Cluster>(childNeighbors);

        for (Cluster neighbor : childNeighborsDuplicate) {
            // If not this cluster, then remove child from neighbor and add parent
            if (!this.equals(neighbor)) {
                neighbor.removeNeighbor(childCluster);
                childCluster.removeNeighbor(neighbor);
                this.addNeighbor(neighbor);
                neighbor.addNeighbor(this);
            }
        }
    }

    // Get the cluster's summary data.
    public int getSummary() {
        return this.summary;
    }

    // Get the cluster's uniqueId.
    public int getUniqueId() {
        return this.uniqueId;
    }

    // Get the cluster's set of nodes.
    public Set<Node> getNodes() {
        return this.nodes;
    }

    // Get the cluster's set of neighbors.
    public Set<Cluster> getNeighbors() {
        return this.neighbors;
    }

    // Get the number of nodes in the cluster.
    public int getNumberOfNodes() {
        return this.nodes.size();
    }

    // Check if uniqueId matches
    public boolean isSameCluster(int uniqueId) {
        return this.uniqueId == uniqueId;
    }

    // Check if a node is in the cluster.
    public boolean nodeInCluster(Node node) {
        return this.nodes.contains(node);
    }

    // Check if a cluster is a neighbor.
    public boolean isNeighbor(Cluster cluster) {
        return this.neighbors.contains(cluster);
    }

    // Clean way to print cluster's data.
    public void printCluster() {
        System.out.println("This cluster is " + this.uniqueId + " and its summary is " + this.summary);
        System.out.println("Its nodes are: ");
        for (Node node : this.nodes) {
            System.out.println(node.getUniqueId());
        }
        System.out.println("Its neighbors are: ");
        for (Cluster neighbor : this.neighbors) {
            System.out.println(neighbor.getUniqueId());
        }
        System.out.println();
    }

    // NEED METHOD FOR FINDING BORDER NODES

    // Override equals method to be able to use in set.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster cluster = (Cluster) o;
        return uniqueId == cluster.uniqueId;
    }

    // Override hashCode method to be able to use in set.
    @Override
    public int hashCode() {
        return Objects.hash(uniqueId);
    }
}
