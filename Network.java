package project.json;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
				String line = String.valueOf(file).replaceAll("_","");
				line = line.replaceAll("RATPGTFS", "");
				line = line.split("\\\\")[1];
				filereader = new FileReader(file);
				csvReader = new CSVReader(filereader, ',', '\"', 1);
				while ((nextRecord = csvReader.readNext()) != null) {
					int id = Integer.parseInt(nextRecord[0]);
					String name = nextRecord[2] + " [" + line + "]";
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
				int time;
				Station station = new Station(), lastStation = new Station();
				
				while ((nextRecord = csvReader.readNext()) != null) {
					trip_id = Long.parseLong(nextRecord[0]);
					time = Integer.parseInt(nextRecord[1].substring(0, 2));
					station_id = Integer.parseInt(nextRecord[3]);
					
					if(time>=8 && time<=22 && station_id!=8433) {
						station = getStation(station_id);
						
						boolean present = false;
						
						if (trip_id.equals(lastTrip_id)) {
							for (int i = 0; i < listEdges.size(); i++) {
								Edge edge = listEdges.get(i);
								if ((edge.getStation1_id()==lastStation.getId() && edge.getStation2_id()==station.getId()) ||
										(edge.getStation1_id()==station.getId() && edge.getStation2_id()==lastStation.getId())) {
									present = true;
									break;
								}
							}
							double weight = Math.sqrt(Math.pow(station.getLatitude()-lastStation.getLatitude(), 2) +
									Math.pow(station.getLongitude()-lastStation.getLongitude(), 2));
							if(!present) {
								if(Arrays.asList(1818, 1817, 2346, 2004, 2193,
										2256, 2354, 1903, 2299, 2026).contains(station_id)) {
									listEdges.add(new Edge(lastStation.getId(), station.getId(), weight));
								}
								else {
									listEdges.add(new Edge(lastStation.getId(), station.getId(), weight));
									listEdges.add(new Edge(station.getId(), lastStation.getId(), weight));
								}
							}
						}
						lastTrip_id = trip_id;
						lastStation = station;
					}
				}
			}
			// Transition de lignes (même station)
			System.out.println("Adding transit...");
			for(Station s : this.stations) {
				String name = s.getName().replaceAll("\\[\\w*\\]", "");
				for(Station same : fetchSameStation(name)) {
					double weight = Math.sqrt(Math.pow(s.getLatitude()-same.getLatitude(), 2) +
							Math.pow(s.getLongitude()-same.getLongitude(), 2));
					if(weight!=0) {
						Edge edge = new Edge(s.getId(), same.getId(), weight);
						if(!listEdges.contains(edge)) {
							listEdges.add(edge);
						}
					}
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
	
	private List<Station> fetchSameStation(String name) {
		List<Station> res = new ArrayList<Station>();
		for(Station s : this.stations) {
			if(s.getName().replaceAll("\\[\\w*\\]", "").equals(name)) {
				res.add(s);
			}
		}
		return res;
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
		}
		return neighbors;
	}
	
	public ArrayList<Integer> bfs(int startId, int targetId) {
		LinkedList<Integer> queue = new LinkedList<Integer>();
		List<ArrayList<Integer>> distances = new ArrayList<ArrayList<Integer>>();
		
		queue.add(startId);
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		tmp.add(startId);
		distances.add(tmp);
		while(!queue.isEmpty()) {
			int currentId = queue.poll();
			ArrayList<Integer> currentPath = new ArrayList<Integer>();
			for (ArrayList<Integer> path : distances) {
				if(path.get(path.size() - 1) == currentId) {
					currentPath = new ArrayList<Integer>(path);
				}
			}
			for (int neighborId : this.neighbors(currentId)) {
				
				if (neighborId == targetId) {
					currentPath.add(neighborId);
					return currentPath;
				}
				//Cherche si la station a déjà été visitée
				boolean visited = false;
				for (ArrayList<Integer> path : distances) {
					if (path.get(path.size() - 1) == neighborId) {
						visited = true;
						break;
					}
				}
				if (visited == false) {
					ArrayList<Integer> newPath = new ArrayList<Integer>(currentPath);
					newPath.add(neighborId);
					distances.add(newPath);
					queue.add(neighborId);
				}
			}
		}
		return new ArrayList<Integer>();
	}
	
	public List djikstra(int start, int dest) {
		HashSet<Integer> unvisitedIds = new HashSet<Integer>();
		for(int i=0; i<this.stations.size(); i++) {
			unvisitedIds.add(this.stations.get(i).getId());
		}
		
		HashMap<Integer, Double> distances = new HashMap<Integer, Double>();
		for(int i=0; i<this.stations.size(); i++) {
			distances.put(this.stations.get(i).getId(), Double.MAX_VALUE);
		}
		distances.put(start, 0.0);
		
		TreeSet queue = new TreeSet(new PairComparator());
		queue.add(new SimpleEntry(distances.get(start), start));
		
		HashMap<Double, List<Integer>> pathes = new HashMap<Double, List<Integer>>();
		List<Integer> firstList = new ArrayList<Integer>();
		firstList.add(start);
		pathes.put(0.0, firstList);
		
		while(!queue.isEmpty()) {
			SimpleEntry extractedPair = (SimpleEntry) queue.pollFirst();
			int extractedStationId = (Integer) extractedPair.getValue();
			
			List<Integer> currentPath = pathes.get(distances.get(extractedStationId));
			
			if(unvisitedIds.contains(extractedStationId)) {
				unvisitedIds.remove(extractedStationId);
				
				if(!unvisitedIds.contains(dest)) {
					return pathes.get(distances.get(dest));
				}
				
				for(int neighborId : this.neighbors(extractedStationId)) {
					Edge edge = getEdge(extractedStationId, neighborId);
					
					if(unvisitedIds.contains(neighborId)) {
						double currentDistance = distances.get(neighborId);
						double newDistance = distances.get(extractedStationId) + edge.getWeight();
						
						if(newDistance < currentDistance) {
							queue.add(new SimpleEntry(newDistance, neighborId));
							distances.put(neighborId, newDistance);
							List<Integer> newPath = new ArrayList<Integer>();
							pathes.remove(distances.get(currentPath.get(currentPath.size()-1)));
							newPath.addAll(currentPath);
							newPath.add(neighborId);
							pathes.put(newDistance, newPath);
						}
					}
					
				}
			}
		}
		return null;
	}
	
	public ArrayList<Integer> diameter() {
		int diameter = -1;
		ArrayList<Integer> diameterPath = new ArrayList<Integer>();
		for (int i = 0; i < this.stations.size(); i++) {
			for (int j = 0; j < this.stations.size(); j++) {
				if (i != j) {
					ArrayList<Integer> path = this.bfs(this.stations.get(i).getId(), this.stations.get(j).getId());
					int distance = path.size();
					if (distance > diameter) {
						diameter = distance;
						diameterPath = path;
					}
				}
			}
		}
		return diameterPath;
	}
	
	public void printDiameter() {
		System.out.println("Searching for diameter");
		ArrayList<Integer> diameter = this.diameter();
		System.out.println("This network's diameter is made of these stations : ");
		
		for (int stationId : diameter) {
			String stationName = this.getStation(stationId).getName();
			System.out.print(stationName);
			System.out.print(", ");
		}
		System.out.println();
		System.out.println("The diameter of the graph is " + diameter.size());
	}
	
	public void prettyPrintPath(List<Integer> res) {
		int tmp = res.get(res.size()-1);
		res.remove(res.size()-1);
		for(int stationId : res) {
			Station s = getStation(stationId);
			System.out.print(s.getName() + " -> ");
		}
		System.out.print(getStation(tmp).getName() + "\n");
	}
	
	static class PairComparator implements Comparator {
		
		public int compare(Object o2, Object o1) {
			// This method must return an int. As we manipulate Double instead, we have to make an approximation before
			// comparing each number.
			Double d1 = (Double) ((SimpleEntry) o1).getKey();
			Double d2 = (Double) ((SimpleEntry) o2).getKey();
			Double res = (d1 - d2)*1000000000;
			return res.intValue();
		}
	}

}
