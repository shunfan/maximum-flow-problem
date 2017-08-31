package models;

public class Arc {

	public int from;
	public int to;
	public double value;
	public double capacity;

	public Arc(int from, int to, double value, double capacity) {
		this.from = from;
		this.to = to;
		this.value = value;
		this.capacity = capacity;
	}

}
