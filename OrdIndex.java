package disk_store;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An ordered index.  Duplicate search key values are allowed,
 * but not duplicate index table entries.  In DB terminology, a
 * search key is not a superkey.
 * 
 * A limitation of this class is that only single integer search
 * keys are supported.
 *
 */

public class OrdIndex implements DBIndex {

	/**
	 * Create an new ordered index.
	 */
	private ArrayList<Pair<Integer, Integer>> data;

	public OrdIndex() {
		data = new ArrayList<Pair<Integer, Integer>>();
	}
	
	@Override
	public List<Integer> lookup(int key) {
		ArrayList<Integer> temp = new ArrayList<Integer> ();
		for(int i = 0; i < data.size(); i++) {
			if(key == data.get(i).getKey()) {
				temp.add(data.get(i).getValue());
			}
		}
		return temp;
	}
	
	@Override
	public void insert(int key, int blockNum) {
		data.add(new Pair<Integer, Integer>(key, blockNum));

	}

	@Override
	public void delete(int key, int blockNum) {
		for(int i = 0; i < data.size(); i++) {
			if(key == data.get(i).getKey() && blockNum == data.get(i).getValue()) {
				data.remove(i);
			}
		}
	}
	
	/**
	 * Return the number of entries in the index
	 * @return
	 */
	public int size() {
		// you may find it useful to implement this
		return data.size();
	}
	
	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}
}
