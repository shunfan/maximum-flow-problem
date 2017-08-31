package models;

import java.util.ArrayList;
import java.util.List;

public class Graph {

	public int numberOfNodes;
	public int numberOfArcs;
	public int source;
	public int sink;
	public List<Arc> arcs;

	public Graph() {
		this.arcs = new ArrayList<>();
	}

}
