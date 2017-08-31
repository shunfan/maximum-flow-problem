package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Arc;
import models.Graph;

public class ShortestAugmentingPathAlgorithm extends AbstractAlgorithm {

	private Map<Integer, Integer> pred;

	public ShortestAugmentingPathAlgorithm(Graph graph) {
		super(graph);

		this.pred = new HashMap<>();
	}

	@Override
	public void execute() {
		this.computeExactDistanceLabels();

		int nodeToExamine = this.graph.source;

		while (this.d.get(nodeToExamine) < this.graph.numberOfNodes) {
			Arc admissibleArc = this.getAdmissibleArc(nodeToExamine);

			if (admissibleArc != null) {
				nodeToExamine = this.advance(admissibleArc);

				if (nodeToExamine == this.graph.sink) {
					augment();
					nodeToExamine = this.graph.source;
				}
			} else {
				nodeToExamine = retreat(nodeToExamine);
			}
		}
	}

	private int advance(Arc arc) {
		this.pred.put(arc.to, arc.from);
		return arc.to;
	}

	private int retreat(int node) {
		this.updateDistanceLabel(node);

		if (node == this.graph.source) {
			return node;
		}

		return this.pred.get(node);
	}

	private void augment() {
		List<Arc> path = new ArrayList<>();

		int currentNode = this.graph.sink;
		while (currentNode != this.graph.source) {
			Arc arc = this.r.getPredArc(currentNode, this.pred.get(currentNode));
			path.add(arc);
			currentNode = this.pred.get(currentNode);
		}

		double minR = Double.MAX_VALUE;
		for (Arc arc : path) {
			if (arc.capacity < minR) {
				minR = arc.capacity;
			}
		}

		for (Arc arc : path) {
			this.augmentFlow(arc, minR);
		}
	}

}
