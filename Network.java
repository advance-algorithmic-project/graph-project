package project.json;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;

public class Network {
	
	private List<Station> stations = new ArrayList<Station>();
	private List<Edge> edges;
	
	public Network(List<File> stationFile, List<File> lineFiles) {
		List<Edge> listEdges = new ArrayList<Edge>();
		FileReader filereader;
		CSVReader csvReader;
		String[] nextRecord;
		
		try {
			System.out.println("Reading stops file...");
			for(File file : stationFile) {
				System.out.println(file);
				filereader = new FileReader(file);
				csvReader = new CSVReader(filereader, ',', '\"', 1);
				while ((nextRecord = csvReader.readNext()) != null) {
					int id = Integer.parseInt(nextRecord[0]);
					String name = nextRecord[2];
					double lat = Double.parseDouble(nextRecord[4]);
					double lon = Double.parseDouble(nextRecord[5]);
					if(!(name.toUpperCase() == name)&&!(joinIdStationOnName(name, id))){
						this.stations.add(new Station(id, name, lat, lon));
					}
				} 
			}
			
			System.out.println("Reading lines file...");
			for (File file : lineFiles) {
				System.out.println(file);
				filereader = new FileReader(file);
				csvReader = new CSVReader(filereader, ',', '\"', 1);
				
				int station_id = 0;
				Long trip_id = 0L, lastTrip_id = 0L;
				Station station = new Station(), lastStation = new Station();
				
				while ((nextRecord = csvReader.readNext()) != null) {
					trip_id = Long.parseLong(nextRecord[0]);
					station_id = Integer.parseInt(nextRecord[3]);

					station = getStation(station_id);
					
					boolean present = false;
					
					if (trip_id.equals(lastTrip_id)) {
						for (int i = 0; i < listEdges.size(); i++) {
							int id_aa = listEdges.get(i).getStation1_id();
							int id_ab = listEdges.get(i).getStation2_id();
							if ((station.containsId(id_aa) && lastStation.containsId(id_ab)) || (station.containsId(id_ab) && lastStation.containsId(id_aa))) {
								present = true;
								break;
							}
						}
						if (present == false) {
							double weight = Math.sqrt(Math.pow(station.getLatitude()-lastStation.getLatitude(), 2) +
														Math.pow(station.getLongitude()-lastStation.getLongitude(), 2));
							listEdges.add(new Edge(lastStation.getId(), station.getId(), weight));
						}
					}
					lastTrip_id = trip_id;
					lastStation = station;
				}
				
			}

		} 
		catch (Exception e) { 
			e.printStackTrace(); 
		} 
		this.edges = listEdges;
	}
	
	public Network(File jsonFile1, File jsonFile2) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			this.stations = objectMapper.readValue(jsonFile1, new TypeReference<List<Station>>(){});
			this.edges = objectMapper.readValue(jsonFile2, new TypeReference<List<Edge>>(){});
			//this.edges = Arrays.asList(objectMapper.readValue(jsonFile2, Edge[].class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private List<Station> deleteDuplicates(List<Station> listStations) {
		List<String> stationNames = new ArrayList<String>();
		for (int i = 0; i < listStations.size(); i++) {
			String name = listStations.get(i).getName();
			if (!stationNames.contains(name)) {
				stationNames.add(name);
			}
		}
		List<Station> tmp = new ArrayList<Station>();
		for (int i = 0; i < stationNames.size(); i++) {
			for (int j = 0; j < listStations.size(); j++) {
				if (listStations.get(j).getName() == stationNames.get(i)) {
					tmp.add(listStations.get(j));
				}
			}
		}
		return tmp;
	}
	
	private boolean joinIdStationOnName(String stationName, int id) {
		for(int i=0; i<this.stations.size() ; i++) {
			if(stationName.equals(this.stations.get(i).getName())) {
				this.stations.get(i).addOtherId(id);
				this.stations.get(i).updateId();
				return true;
			}
		}
		return false;
	}
	
	public void writeToJson() {	
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("edges.json"), this.edges);
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("stations.json"), this.stations);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Station getStation(int stationid) {
		for (int i = 0; i < this.stations.size(); i++) {
			if (this.stations.get(i).containsId(stationid)) {
				return this.stations.get(i);
			}
		}
		System.out.println("/!\\ WARNING: A station has not been found, check if there is any abnomality");
		return null;
	}
	
	public Edge getEdge(int idA, int idB) {
		for(Edge edge : this.edges) {
			if((edge.getStation1_id()==idA && edge.getStation2_id()==idB) ||
					(edge.getStation1_id()==idB && edge.getStation2_id()==idA)) {
				return edge;
			}
		}
		System.out.println("Error in getEdge function: could not find the edge \"" + idA + " / " + idB);
		return null;
	}
	
	public Station getStation(String stationName) {
		for (int i=0; i<this.stations.size(); i++) {
			if(stationName.equals(this.stations.get(i).getName())) {
				return this.stations.get(i);
			}
		}
		return null;
	}
	
	public List<Integer> neighbors (int stationId) {
		List<Integer> neighbors = new ArrayList<Integer>();
		for (Edge edge : this.edges) {
			if (edge.getStation1_id() == stationId) {
				neighbors.add(edge.getStation2_id());
			}
			if (edge.getStation2_id() == stationId) {
				neighbors.add(edge.getStation1_id());
			}
		}
		return neighbors;
	}
	
	public int bfs(int startId, int targetId) {
		LinkedList<Integer> queue = new LinkedList<Integer>();
		List<Pair> distances = new ArrayList<Pair>();
		
		queue.add(startId);
		distances.add(new Pair(startId, 0));
		
		while(!queue.isEmpty()) {
			int currentId = queue.poll();
			int currentDistance = -1;

			for (int i = 0; i < distances.size(); i++) {
				if(distances.get(i).getId() == currentId) {
					currentDistance = distances.get(i).getDistance();
				}
			}
			for (int neighborId : this.neighbors(currentId)) {
				
				if (neighborId == targetId) {
					return currentDistance + 1;
				}
				//Cherche si la station a déjà été visitée
				boolean visited = false;
				for (int i = 0; i < distances.size(); i++) {
					if (distances.get(i).getId() == neighborId) {
						visited = true;
						break;
					}
				}
				if (visited == false) {
					distances.add(new Pair(neighborId, currentDistance + 1));
					queue.add(neighborId);
				}
			}
		}
		return -1;
		
	}
	
	public double djikstra(int start, int dest) {
		HashSet<Integer> unvisitedIds = new HashSet<Integer>();
		for(int i=0; i<this.stations.size(); i++) {
			unvisitedIds.add(this.stations.get(i).getId());
		}
		
		HashMap<Integer, Double> distances = new HashMap<Integer, Double>();
		for(int i=0; i<this.stations.size(); i++) {
			distances.put(this.stations.get(i).getId(), Double.MAX_VALUE);
		}
		distances.put(start, 0.0);
		
		LinkedList<Integer> queue = new LinkedList<Integer>();
		
		queue.add(start);
		
		while(!queue.isEmpty()) {
			int extractedStationId = queue.poll();
			
			if(unvisitedIds.contains(extractedStationId)) {
				unvisitedIds.remove(extractedStationId);
				
				for(int neighborId : this.neighbors(extractedStationId)) {
					Edge edge = getEdge(extractedStationId, neighborId);
					
					if(unvisitedIds.contains(neighborId)) {
						double currentDistance = distances.get(neighborId);
						double newDistance = distances.get(extractedStationId) + edge.getWeight();
						
						if(newDistance < currentDistance) {
							queue.add(neighborId);
							distances.put(neighborId, newDistance);
						}
					}
					if(neighborId==dest) {
						return distances.get(dest);
					}
				}
			}
		}
		return -1;
	}
	
	public int diameter() {
		int diameter = -1;
		for (int i = 0; i < this.stations.size(); i++) {
			for (int j = 0; j < this.stations.size(); j++) {
				if (i != j) {

					int distance = this.bfs(this.stations.get(i).getId(), this.stations.get(j).getId());
					if (distance > diameter) {
						diameter = distance;
					}
				}
			}
		}
		return diameter;
	}

}
