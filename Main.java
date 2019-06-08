package project.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		Network paris = GTFS();
		//paris.writeToJson();
		System.out.println(paris.getStation(2035).getName());
		System.out.println(paris.getStation(1642).getName());
		System.out.println(paris.djikstra(2035, 1642));
		
		//Network paris = new Network(new File("stations.json"), new File("edges.json"));
		System.out.println("Finished!");
		
		//System.out.println("The diameter of the graph is " + paris.diameter());
	}
	
	
	public static Network GTFS() {
		List<File> stopsFiles = new ArrayList<File>();
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_1/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_2/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_3/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_3b/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_4/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_5/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_6/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_7/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_7b/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_8/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_9/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_10/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_11/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_12/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_13/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_14/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_Fun/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_Orv/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_RER_A/stops.txt"));
		stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_RER_B/stops.txt"));
		
		List<File> lineFiles = new ArrayList<File>();
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_1/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_2/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_3/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_3b/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_4/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_5/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_6/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_7/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_7b/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_8/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_9/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_10/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_11/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_12/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_13/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_14/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_Fun/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_Orv/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_RER_A/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_RER_B/stop_times.txt"));
		
		return new Network(stopsFiles,lineFiles);
	}
	
}
