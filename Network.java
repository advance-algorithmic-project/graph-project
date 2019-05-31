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
import com.opencsv.CSVReader;

public class Network {
	
	private List<Station> stations;
	private List<Edge> edges;
	
	
	public Network(File stationFile, List<File> lineFiles) {
		

		List<Station> listStations = new ArrayList<Station>();
		List<Edge> listEdges = new ArrayList<Edge>();
		
		try {
			
			FileReader filereader = new FileReader(stationFile);

			CSVReader csvReader = new CSVReader(filereader, ',', '\"', 1);
			String[] nextRecord; 
			
			while ((nextRecord = csvReader.readNext()) != null) {

				int id = Integer.parseInt(nextRecord[0]);
				String name = nextRecord[2];
				double lat = Double.parseDouble(nextRecord[4]);
				double lon = Double.parseDouble(nextRecord[5]);
				
				if (!(name.toUpperCase() == name)) {
					listStations.add(new Station(id, name, lat, lon));
				}
				
			} 
			
			List<Station> listStationsWithoutDuplicate = deleteDuplicates(listStations);
			this.stations = listStationsWithoutDuplicate;
			
			for (File file : lineFiles) {

				System.out.println(file);
				filereader = new FileReader(file);
				csvReader = new CSVReader(filereader, ',', '\"', 1);
				
				int station_id = 0, lastStation_id = 0;
				Long trip_id = 0L, lastTrip_id = 0L;
				Station station = new Station(), lastStation = new Station();
				String station_name = "";
				
				while ((nextRecord = csvReader.readNext()) != null) {
					trip_id = Long.parseLong(nextRecord[0]);
					station_id = Integer.parseInt(nextRecord[3]);

					for (int i = 0; i < listStations.size(); i++) {
						if (listStations.get(i).getId() == station_id) {
							station_name = listStations.get(i).getName();
							break;
						}
					}
					
					for (int i = 0; i < listStationsWithoutDuplicate.size(); i++) {
						if (listStations.get(i).getName().equals(station_name)) {
							station = listStations.get(i);
							break;
						}
					}
					
					boolean present = false;
					
					if (trip_id.equals(lastTrip_id)) {
						for (int i = 0; i < listEdges.size(); i++) {
							int id_aa = listEdges.get(i).getStation1_id();
							int id_ab = listEdges.get(i).getStation2_id();
							if (((id_aa == station_id && id_ab == lastStation_id) || (id_ab == station_id && id_aa == lastStation_id))) {
								present = true;
								break;
							}
						}
						if (present == false) {
							listEdges.add(new Edge(lastStation_id, station_id));
						}
					}
					
					
					lastStation_id = station_id;
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
	
	

}
