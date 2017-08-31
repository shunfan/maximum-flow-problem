package main;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.Queue;

import algorithms.IAlgorithm;
import models.Graph;
import models.Report;

public class PerformaceTest {

	private static final int numOfRuns = 5;

	public static void main(String[] args) {
		Queue<String> inputs = new LinkedList<>();
		inputs.add("inputs/elist96d.rmf");
		inputs.add("inputs/elist160d.rmf");
		inputs.add("inputs/elist200d.rmf");
		inputs.add("inputs/elist500d.rmf");
		inputs.add("inputs/elist640d.rmf");
		inputs.add("inputs/elist960d.rmf");
		inputs.add("inputs/elist1440d.rmf");

		GraphGenerator generator = GraphGenerator.getInstance();

		while (!inputs.isEmpty()) {
			String input = inputs.poll();
			Graph graph = generator.generate(input);

			System.out.println("Input file: " + input);

			testAlgorithm(graph, "algorithms.QueuePreflowPushAlgorithm");
			testAlgorithm(graph, "algorithms.StackPreflowPushAlgorithm");
			testAlgorithm(graph, "algorithms.MDLCPreflowPushAlgorithm");
			testAlgorithm(graph, "algorithms.ShortestAugmentingPathAlgorithm");

			System.out.println("\n");
		}
	}

	private static void testAlgorithm(Graph graph, String className) {
		double sumOfComputationalTime = 0;

		System.out.println("Algorithm: " + className);

		for (int i = 0; i < numOfRuns; i++) {
			try {
				@SuppressWarnings("unchecked")
				Class<IAlgorithm> clazz = (Class<IAlgorithm>) Class.forName(className);
				Constructor<IAlgorithm> constructor = clazz.getConstructor(Graph.class);
				IAlgorithm algorithm = (IAlgorithm) constructor.newInstance(graph);

				Report report = algorithm.run();
				sumOfComputationalTime += report.computationalTime;

				if (i == 0) {
					System.out.println("Maximum flow value: " + report.maximumFlowValue);
					System.out.println("Number of augmentations: " + report.numOfAugmentations);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out
				.println("Average Computational Time (" + numOfRuns + " runs): " + sumOfComputationalTime / numOfRuns);

		System.out.println("============================================================");
	}

}
