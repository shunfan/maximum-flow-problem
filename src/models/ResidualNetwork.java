package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResidualNetwork {

	public List<Arc> graphArcs;
	public Map<Integer, List<Arc>> outArcs;
	public Map<Integer, List<Arc>> inArcs;
	public Map<Arc, Arc> reverseArcs;

	public ResidualNetwork(Graph graph) {
		this.graphArcs = new ArrayList<>();
		this.outArcs = new HashMap<>();
		this.inArcs = new HashMap<>();
		this.reverseArcs = new HashMap<>();

		// To store each node pair's first visited arc
		Map<String, Arc> visitedNodePairArc = new HashMap<>();

		for (Arc arc : graph.arcs) {
			Arc arcClone = new Arc(arc.from, arc.to, 0, arc.capacity);

			this.graphArcs.add(arcClone);
			this.addArc(arcClone);

			String nodePair = this.getNodePair(arc.from, arc.to);

			if (visitedNodePairArc.containsKey(nodePair)) {
				// If a node pair have two arcs, put them into reverseArcs
				this.reverseArcs.put(arcClone, visitedNodePairArc.get(nodePair));
				this.reverseArcs.put(visitedNodePairArc.get(nodePair), arcClone);
			} else {
				visitedNodePairArc.put(nodePair, arcClone);
			}
		}

		this.addZeroCapacityArcs();
	}

	public List<Arc> getValidOutArcs(int node) {
		List<Arc> validOutArcs = new ArrayList<>();

		for (Arc outArc : this.outArcs.get(node)) {
			if (outArc.capacity > 0) {
				validOutArcs.add(outArc);
			}
		}

		return validOutArcs;
	}

	public List<Arc> getValidInArcs(int node) {
		List<Arc> validInArcs = new ArrayList<>();

		for (Arc inArc : this.inArcs.get(node)) {
			if (inArc.capacity > 0) {
				validInArcs.add(inArc);
			}
		}

		return validInArcs;
	}

	public Arc getPredArc(int node, int pred) {
		for (Arc inArc : this.outArcs.get(pred)) {
			if (inArc.to == node) {
				return inArc;
			}
		}

		return null;
	}

	private void addArc(Arc arc) {
		if (!this.outArcs.containsKey(arc.from)) {
			this.outArcs.put(arc.from, new ArrayList<>());
		}

		if (!this.inArcs.containsKey(arc.to)) {
			this.inArcs.put(arc.to, new ArrayList<>());
		}

		this.outArcs.get(arc.from).add(arc);
		this.inArcs.get(arc.to).add(arc);
	}

	private String getNodePair(int from, int to) {
		StringBuilder sb = new StringBuilder();

		if (from <= to) {
			sb.append(from);
			sb.append(" ");
			sb.append(to);
		} else {
			sb.append(to);
			sb.append(" ");
			sb.append(from);
		}

		return sb.toString();
	}

	private void addZeroCapacityArcs() {
		for (Arc arc : this.graphArcs) {
			if (!this.reverseArcs.containsKey(arc)) {
				Arc zeroCapcityArc = new Arc(arc.to, arc.from, 0, 0);

				this.addArc(zeroCapcityArc);

				this.reverseArcs.put(arc, zeroCapcityArc);
				this.reverseArcs.put(zeroCapcityArc, arc);
			}
		}
	}

}
