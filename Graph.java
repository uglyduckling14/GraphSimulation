import java.util.*;

import static java.lang.Math.min;

public class Graph {
    private final GraphNode[] vertices;  // Adjacency list for graph.
    private final String name;  //The file from which the graph was created.

    private final int[][] residual;
    private final int [][] flow;
    public Graph(String name, int vertexCount) {
        this.name = name;
        flow = new int[vertexCount][vertexCount];
        residual = new int[vertexCount][vertexCount];
        vertices = new GraphNode[vertexCount];
        for (int vertex = 0; vertex < vertexCount; vertex++) {
            vertices[vertex] = new GraphNode(vertex);
        }
    }

    public boolean addEdge(int source, int destination, int capacity) {
        // A little bit of validation
        if (source < 0 || source >= vertices.length) return false;
        if (destination < 0 || destination >= vertices.length) return false;

        // This adds the actual requested edge, along with its capacity
        vertices[source].addEdge(source, destination, capacity);
        // TODO: This is what you have to describe in the required README.TXT file
        //       that you submit as part of this assignment.
        vertices[destination].addEdge(destination, source, 0);
        residual[source][destination]+=capacity;
        return true;
    }

    /**
     * Algorithm to find max-flow in a network
     */
    public int findMaxFlow(int s, int t, boolean report) {
        int totalFlow=0;
        while(hasAugmentingPath(s,t)){
            //you need to go thru all the residuals at index v!!!
            int availableFlow = Integer.MAX_VALUE;
            for(int v = t; v!=s; v = vertices[v].parent){
                int w = vertices[v].parent;
                availableFlow=min(availableFlow,residual[w][v]-flow[w][v]);
            }
            for(int v = t; v!=s; v = vertices[v].parent) {
                int w = vertices[v].parent;
                flow[w][v] += availableFlow;
                flow[v][w] -= availableFlow;
            }
            if(report){
                System.out.println("Flow "+ availableFlow+":");
            }
            totalFlow+=availableFlow;
        }
        return totalFlow;
    }


    /**
     * Algorithm to find an augmenting path in a network
     */
    private boolean hasAugmentingPath(int s, int t) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        for(GraphNode graphNode: vertices){
            graphNode.parent=-1;
            graphNode.visited=false;
        }
        vertices[s].visited=true;
        vertices[s].parent = s;
        while(!queue.isEmpty() && vertices[t].parent==-1){
            int v = queue.remove();
            for(GraphNode.EdgeInfo graphNode: vertices[v].successor){
                int w = graphNode.to;
                if(s!=w&&(residual[v][w]>flow[v][w]) && !vertices[w].visited){
                    queue.add(w);
                    vertices[w].visited=true;
                    vertices[w].parent=v;
                }
            }
        }
        return vertices[t].parent!=-1;
    }

    /**
     * Algorithm to find the min-cut edges in a network
     */
    public void findMinCut(int s) {
        // TODO:
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("The Graph " + name + " \n");
        for (var vertex : vertices) {
            sb.append((vertex.toString()));
        }
        return sb.toString();
    }
}