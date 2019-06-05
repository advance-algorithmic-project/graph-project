package project.json;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
			this.stations = objectMapper.readValue(jsonFile1, List.class);
			this.edges = objectMapper.readValue(jsonFile1, List.class);
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
	
	public Station getStation(String stationName) {
		for (int i=0; i<this.stations.size(); i++) {
			if(stationName.equals(this.stations.get(i).getName())) {
				return this.stations.get(i);
			}
		}
		return null;
	}

}
