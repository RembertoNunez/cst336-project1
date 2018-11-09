package disk_store;

import disk_store.OrdIndex.IndexObj;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * A hash index.  
 * 
 */

public class HashIndex implements DBIndex {

	private static int SIZE= 10000; //NUMBER OF BUCKETS
	private int counter; //Current # of inputs

	//Following a variation of chain hashing - The key will be the identifier
	//The block number and count will just keep adding at the end of the 'chain' (the ArrayList)
	private class IndexObj{
		public int key;
		public ArrayList<Integer> block;
		public ArrayList<Integer> count;

		public IndexObj(int key) {
			this.key=key;
			block= new ArrayList<>();
			count = new ArrayList<>();
		}


		public int getKey(){
			return key;
		}

		public void insert(int blockNum){
			int index=this.block.indexOf(blockNum);
			if(index!=-1){ //Found block number in current list - just increment counter
				count.set(index, count.get(index)+1);
			}
			else {//Did not find this block Number, have to append new block number and count
				block.add(blockNum);
				count.add(1);
				counter++;
			}
		}

		public void delete(int blockNum){
			int index=this.block.indexOf(blockNum);
			if(index!=-1){
				count.set(index, count.get(index)-1);
				System.out.println(count.get(index));
				if(count.get(index)==0){
					block.remove(index);
					count.remove(index);
					counter--;
				}
			}

			//throw new InvalidParameterException("Block Number to delete does not exists");
		}

	}
	private IndexObj data[];

	/**
	 * Create an new index.
	 */
	public HashIndex() {
		data= new IndexObj[SIZE]; //Created SIZE # of buckets
		counter=0;//Initialized current # of inputs to zero
	}
	
	@Override
	public List<Integer> lookup(int key) {
		int hashKey= hashFunction(key); //get the hashKey
		// Collision check
		while(true){ //Loop continuously
			if(data[hashKey]==null) //Until I hit a null
				break;
			if( data[hashKey].getKey()==key) //Or until i hit the correct key
				break;
			hashKey++; //Keep incrementing while I haven't found it
			if(hashKey>=SIZE){ //If I reached the end, loop back around
				hashKey=0;
			}
		}
		IndexObj result= data[hashKey];
		if(result==null){//Did not find any object with that key
			return new ArrayList<Integer>(); //Return empty lisy
		}

		//Sort and return
		Collections.sort(result.block);
		return result.block;
	}
	
	@Override
	public void insert(int key, int blockNum) {

		int hashKey= hashFunction(key);
		//Collision handling
		while(true){ //Loop continuously
			if(data[hashKey]==null) //Until I hit a null
				break;
			if( data[hashKey].getKey()==key) //Or until i hit the correct key
				break;
			hashKey++; // Keep incrementing while I haven't found it
			if(hashKey>=SIZE){ //If I reached the end, loop back around
				hashKey=0;
			}
		}
		if(data[hashKey]==null){ //First time I've seen this key
			IndexObj res = new IndexObj(key);//Create a new object
			res.insert(blockNum);
			data[hashKey]= res;
		}
		else{

			data[hashKey].insert(blockNum);//Just use the object there to insert blockNum
		}
	}

	@Override
	public void delete(int key, int blockNum) {
		int hashKey= hashFunction(key);
		while(true){ //Loop continuously
			if(data[hashKey]==null) //Until I hit a null
				break;
			if( data[hashKey].getKey()==key) //Or until i hit the correct key
				break;
			hashKey++; // Keep incrementing while I haven't found it
			if(hashKey>=SIZE){ //If I reached the end, loop back around
				hashKey=0;
			}
		}

		if(data[hashKey]!=null){ //Found spot with key
			data[hashKey].delete(blockNum);//Call delete with blockNum
		}
	}
	public int size() {
		//Counter was incremented whenever we added any new key to our hashMap
		return counter;
	}

	//Loop through everything and only print if not null
	//Probably exists a better way to do this...
	@Override
	public String toString() {
		String output="";
		for (int i = 0; i < SIZE; i++) {
			if(data[i]!=null){
				for (int j = 0; j < data[i].block.size(); j++) {
					output+="Key: "+ data[i].key;
					output+=" | Block: "+ data[i].block.get(j);
					output+=" | Count: "+ data[i].count.get(j);
				}
				output+="\n";
			}
		}
		return output;
	}

	//Simple hash function for integers
	private int hashFunction(int key){
		return (key * 31)%SIZE;
	}
}