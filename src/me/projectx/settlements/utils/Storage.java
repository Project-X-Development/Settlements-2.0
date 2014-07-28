package me.projectx.settlements.utils;

import java.util.HashMap;
import java.util.Map;

public class Storage {

	private Map<String, Integer> ints = new HashMap<String, Integer>();
	private Map<String, String> strings = new HashMap<String, String>();
	private Map<String, Boolean> booleans = new HashMap<String, Boolean>();
	
	public void setInt(String key, int value){
		this.ints.put(key, value);
	}

	public void removeInt(String key){
		if(this.ints.containsKey(key)){
			this.ints.remove(key);
		}
	}

	public int getInt(String key){
		return this.ints.get(key);
	}

	public void addToInt(String key, int value){
		this.ints.put(key, this.ints.get(key) + value);
	}

	public void minusFromInt(String key, int value){
		this.ints.put(key, this.ints.get(key) - value);
	}

	public boolean isIntGreaterThan(String key, int min){
		return this.ints.get(key) >= min ? true : false;
	}

	public void setString(String key, String value){
		this.strings.put(key, value);
	}

	public void removeString(String key){
		if(this.strings.containsKey(key)){
			this.strings.remove(key);
		}
	}

	public String getString(String key){
		return this.strings.get(key);
	}

	public void setBoolean(String key, boolean value){
		this.booleans.put(key, value);
	}

	public void removeBoolean(String key){
		if(this.booleans.containsKey(key)){
			this.booleans.remove(key);
		}
	}

	public boolean getBoolean(String key){
		if(this.booleans.containsKey(key)){
			return this.booleans.get(key);	
		}
		return false;
	}
}
