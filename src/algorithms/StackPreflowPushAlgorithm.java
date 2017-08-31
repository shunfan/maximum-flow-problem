package algorithms;

import java.util.Stack;

import models.Graph;

public class StackPreflowPushAlgorithm extends AbstractPreflowPushAlgorithm {

	Stack<Integer> stack;

	public StackPreflowPushAlgorithm(Graph graph) {
		super(graph);
		this.stack = new Stack<>();
	}

	@Override
	protected boolean hasActiveNodes() {
		return !this.stack.isEmpty();
	}

	@Override
	protected boolean containsActiveNode(int node) {
		return this.stack.contains(node);
	}

	@Override
	protected void addActiveNode(int node) {
		this.stack.push(node);
	}

	@Override
	protected int removeActiveNode() {
		return this.stack.pop();
	}

}
