package algorithms;

import java.util.LinkedList;
import java.util.Queue;

import models.Graph;

public class QueuePreflowPushAlgorithm extends AbstractPreflowPushAlgorithm {

	private Queue<Integer> queue;

	public QueuePreflowPushAlgorithm(Graph graph) {
		super(graph);
		this.queue = new LinkedList<>();
	}

	@Override
	protected boolean hasActiveNodes() {
		return !this.queue.isEmpty();
	}

	@Override
	protected boolean containsActiveNode(int node) {
		return this.queue.contains(node);
	}

	@Override
	protected void addActiveNode(int node) {
		this.queue.add(node);
	}

	@Override
	protected int removeActiveNode() {
		return this.queue.poll();
	}

}
