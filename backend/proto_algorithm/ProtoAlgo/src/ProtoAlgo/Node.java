package ProtoAlgo;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

public class Node {

    int summary, uniqueId;
    Set<Node> neighbors;

    // The node constructor.
    public Node(int uniqueNumber, int summary) {
        this.summary = summary;
        this.uniqueId = uniqueNumber;
        this.neighbors = new HashSet<Node>();
    }

    // Add neighboring node as neighbor.
    public void addNeighbor(Node neighbor) {
        this.neighbors.add(neighbor);
    }

    // Add neighboring nodes as neighbors.
    public void addNeighbors(Set<Node> neighbors) {
        this.neighbors.addAll(neighbors);
    }

    // Get the summary data of the node.
    public int getSummary() {
        return this.summary;
    }

    // Get this node's uniqueId.
    public int getUniqueId() {
        return this.uniqueId;
    }

    // Get the neighbors of the node.
    public Set<Node> getNeighbors() {
        return this.neighbors;
    }

    // Check if uniqueId matches
    public boolean isSameNode(int uniqueId) {
        return this.uniqueId == uniqueId;
    }

    // Check if a node is a neighbor.
    public boolean isNeighbor(Node node) {
        return this.neighbors.contains(node);
    }

    // Clean way to print node's data.
    public void printNode() {
        System.out.println("This node is " + this.uniqueId + " and its summary is " + this.summary);
        System.out.println("Its neighbors are: ");
        for (Node neighbor : this.neighbors) {
            System.out.println(neighbor.getUniqueId());
        }
        System.out.println();
    }

    // Override equals method to be able to use in set.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return uniqueId == node.uniqueId;
    }

    // Override hashCode method to be able to use in set.
    @Override
    public int hashCode() {
        return Objects.hash(uniqueId);
    }
}
