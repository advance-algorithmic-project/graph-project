package project.json;

public class Edge {
	
	private int station1_id;
	private int station2_id;
	private double weight;
	
	// TODO: Remove later
	public Edge(int station1_id, int station2_id) {
		this.station1_id = station1_id;
		this.station2_id = station2_id;
	}
	
	public Edge(int station1_id, int station2_id, double weight) {
		this.station1_id = station1_id;
		this.station2_id = station2_id;
		this.setWeight(weight);
	}

	public int getStation1_id() {
		return station1_id;
	}

	public void setStation1_id(int station1_id) {
		this.station1_id = station1_id;
	}

	public int getStation2_id() {
		return station2_id;
	}

	public void setStation2_id(int station2_id) {
		this.station2_id = station2_id;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
}
