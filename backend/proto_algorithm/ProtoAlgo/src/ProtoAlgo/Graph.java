package ProtoAlgo;

import java.util.HashSet;
import java.util.Set;

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
}