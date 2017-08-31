package algorithms;

import java.util.ArrayList;
import java.util.List;

import models.Graph;

public class MDLCPreflowPushAlgorithm extends AbstractPreflowPushAlgorithm {

	List<Integer> list;

	public MDLCPreflowPushAlgorithm(Graph graph) {
		super(graph);
		this.list = new ArrayList<>();
	}

	@Override
	protected boolean hasActiveNodes() {
		return !this.list.isEmpty();
	}

	@Override
	protected boolean containsActiveNode(int node) {
		return this.list.contains(node);
	}

	@Override
	protected void addActiveNode(int node) {
		this.list.add(node);
	}

	@Override
	protected int removeActiveNode() {
		int maxD = -1;
		Integer activeNodeToRemove = -1;

		for (int activeNode : this.list) {
			if (this.d.get(activeNode) > maxD) {
				maxD = this.d.get(activeNode);
				activeNodeToRemove = activeNode;
			}
		}

		this.list.remove(activeNodeToRemove);

		return activeNodeToRemove;
	}

}
