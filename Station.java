package project.json;

import java.util.ArrayList;
import java.util.List;

public class Station {
	private int id;
	private String name;
	private double longitude;
	private double latitude;
	private List<Integer> otherids; // Should always be sorted unless another program alters data position
		
	public Station(int id, String name, double longitude, double latitude) {
		this.id = id;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.otherids = new ArrayList<Integer>();
		addOtherId(id);
	}

	public Station() {
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

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public List<Integer> getOtherids() {
		return otherids;
	}
	
	public void setOtherids(List<Integer> otherids) {
		this.otherids = otherids;
	}
	
	public void addOtherId(int id) {
		this.otherids.add(id);
		this.otherids.sort(null);
	}
	
	public boolean containsId(int id) {
		return this.otherids.contains(id);
	}
	
	public void updateId() {
		this.id = this.otherids.get(0);
	}

}
