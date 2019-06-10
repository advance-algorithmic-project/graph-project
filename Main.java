package project.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		Network paris = GTFS();
		paris.writeToJson();
		//Network paris = new Network(new File("stations.json"), new File("edges.json"));
		System.out.println("Finished!");
		
		paris.diameterDjikstra();
		
		// Long trajet
		/*
		paris.prettyPrintPath(paris.djikstra(1747, 1744));
		paris.prettyPrintPath(paris.bfs(1747, 1744));
		*/
		
		// Ligne10 test
		/*
		paris.prettyPrintPath(paris.djikstra(2026, 1817));
		paris.prettyPrintPath(paris.bfs(2026, 1817));
		*/
		
		// Ligne1 transit Ligne2
		/*
		paris.prettyPrintPath(paris.djikstra(2035, 1910));
		paris.prettyPrintPath(paris.bfs(2035, 1910));
		*/
		
		//paris.printDiameter();
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
		//stopsFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_Fun/stops.txt"));
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
		//8433,,"Porte d'Auteuil","Route des Lacs - 75116",48.84822598041119,2.257698885660922,0,
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_10/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_11/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_12/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_13/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_14/stop_times.txt"));
		//lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_Fun/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_METRO_Orv/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_RER_A/stop_times.txt"));
		lineFiles.add(new File("RATP_GTFS_LINES/RATP_GTFS_RER_B/stop_times.txt"));
		
		return new Network(stopsFiles,lineFiles);
	}
	
}
