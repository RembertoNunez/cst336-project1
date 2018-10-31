package disk_store;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.util.Pair;
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

	private ArrayList<Pair<Integer, Integer>> data;

	/**
	 * Create an new ordered index.
	 */
	public OrdIndex() {
		data= new ArrayList<Pair<Integer, Integer>> ();
	}
	
	@Override
	public List<Integer> lookup(int key) {
		ArrayList<Integer> ret= new ArrayList<Integer>();
		boolean down= true, up = true;
		int d, u, current;
		current=findIndex(key);
		if(current<data.size()) {
			u = current + 1;
			d = current - 1;

			if (key == data.get(current).getKey()) {
				ret.add(data.get(current).getValue());
			}
			while (down && up) {
				if (d > -1 && down) {
					if (key == data.get(d).getKey()) {
						ret.add(data.get(d).getValue());
						d--;
					} else
						down = false;
				}
				if (u < data.size() && up) {
					if (key == data.get(u).getKey()) {
						ret.add(data.get(u).getValue());
						u++;
					} else
						up = false;
				}
			}
		}
		//ArrayList<Pair<Integer, Integer>> data;

		Collections.sort(ret);
		return ret;
	}

	private int findIndex(int key)
	{
		int upperLimit= this.size();
		int lowerLimit= 0;
		int current= upperLimit/2;
		int val;
		if (upperLimit==0)
			return 0;
		while(upperLimit>lowerLimit)
		{
			System.out.println(upperLimit+" "+ lowerLimit+" "+ current);
			val=data.get(current).getKey();
			if(val==key ) {
				System.out.println(current);
				return current;
			}else if(val<key){
				lowerLimit=current+1;
				current= ((upperLimit-lowerLimit)/2)+lowerLimit;
			}
			else
			{
				upperLimit=current-1;
				current= ((upperLimit-lowerLimit)/2)+lowerLimit;
			}
		}
		System.out.println(current);
		if( current<data.size() && key>data.get(current).getKey())
			current++;
		return current;
	}

	@Override
	public void insert(int key, int blockNum) {
		int index= findIndex(key);
		if(index>=data.size())
			data.add(new Pair<Integer, Integer>(key,blockNum));
		else
			data.add(index,new Pair<Integer, Integer>(key,blockNum));
		System.out.println(data);
	}

	@Override
	public void delete(int key, int blockNum) {
		boolean down = true, up = true;
		int d, u, current;
		current = findIndex(key);
		if (current < data.size()) {
			u = current + 1;
			d = current - 1;

			if (key == data.get(current).getKey()) {
				if (data.get(current).getValue() == blockNum) {
					data.remove(current);
					return;
				}
			}

			while (down && up) {
				if (d > -1 && down) {
					if (key == data.get(d).getKey()) {
						if (data.get(d).getValue() == blockNum) {
							data.remove(d);
							return;
						}
						d--;
					} else
						down = false;
				}
				if (u < data.size() && up) {
					if (key == data.get(u).getKey()) {
						if (data.get(u).getValue() == blockNum) {
							data.remove(u);
							return;
						}
						u++;
					} else
						up = false;
				}
			}
			System.out.println("Not found: " + key + ", " + blockNum);
		}
	}
	
	/**
	 * Return the number of entries in the index
	 * @return
	 */
	public int size() {
		// you may find it useful to implement this
		//System.out.println(data.size());
		return data.size();
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
}
