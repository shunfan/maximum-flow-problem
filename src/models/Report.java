package models;

import java.util.List;

public class Report {

	public long computationalTime;
	public int numOfAugmentations;
	public double maximumFlowValue;
	public List<Arc> positiveFlowArcs;
	public Cut minimumCut;

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Computational time:\n");
		sb.append(this.computationalTime + "\n");

		sb.append("Num of augmentations:\n");
		sb.append(this.numOfAugmentations + "\n");

		sb.append("Maximum flow value:\n");
		sb.append(this.maximumFlowValue + "\n");

		sb.append("Arcs with positive flow:\n");
		for (Arc arc : this.positiveFlowArcs) {
			sb.append(arc.from + " -> " + arc.to + ": (" + arc.value + " / " + arc.capacity + ")\n");
		}

		sb.append("Minimum cut:\n");
		sb.append("{(");
		for (int node : this.minimumCut.cutA) {
			sb.append(node + ", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append("), (");
		for (int node : this.minimumCut.cutB) {
			sb.append(node + ", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")}\n");

		return sb.toString();
	}

}
