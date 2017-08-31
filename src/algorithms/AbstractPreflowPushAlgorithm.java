package algorithms;

import java.util.HashMap;
import java.util.Map;
import models.Arc;
import models.Graph;

public abstract class AbstractPreflowPushAlgorithm extends AbstractAlgorithm {

	private Map<Integer, Double> e;

	public AbstractPreflowPushAlgorithm(Graph graph) {
		super(graph);

		this.e = new HashMap<>();

		// Initialize e
		for (int i = 1; i <= this.graph.numberOfNodes; i++) {
			this.e.put(i, 0D);
		}

		// Initialize e of the source node
		for (Arc outArc : this.r.getValidOutArcs(this.graph.source)) {
			this.e.put(this.graph.source, this.e.get(this.graph.source) + outArc.capacity);
		}
	}

	@Override
	public void execute() {
		this.preprocess();

		while (this.hasActiveNodes()) {
			int node = this.removeActiveNode();
			pushOrRelabelActiveNode(node);
		}
	}

	private void preprocess() {
		this.computeExactDistanceLabels();
		this.pushFlowsFromSource();
		this.d.put(this.graph.source, this.graph.numberOfNodes);
	}

	private void pushOrRelabelActiveNode(int node) {
		Arc admissibleArc = this.getAdmissibleArc(node);

		if (admissibleArc != null) {
			// Push the flow
			this.pushFlow(admissibleArc);

			// Update active nodes
			if (this.e.get(node) != 0) {
				this.addActiveNodeSafely(node);
			}
		} else {
			// Update d
			this.updateDistanceLabel(node);

			// Put it back
			this.addActiveNodeSafely(node);
		}
	}

	private void pushFlowsFromSource() {
		for (Arc outArc : this.r.getValidOutArcs(this.graph.source)) {
			this.pushFlow(outArc);

			// Add the active node since it has a positive e right now
			this.addActiveNodeSafely(outArc.to);
		}
	}

	private void pushFlow(Arc arc) {
		// Find out how much flow needs to be pushed (It should > 0)
		double flowToPush = Math.min(this.e.get(arc.from), arc.capacity);
		this.augmentFlow(arc, flowToPush);

		// Update e
		this.e.put(arc.from, this.e.get(arc.from) - flowToPush);
		this.e.put(arc.to, this.e.get(arc.to) + flowToPush);

		// Update active nodes
		this.addActiveNodeSafely(arc.to);
	}

	private void addActiveNodeSafely(int node) {
		if (!this.containsActiveNode(node) && node != this.graph.source && node != this.graph.sink) {
			this.addActiveNode(node);
		}
	}

	protected abstract boolean hasActiveNodes();

	protected abstract boolean containsActiveNode(int node);

	protected abstract void addActiveNode(int node);

	protected abstract int removeActiveNode();

}
