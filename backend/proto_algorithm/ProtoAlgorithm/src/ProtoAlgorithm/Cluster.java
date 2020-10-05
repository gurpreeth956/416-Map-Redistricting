package ProtoAlgorithm;

import java.util.HashSet;
import java.util.Set;

public class Cluster {
    
//    static final int IDEAL_BALANCE_MAX = ;
//    static final int IDEAL_BALANCE_MIN = ;
    
    int summary, uniqueId;
    Set<Node> nodes;
    Set<Cluster> neighbors;
//    boolean balanced;

    public Cluster(int uniqueId, Node node) {
        this.summary = node.getSummary();
        this.uniqueId = uniqueId;
        this.nodes = new HashSet<Node>();
        this.nodes.add(node);
        this.neighbors = new HashSet<Cluster>();
    }
    
    public void combineClusters(Cluster cluster) {
        this.nodes.addAll(cluster.getNodes());
        this.neighbors.addAll(cluster.getNeighbors());
        this.summary += cluster.getSummary();
    }
    
    public void addNeighbor(Cluster cluster) {
        this.neighbors.add(cluster);
    }
    
    public int getSummary() {
        return this.summary;
    }
    
    public int getUniqueId() {
        return this.uniqueId;
    }
    
    public Set<Node> getNodes() {
        return this.nodes;
    }
    
    public Set<Cluster> getNeighbors() {
        return this.neighbors;
    }
    
    public boolean nodeInCluster(Node node) {
        return this.nodes.contains(node);
    }
    
    public boolean isNeighbor(Cluster cluster) {
        return this.neighbors.contains(cluster);
    }
    
    public void printCluster() {
        System.out.println("This cluster is " + this.uniqueId + " and its summary is " + this.summary);
        System.out.println("Its neihbors are: ");
        for (Cluster neighbor : this.neighbors) {
            System.out.println(neighbor.getUniqueId());
        }
        System.out.println();
    }
}
