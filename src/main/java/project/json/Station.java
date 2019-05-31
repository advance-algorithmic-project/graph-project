package project.json;

public class Station {
	private int id;
	private String name;
	private double longitud;
	private double latitud;
	
	public Station() {

	}
	
	public Station(int id, String name, double longitud, double latitud) {
		this.id = id;
		this.name = name;
		this.longitud = longitud;
		this.latitud = latitud;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

}
