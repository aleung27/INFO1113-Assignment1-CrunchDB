import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * Entry deals with storing the key and value associated with entries in the
 * database.
 * As well as storing the data the entry class should manage operations
 * associated with any Entry.
 */

public class Entry {
	private String key;
	private List<Integer> values;

	public Entry(String key, List<Integer> values) {
		//Constructor method
		
		this.key = key;
		this.values = values;
	}

	public String getKey(){
		//Getter method for key
		//@return the value of key

		return key;
	}

	public List<Integer> getValues(){
		//Getter method for the values
		//@return the list of values

		return values;
	}

	public String get() {
		// Formats the Entry for Display - NB NO NEWLINE (Automarker doesn'l like it :O)
		// @return = String of values

		String returnValues = "["; //stores the string we'll return
		for(int i = 0; i < values.size(); i++){
			returnValues += Integer.toString(values.get(i));;
			if(i != values.size()-1){
				returnValues += " ";
			}
		}
		returnValues += "]";

		return returnValues;
	}

	public void set(List<Integer> values) {
		// Sets the value of this Entry - NB overwrites
		// @param values the values to set

		this.values = values;
	}

	public void push(List<Integer> values) {
		//Adds the values to the start - NB opposite order
		//@param values the values to add

		Collections.reverse(values);
		this.values.addAll(0, values);
	}

	public void append(List<Integer> values) {
		//Adds the values to the end
		//@param values the values to add

		this.values.addAll(values);
	}

	public Integer pick(int index) {
		//Finds the value at the given index
		//@param index the index
		//@return the value at the index - NB null if index doesn't exist


		//As entries are 1-indexed we will subtract one from given param
		index--;
		try{
			return this.values.get(index);
		} catch (IndexOutOfBoundsException e){
			System.out.println("index out of range");
			return null;
		}
	}

	public Integer pluck(int index) {
		//Finds and removes the value at the given index
		//@param index the index
		//@return the value at the index - NB null if index doesn't exist

		//As entries are 1-indexed we will subtract one from given param
		index--;
		try{
			int valueAtIndex = values.get(index);
			values.remove(index);
			return valueAtIndex;
		} catch (IndexOutOfBoundsException e){
			System.out.println("index out of range");
			return null;
		}
	}

	public Integer pop() {
		//Finds and removes the first value
		//@return the first value - NB null if no elements

		try{
			int valueAtIndex = values.get(0);
			values.remove(0);
			return valueAtIndex;
		} catch (IndexOutOfBoundsException e){
			System.out.println("nil");
			return null;
		}

	}

	public Integer min() {
		//Finds the minimum value
		//@return the minimum value - NB null if no min

		try{
			return Collections.min(values);
		} catch (NoSuchElementException e){
			System.out.println("nil");
			return null;
		}
	}

	public Integer max() {
		//Finds the maximum value
		//@return the max value - NB null if no max

		try{
			return Collections.max(values);
		} catch (NoSuchElementException e){
			System.out.println("nil");
			return null;
		}
	}

	public Integer sum() {
		//Computes the sum of all values
		//@return the sum - NB null if no sum exists

		int total = 0;
		for(int next: values) total += next;

		if(values.size() == 0) {
			System.out.println("nil");
			return null;
		}

		return total;
	}

	public Integer len() {
		//Finds the number of values
		//@return the number of values;

		return values.size();
	}

	public void rev() {
		//Reverses the order of the values

		Collections.reverse(values);
	}

	public void uniq() {
		//Removes adjacent duplicates
		//~O(N -> NlogN) Solution

		List<Integer> temp = new ArrayList<Integer>();

		for(int i = 0; i < values.size(); i++){
			temp.add(values.get(i));
			for(int j = i+1; j < values.size(); j++){
				if(values.get(j) == values.get(i)){
					i = j;
				} else break;
			}
		}

		values = temp;
	}

	public void sort() {
		//Sorts the values in ASCENDING order

		Collections.sort(values);
	}

	public static List<Integer> diff(List<Entry> entries) {
		//Computes the set difference of the entries
		//@param entries the list of entries
		//@return the difference

		List<Integer> differences = new ArrayList<Integer>();
		HashMap <Integer, Integer> map = new HashMap<Integer, Integer>();
		HashMap <Integer, Boolean> duplicate = new HashMap<Integer, Boolean>();
		//We'll have a hashmap where each entries value is a key and the value is the amount of times we've encountered that value
		//At the end, all keys with value 1 in the hashmap are the unique entries
		//We have the hashmap duplicate to ensure entries with duplicate values are not counted more than once (as they are in the same set)

		for(Entry nextEntry: entries){
			for(int nextValue: nextEntry.values){
				if(map.containsKey(nextValue) && !duplicate.containsKey(nextValue)){
					//We've encountered the value before
					map.put(nextValue, map.get(nextValue)+1);
				} else if(!duplicate.containsKey(nextValue)) {
					//Adding value to the hashmap if we haven't encountered
					map.put(nextValue, 1);
				}

				if(!duplicate.containsKey(nextValue)){
					duplicate.put(nextValue, true);
				}
			}
			duplicate.clear(); //Clearing the duplicate hashmap for the next entry
		}

		for(int nextKey: map.keySet()){
			if(map.get(nextKey) == 1){
				differences.add(nextKey);
			}
		}

		Collections.sort(differences);
		return differences;
	}

	public static List<Integer> inter(List<Entry> entries) {
		//Computes the set intersection of the entries A n B
		//@param entries list of entries
		//@return the resulting values

		List<Integer> intersection = new ArrayList<Integer>();
		HashMap <Integer, Integer> map = new HashMap<Integer, Integer>();
		HashMap <Integer, Boolean> duplicate = new HashMap<Integer, Boolean>();
		//Similar to Diff except we instead check that the value of keys is equal to entries.size() (in all entries)
		//We have the hashmap duplicate to ensure entries with duplicate values are not counted more than once (as they are in the same set)

		for(Entry nextEntry: entries){
			for(int nextValue: nextEntry.values){
				if(map.containsKey(nextValue) && !duplicate.containsKey(nextValue)){
					//We've encountered the value before
					map.put(nextValue, map.get(nextValue)+1);
				} else if(!duplicate.containsKey(nextValue)) {
					//Adding value to the hashmap if we haven't encountered
					map.put(nextValue, 1);
				}

				if(!duplicate.containsKey(nextValue)){
					duplicate.put(nextValue, true);
				}
			}
			duplicate.clear(); //Clearing the duplicate hashmap for the next entry
		}

		for(int nextKey: map.keySet()){
			if(map.get(nextKey) == entries.size()){
				intersection.add(nextKey);
			}
		}

		Collections.sort(intersection);
		return intersection;
	}

	public static List<Integer> union(List<Entry> entries) {
		//Computes the set union of the entries A U B
		//@param entries list of entries
		//@return list of resulting values

		List<Integer> union = new ArrayList<Integer>();

		for(Entry nextEntry: entries){
			for(int nextValue: nextEntry.values){
				if(!union.contains(nextValue)){
					union.add(nextValue);
				}
			}
		}

		Collections.sort(union);
		return union;
	}

	public static List<List<Integer>> cartprod(List<Entry> entries) {
		//Computes the Cartesian Product of the entries
		//@param entries list of entries
		//@return the resulting cartesian product in a 2D list

		List<List<Integer>> cartesian = new ArrayList<List<Integer>>();

		cartesian = generateCartProduct(0, entries);

		//Reversing for the right cartprod
		for(List<Integer> next: cartesian){
			Collections.reverse(next);
		}

		return cartesian;
	}

	public static List<List<Integer>> generateCartProduct(int currentIndex, List<Entry> entries){
		//Recursive function which returns us a 2D List representing the cartproduct - NB each list in is reversed due to recursion
		//@param currentIndex The current index we are recursing at (working from 0 -> N-1)
		//@param entries the list of entries
		//@return the current 2D list of cartesian products

		List<List<Integer>> temp = new ArrayList<List<Integer>>();
		Entry currentEntry = entries.get(currentIndex);

		//Base case when n is the last set, we return that sets value as individual lists
		if(currentIndex == entries.size()-1){
			for(int i = 0; i < currentEntry.values.size(); i++){
				List<Integer> app = new ArrayList<Integer>();
				app.add(currentEntry.values.get(i));
				temp.add(app);
			}
			return temp;
		} else{
			//Recursive relation, we get the cartprod of the next index (closer towards numsets) and get the new cartprod
			List<List<Integer>> recursionResult = new ArrayList<List<Integer>>();
			recursionResult = generateCartProduct(currentIndex+1, entries); //The cartprod of the one before

			for(int i = 0; i < currentEntry.values.size(); i++){
				for(int j = 0; j < recursionResult.size(); j++){
					List<Integer> currentList = new ArrayList<Integer>(recursionResult.get(j)); //Copy of recursionResult - NB mutability
					currentList.add(currentEntry.values.get(i));
					temp.add(currentList);
				}
			}
			return temp;
		}

	}

	public static String listAllEntries(List<Entry> entries) {
		//Formats all the entries for displays
		//@param entries list of entries to display
		//@return entries with values

		String listEntries = "";

		for(int i = 0; i < entries.size(); i++){
			listEntries += entries.get(i).key + " ";
			listEntries += entries.get(i).get();
			if(i != entries.size()-1) listEntries += "\n";
		}

		return listEntries;
	}

}
