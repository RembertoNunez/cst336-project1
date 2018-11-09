package disk_store;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

	class IndexObj{
		public int key;
		public int block;
		public int count;

		public IndexObj(int key, int block) {
			this.key = key;
			this.block = block;
			count=1;
		}


		public int getKey(){
			return key;
		}

		public int getValue(){
			return block;
		}
		public int decrease(){
			count--;
			return count;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			IndexObj indexObj = (IndexObj) o;
			return key == indexObj.key &&
					block == indexObj.block;
		}

		@Override
		public String toString() {
			return "IndexObj{" +
					"key=" + key +
					", block=" + block +
					", count=" + count +
					'}'+'\n';
		}
	}

	private ArrayList<IndexObj> data;

	/**
	 * Create an new ordered index.
	 */
	public OrdIndex() {
		data= new ArrayList<IndexObj> ();
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

			val=data.get(current).getKey();
			if(val==key ) {

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

		if( current<data.size() && key>data.get(current).getKey())
			current++;
		return current;
	}

	@Override
	public void insert(int key, int blockNum) {
		boolean down = true, up = true;
		int d, u, current;
		current = findIndex(key);
		if (current < data.size()) {
			u = current + 1;
			d = current - 1;

			if (key == data.get(current).getKey()) {
				if (data.get(current).getValue() == blockNum) {
					data.get(current).count++;
					return;
				}
			}

			while (down || up) {
				if(d  <0)
					down = false;
				if(u >= data.size())
					up=false;
				if (down) {
					if (key == data.get(d).getKey()) {
						if (data.get(d).getValue() == blockNum) {
							data.get(d).count++;
							return;
						}
						d--;
					} else
						down = false;

				}
				if ( up) {
					if (key == data.get(u).getKey()) {
						if (data.get(u).getValue() == blockNum) {
							data.get(u).count++;
							return;
						}
						u++;
					} else
						up = false;

				}
			}

		}
//		int index= findIndex(key);
		if(current>=data.size())
			data.add(new IndexObj(key,blockNum));
		else
			data.add(current,new IndexObj(key,blockNum));
//		System.out.println(data);
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
					if(data.get(current).decrease()==0)
						data.remove(current);
					return;
				}
			}

			while (down || up) {
				if(d  <0)
					down = false;
				if(u >= data.size())
					up=false;
				if ( down) {
					if (key == data.get(d).getKey()) {
						if (data.get(d).getValue() == blockNum) {
							if(data.get(d).decrease()==0)
								data.remove(d);
							return;
						}
						d--;
					} else
						down = false;
				}
				if (up) {
					if (key == data.get(u).getKey()) {
						if (data.get(u).getValue() == blockNum) {
							if(data.get(u).decrease()==0)
								data.remove(u);
							return;
						}
						u++;
					} else
						up = false;
				}
			}

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
