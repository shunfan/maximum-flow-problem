package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import models.Arc;
import models.Cut;
import models.Graph;
import models.Report;
import models.ResidualNetwork;

public abstract class AbstractAlgorithm implements IAlgorithm {

	protected Graph graph;
	protected ResidualNetwork r;
	protected Map<Integer, Integer> d;
	private long computationalTime;
	private int numOfAugmentations;

	public AbstractAlgorithm(Graph graph) {
		this.graph = graph;
		this.r = new ResidualNetwork(graph);
		this.d = new HashMap<>();

		// Initialize d
		for (int i = 1; i <= this.graph.numberOfNodes; i++) {
			this.d.put(i, Integer.MAX_VALUE);
		}
		this.d.put(this.graph.sink, 0);
	}

	public Report run() {
		long startTime = System.currentTimeMillis();
		this.execute();
		long endTime = System.currentTimeMillis();

		this.computationalTime = endTime - startTime;

		return this.generateReport();
	}

	/**
	 * Compute the exact distance labels
	 */
	protected void computeExactDistanceLabels() {
		Queue<Integer> queue = new LinkedList<>();
		Set<Integer> visited = new HashSet<>();

		for (Arc inArc : this.r.getValidInArcs(graph.sink)) {
			this.d.put(inArc.from, 1);
			queue.add(inArc.from);
		}

		while (!queue.isEmpty()) {
			int node = queue.poll();

			// Check if the node is examined for the first time
			if (visited.add(node)) {
				// Check if the node is not the source
				if (node != this.graph.source) {
					for (Arc inArc : this.r.getValidInArcs(node)) {
						// Check if the node does not have a distance label or
						// its distance label is invalid
						if (!this.d.containsKey(inArc.from) || this.d.get(inArc.from) > this.d.get(inArc.to) + 1) {
							// Update the distance label
							this.d.put(inArc.from, this.d.get(inArc.to) + 1);
						}

						queue.add(inArc.from);
					}
				}
			}
		}
	}

	/**
	 * Get one of the node's admissible arcs
	 * 
	 * @param node
	 * @return the admissible arc or null if not found
	 */
	protected Arc getAdmissibleArc(int node) {
		for (Arc outArc : this.r.getValidOutArcs(node)) {
			if (this.d.get(outArc.from) == this.d.get(outArc.to) + 1) {
				// An admissible arc is found
				return outArc;
			}
		}

		return null;
	}

	/**
	 * Update the node's distance label
	 * 
	 * @param node
	 */
	protected void updateDistanceLabel(int node) {
		int minD = Integer.MAX_VALUE;

		for (Arc outArc : this.r.getValidOutArcs(node)) {
			int dCandidate = this.d.get(outArc.to) + 1;

			if (dCandidate < minD) {
				minD = dCandidate;
			}
		}

		this.d.put(node, minD);
	}

	/**
	 * Augment a flow into the arc and update related arcs' capacity
	 * 
	 * @param arc
	 * @param value
	 */
	protected void augmentFlow(Arc arc, double value) {
		// Update arcs in the residual network
		arc.value += value;
		arc.capacity = arc.capacity - value;
		Arc reverseArc = this.r.reverseArcs.get(arc);
		reverseArc.capacity = reverseArc.capacity + value;

		// Increment the number of augmentations
		this.numOfAugmentations += 1;
	}

	/**
	 * Generate the report
	 * 
	 * @return
	 */
	protected Report generateReport() {
		// Create a report
		Report report = new Report();

		// Initialize the maximum flow value
		double maximumFlowValue = 0;

		// Add arcs with positive flow into report
		List<Arc> positiveFlowArcs = new ArrayList<>();

		for (Arc arc : this.r.graphArcs) {
			if (arc.value > 0) {
				Arc reverseArc = this.r.reverseArcs.get(arc);

				Arc arcWithPositiveFlow = null;
				if (this.r.graphArcs.contains(reverseArc)) {
					// If the arc has an reverse arc in the original graph
					arcWithPositiveFlow = new Arc(arc.from, arc.to, arc.value, arc.value + arc.capacity);
				} else {
					if (reverseArc.value != 0) {
						// System.out.println("\nFuck ============\n");
					}
					arcWithPositiveFlow = new Arc(arc.from, arc.to, arc.value - reverseArc.value,
							arc.capacity + reverseArc.capacity);
				}

				positiveFlowArcs.add(arcWithPositiveFlow);

				// Add the flow value into the maximum flow value
				if (arc.to == this.graph.sink) {
					maximumFlowValue += arcWithPositiveFlow.value;
				}
			}
		}

		// Add the minimum cut into the report
		Cut minimumCut = new Cut();
		Queue<Integer> queue = new LinkedList<>();
		Set<Integer> cutA = new HashSet<>();
		Set<Integer> cutB = new HashSet<>();

		queue.add(this.graph.source);

		// Find cutA
		while (!queue.isEmpty()) {
			int node = queue.poll();

			if (cutA.add(node)) {
				for (Arc outArc : this.r.getValidOutArcs(node)) {
					queue.add(outArc.to);
				}
			}
		}

		// Find cutB
		for (int i = 1; i <= this.graph.numberOfNodes; i++) {
			if (!cutA.contains(i)) {
				cutB.add(i);
			}
		}

		minimumCut.cutA = cutA;
		minimumCut.cutB = cutB;

		report.computationalTime = this.computationalTime;
		report.numOfAugmentations = this.numOfAugmentations;
		report.maximumFlowValue = maximumFlowValue;
		report.positiveFlowArcs = positiveFlowArcs;
		report.minimumCut = minimumCut;

		return report;
	}

	protected abstract void execute();

}
