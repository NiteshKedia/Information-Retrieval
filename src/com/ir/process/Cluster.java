package com.ir.process;

import java.util.ArrayList;
import java.util.HashMap;

public class Cluster{
	
	public ArrayList<Integer> document;
	public HashMap<String,Double> centroid;
	
	public Cluster(){
		document = new ArrayList<Integer>();
		centroid = new HashMap<String,Double>();
	}
}
