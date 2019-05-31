package project.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {

		File file = new File("RATP_GTFS_FULL/stops.txt");
		
		
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
		
		Network paris = new Network(file, lineFiles);
		paris.writeToJson();
		
		Network net = new Network(new File("stations.json"), new File("edges.json"));
		
		//ToFile.readDataLineByLine(file); 
	}

}
