import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Collections;

/**
 * This is responsible for the overall management of the database.
 * CrunchDB should deal with taking input from the user and displaying the correct
 * output while passing off the more complicated work to the corresponding
 * classes.
 */

public class CrunchDB {

	private List<Entry> entries;
	private List<Snapshot> snapshots;

	public CrunchDB() {
		//Constructor method

		this.entries = new ArrayList<Entry>();
		this.snapshots = new ArrayList<Snapshot>();
	}

	private void listKeys() {
		//Displays all keys in the current state

		if(entries.size() == 0){
			System.out.println("no keys");
		} else {
			for(int i = entries.size()-1; i >= 0; i--){
				System.out.println(entries.get(i).getKey());
			}
		}

		System.out.println();
	}

	private void listEntries() {
		//Displays all entries from most recently added to least recently, so we reverse it

		if(entries.size() == 0){
			System.out.println("no entries");
		} else {
			List<Entry> revEntries = new ArrayList<Entry>(entries);
			Collections.reverse(revEntries);
			System.out.println(Entry.listAllEntries(revEntries));
		}

		System.out.println();
	}

	private void listSnapshot() {
		//Displays all snapshots in the current state

		if(snapshots.size() == 0){
			System.out.println("no snapshots");
		} else {
			List<Snapshot> revSnapshots = new ArrayList<Snapshot>(snapshots);
			Collections.reverse(revSnapshots);
			System.out.println(Snapshot.listAllSnapshots(revSnapshots));
		}

		System.out.println();
	}

	private void get(String key) {
		//Prints out the list of values associated with the key
		//@param key the key whose value we want to print

		Boolean foundKey = false;
		for(Entry next: entries){
			if(next.getKey().equals(key)){
				System.out.println(next.get());
				foundKey = true;
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void del(String key) {
		//Deletes entry from current state
		//@param key the key of the entry

		List<Entry> temp = new ArrayList<Entry>(entries);
		Boolean foundKey = false;

		for(int i = 0; i < entries.size(); i++){
			if(entries.get(i).getKey().equals(key)){
				temp.remove(i);
				System.out.println("ok");
				foundKey = true;
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		entries = temp;
		System.out.println();
	}

	private void set(String key, List<Integer> values) {
		//Sets the entry values for a specific key
		//@param key the key value of the entry
		//@param values the list of values associated with the key

		boolean reassigned = false;

		for(int i = 0; i < entries.size(); i++){
			if(entries.get(i).getKey().equals(key)){
				//We already have that key in our list of entries - so we need to remove it
				entries.get(i).set(values);
				reassigned = true;
				break;
			}
		}

		if(!reassigned){
			//A new key-value pair to add to our list of entries
			entries.add(new Entry(key, values));
		}

		System.out.println("ok\n");
	}

	private void push(String key, List<Integer> values) {
		//Pushes the values given to the front of the entry given by the key
		//@param key the key value of the entry
		//@param values the values to push

		Boolean foundKey = false;

		for(Entry next: entries){
			if(next.getKey().equals(key)){
				next.push(values);
				foundKey = true;
				System.out.println("ok");
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void append(String key, List<Integer> values) {
		//Appends values to the end of the entry given by key
		//@param key the key value of the entry
		//@param values list of values to append

		Boolean foundKey = false;

		for(Entry next: entries){
			if(next.getKey().equals(key)){
				next.append(values);
				foundKey = true;
				System.out.println("ok");
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void pick(String key, int index) {
		//Displays the value gievn by the index
		//@param key the key of the entry
		//@param index the index of the value we want to display

		Boolean foundKey = false;

		for(Entry next: entries){
			if(next.getKey().equals(key)){
				Integer pick = next.pick(index);
				if(pick != null){
					System.out.println(pick);
				}
				foundKey = true;
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void pluck(String key, int index) {
		//Displays and removes the value given by the index
		//@param key the key of the entry
		//@param index the index of the value we want to display and remove

		Boolean foundKey = false;

		for(Entry next: entries){
			if(next.getKey().equals(key)){
				Integer pluck = next.pluck(index);
				if(pluck != null){
					System.out.println(pluck);
				}
				foundKey = true;
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void pop(String key) {
		//Displays and removes the front value
		//@param key the key of the entry

		Boolean foundKey = false;

		for(Entry next: entries){
			if(next.getKey().equals(key)){
				Integer pop = next.pop();
				if(pop != null){
					System.out.println(pop);
				}
				foundKey = true;
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		System.out.println();
	}

/*******************************************************START OF SNAPSHOT INVOLVING METHODS*******************************************************/

	private void purge(String key) {
        //Deletes entry from current state and all snapshots
        //@param the key of the entry
		//Could use this.del but we have unnecassary print statements in it - NB purge always prints ok

		List<Entry> temp = new ArrayList<Entry>(entries);

		for(int i = 0; i < entries.size(); i++){
			if(entries.get(i).getKey().equals(key)){
				temp.remove(i);
			}
		}

		entries = temp;

        for(Snapshot nextSnapshot: snapshots){
            nextSnapshot.removeKey(key);
        }
		System.out.println("ok\n");
	}

	private void drop(int id) {
        //Deletes snapshot
        //@param id the id of the snapshot

        List<Snapshot> temp = new ArrayList<Snapshot>(snapshots);
        Boolean foundId = false;

        for(int i = 0; i < snapshots.size(); i++){
            if(snapshots.get(i).getId() == id){
                temp.remove(i);
                foundId = true;
                System.out.println("ok");
            }
        }

        if(!foundId){
            System.out.println("no such snapshot");
        }

        snapshots = temp;

        System.out.println();
	}

	private void rollback(int id) {
        //Restores to snapshot and deletes newer snapshots
        //@param id the id of the snapshot
        //BE CAREFUL OF MUTABILITY!!

        List<Snapshot> tempSnapshot  = new ArrayList<Snapshot>();
        List<Entry> tempEntry = new ArrayList<Entry>(); //For mutability once more
        Boolean foundId = false;

        for(Snapshot next: snapshots){
            tempSnapshot.add(next);
            if(next.getId() == id){
                for(int i = 0; i < next.rollback().size(); i++){
                    List<Integer> tempValues = new ArrayList<Integer>();

                    for(int j = 0; j < next.rollback().get(i).getValues().size(); j++){
                        tempValues.add(next.rollback().get(i).getValues().get(j));
                    }

                    tempEntry.add(new Entry(next.rollback().get(i).getKey(), tempValues));
                }
                entries = tempEntry;
                foundId = true;
                System.out.println("ok");
                break;
            }
        }

        if(!foundId){
            System.out.println("no such snapshot");
        }

        snapshots = tempSnapshot;
        System.out.println();
	}

	private void checkout(int id) {
        //Same as rollback but doesn't get rid of snapshots
        //@param id the id of the snapshot

        List<Entry> tempEntry = new ArrayList<Entry>(); //For mutability once more
        Boolean foundId = false;

        for(Snapshot next: snapshots){
            if(next.getId() == id){
                for(int i = 0; i < next.rollback().size(); i++){
                    List<Integer> tempValues = new ArrayList<Integer>();

                    for(int j = 0; j < next.rollback().get(i).getValues().size(); j++){
                        tempValues.add(next.rollback().get(i).getValues().get(j));
                    }

                    tempEntry.add(new Entry(next.rollback().get(i).getKey(), tempValues));
                }
                entries = tempEntry;
                foundId = true;
                System.out.println("ok");
                break;
            }
        }

        if(!foundId){
            System.out.println("no such snapshot");
        }

        System.out.println();
	}

	private void snapshot() {
        //Saves the current state as a snapshot.

        List<Entry> temp = new ArrayList<Entry>(); //For mutability reasons

        for(Entry next: entries){
            List<Integer> tempValues = new ArrayList<Integer>();

            for(int i = 0; i < next.getValues().size(); i++){
                tempValues.add(next.getValues().get(i));
            }

            Entry tempEntry = new Entry(next.getKey(), tempValues);
            temp.add(tempEntry);
        }

		if(snapshots.size() > 0){
			int lastId = snapshots.get(snapshots.size()-1).getId();
			snapshots.add(new Snapshot(lastId+1, temp));
			System.out.printf("saved as snapshot %d\n\n", lastId+1);
		} else {
			snapshots.add(new Snapshot(1, temp));
			System.out.printf("saved as snapshot 1\n\n");
		}
	}

	private void archive(int id, String filename) {
        //Saves snapshot to file
        //@param id the id of the snapshot we want to save
        //@param filename the name of the file

        Boolean foundId = false;
        for(Snapshot next: snapshots){
            if(next.getId() == id){
                next.archive(filename);
                foundId = true;
            }
        }

        if(!foundId){
            System.out.println("no such snapshot");
        }

        System.out.println();
	}

	private void restore(String filename) {
        //Loads and restore snapshot from a file - NB removes all current snapshots
        //@param filename filename which stores the desired snapshot

        List<Entry> restoredEntries = new ArrayList<Entry>();
        restoredEntries = Snapshot.restore(filename);

        if(restoredEntries.size() != 0){
            snapshots.clear();
            entries = restoredEntries;
            System.out.println("ok");
        }

        System.out.println();
	}

/*******************************************************END OF SNAPSHOT INVOLVING METHODS*******************************************************/

	private void min(String key) {
		//Displays the minimum value
		//@param key the key of the entry

		Boolean foundKey = false;

		for(Entry next: entries){
			if(next.getKey().equals(key)){
				Integer min = next.min();
				if(min != null){
					System.out.println(min);
				}
				foundKey = true;
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void max(String key) {
		//Displays the maximum value
		//@param key the key of the entry

		Boolean foundKey = false;

		for(Entry next: entries){
			if(next.getKey().equals(key)){
				Integer max = next.max();
				if(max != null){
					System.out.println(max);
				}
				foundKey = true;
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void sum(String key) {
		//Displays the Sum of values
		//@param key the key of the entry

		Boolean foundKey = false;

		for(Entry next: entries){
			if(next.getKey().equals(key)){
				Integer sum = next.sum();
				if(sum != null){
					System.out.println(sum);
				}
				foundKey = true;
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void len(String key) {
		//Displays the number of values in the entry
		//@param key the key of the entry

		Boolean foundKey = false;

		for(Entry next: entries){
			if(next.getKey().equals(key)){
				System.out.println(next.len());
				foundKey = true;
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void rev(String key) {
		//Reverses order of the values for the entry
		//@param key the key of the entry

		Boolean foundKey = false;

		for(Entry next:entries){
			if(next.getKey().equals(key)){
				next.rev();
				System.out.println("ok");
				foundKey = true;
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void uniq(String key) {
		//Removes adjacent values
		//@param key the key of the entry

		Boolean foundKey = false;

		for(Entry next:entries){
			if(next.getKey().equals(key)){
				next.uniq();
				System.out.println("ok");
				foundKey = true;
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void sort(String key) {
		//Sort in ascending order
		//@param key the key of the entry

		Boolean foundKey = false;

		for(Entry next:entries){
			if(next.getKey().equals(key)){
				next.sort();
				System.out.println("ok");
				foundKey = true;
			}
		}

		if(!foundKey){
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void diff(List<String> keys) {
		//Displays set difference of values in key
		//@param keys the keys of the entries
		//We guarantee at least two keys (may not be valid) are passed to the func or an error message occurs during input reading

		List<Entry> diffEntries = new ArrayList<Entry>();

		for(String nextKey: keys){
			for(Entry nextEntry: entries){
				if(nextEntry.getKey().equals(nextKey)){
					diffEntries.add(nextEntry);
					break;
				}
			}
		}

		if(diffEntries.size() == keys.size()){
			//All the keys are valid
			System.out.println(Entry.diff(diffEntries).toString().replace(",", ""));
		} else {
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void inter(List<String> keys) {
		//Displays the set intersection of values in keys.
		//@param keys the keys of the entries
		//We can guarantee at least two keys are passed once more (may be invalid)

		List<Entry> interEntries = new ArrayList<Entry>();

		for(String nextKey: keys){
			for(Entry nextEntry: entries){
				if(nextEntry.getKey().equals(nextKey)){
					interEntries.add(nextEntry);
					break;
				}
			}
		}

		if(interEntries.size() == keys.size()){
			//All the keys are valid
			System.out.println(Entry.inter(interEntries).toString().replace(",", ""));
		} else {
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void union(List<String> keys) {
		//Displays set union of values in keys
		//@param keys the keys of the entries
		//We again guarantee at least 2 keys with unknown validity

		List<Entry> unionEntries = new ArrayList<Entry>();

		for(String nextKey: keys){
			for(Entry nextEntry: entries){
				if(nextEntry.getKey().equals(nextKey)){
					unionEntries.add(nextEntry);
					break;
				}
			}
		}

		if(unionEntries.size() == keys.size()){
			//All the keys are valid
			System.out.println(Entry.union(unionEntries).toString().replace(",", ""));
		} else {
			System.out.println("no such key");
		}

		System.out.println();
	}

	private void cartprod(List<String> keys) {
		//Displays cartesian product of sets
		//@param keys the keys of the entries
		//We guarantee at least 2 once more

		List<Entry> cartesianEntries = new ArrayList<Entry>();

		for(String nextKey:keys){
			for(Entry nextEntry: entries){
				if(nextEntry.getKey().equals(nextKey)){
					cartesianEntries.add(nextEntry);
					break;
				}
			}
		}

		if(cartesianEntries.size() == keys.size()){
			String cartesianProduct = Entry.cartprod(cartesianEntries).toString().replace(",", "");
			cartesianProduct = cartesianProduct.replace("[[", "[ [");
			cartesianProduct = cartesianProduct.replace("]]" , "] ]");
			if(cartesianProduct == "[]"){
				//Case for cartesian product of empty set
				System.out.println("[ [] ]");
			} else System.out.println(cartesianProduct);
		} else {
			System.out.println("no such key");
		}

		System.out.println();
	}

	private static final String HELP =
		"BYE   clear database and exit\n"+
		"HELP  display this help message\n"+
		"\n"+
		"LIST KEYS       displays all keys in current state\n"+
		"LIST ENTRIES    displays all entries in current state\n"+
		"LIST SNAPSHOTS  displays all snapshots in the database\n"+
		"\n"+
		"GET <key>    displays entry values\n"+
		"DEL <key>    deletes entry from current state\n"+
		"PURGE <key>  deletes entry from current state and snapshots\n"+
		"\n"+
		"SET <key> <value ...>     sets entry values\n"+
		"PUSH <key> <value ...>    pushes values to the front\n"+
		"APPEND <key> <value ...>  appends values to the back\n"+
		"\n"+
		"PICK <key> <index>   displays value at index\n"+
		"PLUCK <key> <index>  displays and removes value at index\n"+
		"POP <key>            displays and removes the front value\n"+
		"\n"+
		"DROP <id>      deletes snapshot\n"+
		"ROLLBACK <id>  restores to snapshot and deletes newer snapshots\n"+
		"CHECKOUT <id>  replaces current state with a copy of snapshot\n"+
		"SNAPSHOT       saves the current state as a snapshot\n"+
		"\n"+
		"ARCHIVE <id> <filename> saves snapshot to file\n"+
		"RESTORE <filename> loads snapshot from file\n"+
		"\n"+
		"MIN <key>  displays minimum value\n"+
		"MAX <key>  displays maximum value\n"+
		"SUM <key>  displays sum of values\n"+
		"LEN <key>  displays number of values\n"+
		"\n"+
		"REV <key>   reverses order of values\n"+
		"UNIQ <key>  removes repeated adjacent values\n"+
		"SORT <key>  sorts values in ascending order\n"+
		"\n"+
		"DIFF <key> <key ...>   displays set difference of values in keys\n"+
		"INTER <key> <key ...>  displays set intersection of values in keys\n"+
		"UNION <key> <key ...>  displays set union of values in keys\n"+
		"CARTPROD <key> <key ...>  displays set union of values in keys";

	public static void bye() {
		System.out.println("bye");
	}

	public static void help() {
		System.out.println(HELP);
	}

/*******************************************************MAIN METHOD*******************************************************/
/**************************************************SORRY MANUAL MARKER :(**************************************************/

	public static void main(String[] args) {
		CrunchDB program = new CrunchDB();
		Scanner scan = new Scanner(System.in);
		String errorMessage = "Incorrect usage, see HELP for command list\n";

		while(scan.hasNextLine()){
			System.out.printf("> ");
			String nextLine = scan.nextLine();
			String[] splitLine = nextLine.split(" ");
			splitLine[0] = splitLine[0].toUpperCase();

			//We initially seperate cases by length to deal with cases like "GET a a" where too few/many arguments are specified
			if(splitLine.length == 1){
				if(splitLine[0].equals("BYE")){
					CrunchDB.bye();
					return;
				} else if(splitLine[0].equals("HELP")){
					CrunchDB.help();
					System.out.println();
				} else if(splitLine[0].equals("SNAPSHOT")){
					program.snapshot();
				} else{
					//Error handling for incorrect input
					System.out.println(errorMessage);
				}
			} else if(splitLine.length == 2){
				if(splitLine[0].equals("LIST")){
					splitLine[1] = splitLine[1].toUpperCase();

					if(splitLine[1].equals("KEYS")){
						program.listKeys();
					} else if(splitLine[1].equals("ENTRIES")){
						program.listEntries();
					} else if(splitLine[1].equals("SNAPSHOTS")){
						program.listSnapshot();
					} else {
						System.out.println(errorMessage);
					}

				} else if(splitLine[0].equals("GET")){
					program.get(splitLine[1]);
				} else if(splitLine[0].equals("DEL")){
					program.del(splitLine[1]);
				} else if(splitLine[0].equals("PURGE")){
					program.purge(splitLine[1]);
				} else if(splitLine[0].equals("POP")){
					program.pop(splitLine[1]);
				} else if(splitLine[0].equals("DROP")){
					//Error handling done here as an integer is passed to the function
					try{
						program.drop(Integer.parseInt(splitLine[1]));
					} catch(IllegalArgumentException e){
						System.out.println(errorMessage);
					}
				} else if(splitLine[0].equals("ROLLBACK")){
					//Error handling done here as an integer is passed to the function
					try{
						program.rollback(Integer.parseInt(splitLine[1]));
					} catch(IllegalArgumentException e){
						System.out.println(errorMessage);
					}
				} else if(splitLine[0].equals("CHECKOUT")){
					//Error handling done here as an integer is passed to the function
					try{
						program.checkout(Integer.parseInt(splitLine[1]));
					} catch(IllegalArgumentException e){
						System.out.println(errorMessage);
					}
				} else if(splitLine[0].equals("RESTORE")){
					program.restore(splitLine[1]);
				} else if(splitLine[0].equals("MIN")){
					program.min(splitLine[1]);
				} else if(splitLine[0].equals("MAX")){
					program.max(splitLine[1]);
				} else if(splitLine[0].equals("SUM")){
					program.sum(splitLine[1]);
				} else if(splitLine[0].equals("LEN")){
					program.len(splitLine[1]);
				} else if(splitLine[0].equals("REV")){
					program.rev(splitLine[1]);
				} else if(splitLine[0].equals("UNIQ")){
					program.uniq(splitLine[1]);
				} else if(splitLine[0].equals("SORT")){
					program.sort(splitLine[1]);
				} else {
					System.out.println(errorMessage);
				}
			} else {
				if(splitLine[0].equals("SET") || splitLine[0].equals("PUSH") || splitLine[0].equals("APPEND")){
					List<Integer> values = new ArrayList<Integer>();
					for(int i = 2; i < splitLine.length; i++){
						try{
							values.add(Integer.parseInt(splitLine[i]));
						} catch(NumberFormatException e){
							break;
						}
					}

					if(values.size() == splitLine.length - 2){ //No NumberFormatException happened if this condition is true
						if(splitLine[0].equals("SET")){
							program.set(splitLine[1], values);
						} else if(splitLine[0].equals("PUSH")){
							program.push(splitLine[1], values);
						} else if(splitLine[0].equals("APPEND")){
							program.append(splitLine[1], values);
						}
					} else {
						System.out.println(errorMessage);
					}

				} else if(splitLine[0].equals("PICK") || splitLine[0].equals("PLUCK")){
					//NB we pass an index which is 1-indexed - our entry functions deal with that

					try{
						int index = Integer.parseInt(splitLine[2]);
						if(splitLine[0].equals("PICK") && splitLine.length == 3){
							program.pick(splitLine[1], index);
						} else if(splitLine[0].equals("PLUCK") && splitLine.length == 3){
							program.pluck(splitLine[1], index);
						} else{
							System.out.println(errorMessage);
						}
					} catch(NumberFormatException e){
						System.out.println(errorMessage);
					}
				} else if(splitLine[0].equals("ARCHIVE")){
					try{
						int index = Integer.parseInt(splitLine[1]);
						if(splitLine.length == 3){
							program.archive(index, splitLine[2]);
						} else {
							System.out.println(errorMessage);
						}
					} catch(NumberFormatException e){
						System.out.println(errorMessage);
					}
				} else if(splitLine[0].equals("DIFF") || splitLine[0].equals("INTER") || splitLine[0].equals("UNION") || splitLine[0].equals("CARTPROD")){
					List<String> keys = new ArrayList<String>();
					for(int i = 1; i < splitLine.length; i++){
						keys.add(splitLine[i]);
					}

					if(splitLine[0].equals("DIFF")){
						program.diff(keys);
					} else if(splitLine[0].equals("INTER")){
						program.inter(keys);
					} else if(splitLine[0].equals("UNION")){
						program.union(keys);
					} else if(splitLine[0].equals("CARTPROD")){
						program.cartprod(keys);
					}
				} else {
					System.out.println(errorMessage);
				}
			}
		}
	}
}
