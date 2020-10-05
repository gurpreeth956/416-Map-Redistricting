package ProtoAlgorithm;

import java.util.HashSet;
import java.util.Set;

public class Node {
    
    int summary, uniqueId;
    Set<Node> neighbors;
    
    public Node(int uniqueNumber, int summary) {
        this.summary = summary;
        this.uniqueId = uniqueNumber;
        this.neighbors = new HashSet<Node>();
    }
    
    public void addNeighbor(Node neighbor) {
        this.neighbors.add(neighbor);
    }
    
    public int getSummary() {
        return this.summary;
    }
    
    public int getUniqueId() {
        return this.uniqueId;
    }
    
    public Set<Node> getNeighbors() {
        return this.neighbors;
    }
    
    public boolean isNeighbor(Node node) {
        return this.neighbors.contains(node);
    }
    
    public void printNode() {
        System.out.println("This node is " + this.uniqueId + " and its summary is " + this.summary);
        System.out.println("Its neihbors are: ");
        for (Node neighbor : this.neighbors) {
            System.out.println(neighbor.getUniqueId());
        }
        System.out.println();
    }
}
