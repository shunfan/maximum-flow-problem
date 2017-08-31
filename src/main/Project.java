package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;

import algorithms.IAlgorithm;
import models.Graph;
import models.Report;

public class Project {

	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.out.println("Usage: java -jar project.jar <Algorithm> <Input> <Output>");
		} else {
			Report report = null;
			GraphGenerator generator = GraphGenerator.getInstance();
			Graph graph = generator.generate(args[1]);

			switch (args[0]) {
			case "Queue":
				report = runAlgorithm(graph, "algorithms.QueuePreflowPushAlgorithm");
				break;
			case "Stack":
				report = runAlgorithm(graph, "algorithms.StackPreflowPushAlgorithm");
				break;
			case "MDLC":
				report = runAlgorithm(graph, "algorithms.MDLCPreflowPushAlgorithm");
				break;
			case "SAP":
				report = runAlgorithm(graph, "algorithms.ShortestAugmentingPathAlgorithm");
				break;
			default:
				System.out.println("Alogrithm is not found");
				break;
			}

			try (PrintWriter out = new PrintWriter(args[2])) {
				if (report != null) {
					out.println(report);
					System.out.println("Output file stored at: " + args[2]);
				}
			}
		}
	}

	private static Report runAlgorithm(Graph graph, String className) {
		System.out.println("Algorithm used: " + className);

		try {
			@SuppressWarnings("unchecked")
			Class<IAlgorithm> clazz = (Class<IAlgorithm>) Class.forName(className);
			Constructor<IAlgorithm> constructor = clazz.getConstructor(Graph.class);
			IAlgorithm algorithm = (IAlgorithm) constructor.newInstance(graph);

			return algorithm.run();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
