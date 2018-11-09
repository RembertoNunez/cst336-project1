package disk_store;

import disk_store.OrdIndex.IndexObj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A hash index.  
 * 
 */

public class HashIndex implements DBIndex {
	private class IndexObj{
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
			OrdIndex.IndexObj indexObj = (OrdIndex.IndexObj) o;
			return key == indexObj.key &&
					block == indexObj.block;
		}

	}
	private IndexObj data[];

	/**
	 * Create an new index.
	 */
	public HashIndex() {
		data= new IndexObj[500];
	}
	
	@Override
	public List<Integer> lookup(int key) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void insert(int key, int blockNum) {
		int index= hashFunction(key);
//		while()
//		data[]
	}

	@Override
	public void delete(int key, int blockNum) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}

	private int hashFunction(int key){
		return (key * 31)%500;
	}
}