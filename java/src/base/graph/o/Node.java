package base.graph.o;

import java.util.ArrayList;

public class Node {
    public int val;
    public int in;
    public int out;
    public ArrayList<Node> nexts;
    public ArrayList<Edge> edges;
    public Node(int v) {
        val = v;
        nexts = new ArrayList<>();
        edges = new ArrayList<>();
    }
}
