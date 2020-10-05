package ProtoAlgorithm;

import java.util.HashSet;
import java.util.Set;

public class Graph {
    
    int summary, uniqueId;
    Set<Node> nodes;
    
    public Graph(int uniqueId) {
        this.summary = 0;
        this.uniqueId = uniqueId;
        this.nodes = new HashSet<Node>();
    }
    
    public void addNode(Node node) {
        this.nodes.add(node);
        updateSummary(node);
    }
    
    public void addNodes(Set<Node> nodes) {
        this.nodes.addAll(nodes);
        updateSummary(nodes);
    }
    
    private void updateSummary(Node node) {
        this.summary += node.getSummary();
    }
    
    private void updateSummary(Set<Node> nodes) {
        for (Node node : nodes) {
            this.summary += node.getSummary();
        }
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
    
    public void printGraph() {
        System.out.println("This graph is " + this.uniqueId + " and its summary is " + this.summary);
        for (Node node : this.nodes) {
            System.out.println(node.getUniqueId());
        }
        System.out.println();
    }
}