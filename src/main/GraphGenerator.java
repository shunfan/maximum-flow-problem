package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import models.Arc;
import models.Graph;

public class GraphGenerator {

	private static GraphGenerator instance;

	private GraphGenerator() {
	}

	public static synchronized GraphGenerator getInstance() {
		if (instance == null) {
			instance = new GraphGenerator();
		}
		return instance;
	}

	public Graph generate(String dataFilePath) {
		Graph graph = new Graph();

		try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath))) {
			String line;
			int lineNumber = 1;

			while ((line = reader.readLine()) != null) {
				switch (lineNumber) {
				case 1:
					graph.numberOfNodes = Integer.parseInt(line);
					break;
				case 2:
					graph.numberOfArcs = Integer.parseInt(line);
					break;
				case 3:
					graph.source = Integer.parseInt(line);
					break;
				case 4:
					graph.sink = Integer.parseInt(line);
					break;
				default:
					String[] chunks = line.split("\\s+");

					int from = Integer.parseInt(chunks[0]);
					int to = Integer.parseInt(chunks[1]);
					double capacity = Double.valueOf(chunks[2]);

					Arc arc = new Arc(from, to, 0, capacity);
					graph.arcs.add(arc);

					break;
				}

				lineNumber++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return graph;
	}

}
