import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * Snapshot deals with storing the id and current state of the database.
 * As well as storing this data, the Snapshot class should manage operations
 * related to snapshots.
 */

public class Snapshot {
	private int id;
	private List<Entry> entries;

	public Snapshot(int id, List<Entry> entries) {
		//Constructor method

		this.id = id;
		this.entries = entries;
	}

	public int getId(){
		//Getter method for ID
		//@return id of the snapshot

		return id;
	}

	public void removeKey(String key) {
		//Finds and removes the key from entries stored in snapshot
		//We don't care in this function whether key exists or not as only purge calls this method and that alwyas prints ok
		//@param key the key to remove

		List<Entry> temp = new ArrayList<Entry>(entries);

		for(int i = 0; i < entries.size(); i++){
			if(entries.get(i).getKey().equals(key)){
				temp.remove(i);
			}
		}

		entries = temp;
	}

	public List<Entry> rollback() {
		//Returns list of entries for the restored state
		//Essentially a getter method
		//@return the list of entries

		return entries;
	}

	public void archive(String filename) {
		//Saves snapshot to the designated file
		//@param filename the name of the file - NB may not exist and need to create

		File output = new File(filename);

		try{
			PrintWriter writer = new PrintWriter(output);

			String printEntries = Entry.listAllEntries(entries).replace(" [", "|");
			printEntries = printEntries.replace(" ",",");
			printEntries = printEntries.replace("]", "");
			writer.println(printEntries);
			writer.close();

			System.out.println("ok");
		} catch(FileNotFoundException e){
			System.out.println("file not found");
		}

	}

	public static List<Entry> restore(String filename) {
		//Loads and restore a snapshot from file
		//@param filename name of file
		//@return list of entries - NB db deals with the list and reassigns it

		File input = new File(filename);
		List<Entry> newEntries = new ArrayList<Entry>();
		Boolean noError = true; //If any invalid input occurs we don't restore anything

		try{
			Scanner scan = new Scanner(input);
			while(scan.hasNextLine()){
				String inpLine = scan.nextLine();
				String[] line = inpLine.split("[|,]");

				if(line.length < 2){
					//An invalid line
					System.out.println("invalid input");
					noError = false;
				} else {
					List<Integer> values = new ArrayList<Integer>();
					Boolean validString = true;

					for(int i = 1; i < line.length; i++){
						try{
							values.add(Integer.parseInt(line[i]));
						} catch(NumberFormatException e){
							System.out.println("invalid input");
							validString = false;
							noError = false;
							break;
						}
					}

					if(validString){
						newEntries.add(new Entry(line[0], values));
					}
				}


			}
		} catch(FileNotFoundException e){
			System.out.println("file not found");
		}

		if(!noError){
			newEntries.clear();
		}
		return newEntries;
	}

	public static String listAllSnapshots(List<Snapshot> snapshots) {
		//Formats the snapshot IDS for display
		//@param snapshots the snapshots to display
		//@return the String to display - NB ends with no new line

		String listSnapshot = "";

		for(int i = 0; i < snapshots.size(); i++){
			listSnapshot += snapshots.get(i).id;
			if(i != snapshots.size()-1){
				listSnapshot += "\n";
			}
		}

		return listSnapshot;
	}
}
