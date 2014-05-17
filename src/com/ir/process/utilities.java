package com.ir.process;

import org.apache.lucene.index.*;
import org.apache.lucene.document.*;
import org.jsoup.Jsoup;


import java.awt.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.Set;

import javax.lang.model.util.Elements;

public class utilities {
	
	
	public static <E> Map sortByComparator(Map unsortMap) {
		 
		LinkedList list = new LinkedList(unsortMap.entrySet());
 
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object ob1, Object ob2) {
				return ((Comparable) ((Map.Entry) (ob2)).getValue()).compareTo(((Map.Entry) (ob1)).getValue());
			}
		});
 
		// put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator<E> it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			if(!entry.getValue().equals(0.0))
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	public static <E> Map sortByComparatorPR (Map unsortMap) {
		LinkedList list = new LinkedList(unsortMap.entrySet());
		 
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object ob1, Object ob2) {
				return ((Comparable) ((Map.Entry) (ob2)).getValue()).compareTo(((Map.Entry) (ob1)).getValue());
			}
		});
 
		// put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator<E> it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			//if(!entry.getValue().equals(0.0))
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public static <E> Map sortByComparatorasc(Map unsortMap) {
		 
		LinkedList list = new LinkedList(unsortMap.entrySet());
 
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object ob1, Object ob2) {
				return ((Comparable) ((Map.Entry) (ob1)).getValue()).compareTo(((Map.Entry) (ob2)).getValue());
			}
		});
 
		// put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator<E> it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	
	
	public static void printMap(Map<Integer, Double> map,String str,IndexReader r) throws CorruptIndexException, IOException{
		
		int i=0;
		String folder = "C:\\Users\\Nitesh\\Google Drive\\2nd Sem Courses\\IR\\irs13\\Projectclass\\result3\\";
		//System.out.println("Vector Space Results For: "+str);
		//System.out.println("=========================================");
		for (Entry<Integer, Double> entry : map.entrySet()) {
			//if(entry.getValue() != 0){
				if(++i<10){
			//System.out.println(entry.getKey() + " Value : "				+ entry.getValue());
					//System.out.println(entry.getKey());
				Document d = r.document(entry.getKey());
				String url = d.getFieldable("path").stringValue();
				//System.out.println(url);
				org.jsoup.nodes.Document doc = Jsoup.parse(new File(folder+url), "UTF-8");
				org.jsoup.select.Elements content = doc.select("p");
				org.jsoup.select.Elements content2 = doc.select("li");
				String linkText = content.text();
				String linkText2 = content2.text();
				//if(linkText != null)
				//	System.out.println(linkText);
				//if(linkText2 != null)
					//System.out.println(linkText2);
				
				//System.out.println(entry.getKey()+":"+entry.getValue());
			}
		}
		//System.out.println("=========================================");
	}
	
	public static void printMap(Map<Integer, Double> map, String str, int k,IndexReader r) throws CorruptIndexException, IOException {
		
		int i=0;
		System.out.println("Page Rank Results For: "+str);
		System.out.println("=========================================");
		for (Entry<Integer, Double> entry : map.entrySet()) {
			if(entry.getValue()!=0){
				if(++i<=k){
					//System.out.println(i+": Key : " + entry.getKey() + " Value : " + entry.getValue());
					System.out.println(entry.getKey());
					Document d = r.document(entry.getKey());
					String url = d.getFieldable("path").stringValue();
					System.out.println(url);
					//System.out.println(url.replace("%%", "/"));
					}
			}
		}
		System.out.println("=========================================");
	}
	
	public static HashMap getUniqueCount(String[] str){
		HashMap<String,Integer> map=new HashMap<>();
	    Integer j=1;
	    Integer k;
	    for(String i:str){
	    	if(map.containsKey(i)){
	    		k = map.get(i);
	    		map.remove(i);
	    		map.put(i, ++k);
	    	}else{
	    		map.put(i,j);
	    	}
	        
	    }
		return map;
	}
	
	
	public static double[] calcDocMagTF(IndexReader r,TermEnum t) throws IOException{
		Date start = new Date();
		double[] dcmgntdTF = new double[r.maxDoc()];
		
		while(t.next())
		{
			TermDocs td = r.termDocs(t.term());
			while(td.next())
				dcmgntdTF[td.doc()]  = dcmgntdTF[td.doc()] + (double) Math.pow(td.freq(),2);

		}
		for (int j = 0; j < dcmgntdTF.length; j++){
			dcmgntdTF[j] = (double) Math.sqrt(dcmgntdTF[j]);
		}
		Date end = new Date();
		Long timetaken = end.getTime() - start.getTime();
		System.out.println( timetaken + " Total milliSeconds");
		return dcmgntdTF;
	}
	
	
	//calculating document magnitudes for tfidf
	public static double[] calcDocMagTFIDF(IndexReader r,TermEnum t,HashMap<String,Double> mapiDF,int doclimit) throws IOException{
		System.out.println("calculating tfidf");
		double[] dcmgntdTFIDF = new double[doclimit];
		double idf,tfidf;
		
		while(t.next())
		{
			idf = mapiDF.get(t.term().text());
			TermDocs td = r.termDocs(t.term());
			int count = 0;
			while(td.next())
			{
				if(td.doc()<doclimit)
				{
				count += 1;
				tfidf = td.freq()*idf;
				dcmgntdTFIDF[td.doc()]  = dcmgntdTFIDF[td.doc()] + Math.pow(tfidf,2);
				}
			}
		}
		
		for (int j = 0; j < dcmgntdTFIDF.length; j++){
			dcmgntdTFIDF[j] = (double) Math.sqrt(dcmgntdTFIDF[j]);
		}
		
		
		
		
		return dcmgntdTFIDF;
	}
	
	
	//calculating doc term index for clustering
public static HashMap<Integer, HashMap<String,Double>> calcDocTermMap(IndexReader r,TermEnum t,HashMap<String,Double> mapiDF,int doclimit) throws IOException{
		System.out.println("calculating index for clustering");
		//double[] dcmgntdTFIDF = new double[doclimit];
	HashMap<Integer, HashMap<String,Double>> docTermMap = new  HashMap<Integer, HashMap<String,Double>>();
		double idf,tfidf;
		int key;
		while(t.next())
		{
			idf = mapiDF.get(t.term().text());
			TermDocs td = r.termDocs(t.term());
			while(td.next())
			{
				tfidf = td.freq()*idf;
				key = td.doc();
				HashMap<String,Double> termMap = docTermMap.get(key);
				if(termMap == null){
					termMap = new HashMap<String,Double>();
					docTermMap.put(key, termMap);
				}
				termMap.put(t.term().text().toLowerCase(), tfidf);
			}
		}
		return docTermMap;
	}


//doing scalar clustering
public static Map<String,Double>  scalarCluster(HashMap<String,Integer> mapQuery,HashMap<Integer, HashMap<String,Double>> docTermMap,Map<Integer,Double> sortedMap,HashMap<String,Double> mapiDF){
	

	int numOfResults = sortedMap.size();
	int numOfDocs = (numOfResults<5)? numOfResults:5;
	int i=0;
	int index=0;
	
	HashMap<String,Integer> termIndex = new HashMap<String,Integer>();
	for (Entry<Integer, Double> entry : sortedMap.entrySet()) {
		if(i++<numOfDocs){
			HashMap<String,Double> docTerm = docTermMap.get(entry.getKey());
			for (Entry<String,Double> entry1 : docTerm.entrySet()) {
				if(mapiDF.containsKey(entry1.getKey()))
				if(!termIndex.containsKey(entry1.getKey()) && (mapiDF.get(entry1.getKey())>2 || mapQuery.containsKey(entry1.getKey())) ){
					termIndex.put(entry1.getKey(), index);
					index++;
				}
			}
		}
	}
	
	double[][] docTermMatrix = new double[numOfDocs][termIndex.size()];
	i=0;
	for (Entry<Integer, Double> entry : sortedMap.entrySet()) {
		if(i<numOfDocs){
			HashMap<String,Double> docTerm = docTermMap.get(entry.getKey());
			for (Entry<String,Double> entry1 : docTerm.entrySet()) {
				if(termIndex.containsKey(entry1.getKey())){
				int ind =termIndex.get(entry1.getKey());
				if(mapiDF.containsKey(entry1.getKey()) && mapiDF.get(entry1.getKey())!=0){
					docTermMatrix[i][ind] = entry1.getValue()/mapiDF.get(entry1.getKey());
					//System.out.println(mapiDF.get(entry1.getKey()));
				}
			}
			}
			i++;
		}
	}
	
	double[][] termDocMatrix = new double[termIndex.size()][numOfDocs];
	for(i=0;i<termIndex.size();i++){
		for(int j=0;j<numOfDocs;j++){
			termDocMatrix[i][j]=docTermMatrix[j][i];
		}
	}
	double temp=0;
	double[][] termTermMatrix = new double[termIndex.size()][termIndex.size()];
	for(i=0;i<termIndex.size();i++){
		for(int j=0;j<termIndex.size();j++){
			for(int k=0;k<numOfDocs;k++){
				temp  = temp +  termDocMatrix[i][k]*docTermMatrix[k][j];
			}
			termTermMatrix[i][j]=temp;
			temp=0;
		}
	}
	
	double[][] normAssociationMatrix = new double[termIndex.size()][termIndex.size()];
	for(i=0;i<termIndex.size();i++){
		for(int j=0;j<termIndex.size();j++){
			normAssociationMatrix[i][j] = termTermMatrix[i][j]/(termTermMatrix[i][i] + termTermMatrix[j][j] - termTermMatrix[i][j]);
		}
	}
	
	ArrayList<Integer> indexes = new ArrayList<Integer>();
	double[] scalarClusterMatrix = new double[termIndex.size()];
	for (Entry<String,Integer> entry1 : mapQuery.entrySet()) {
		//System.out.println(entry1.getKey());
		if(termIndex.containsKey(entry1.getKey())){
			indexes.add(termIndex.get(entry1.getKey()));
		}
	}
	double cosSimilarity;
	for(int k:indexes){
		for(i=0;i<termIndex.size();i++){
				double[] vec1=normAssociationMatrix[k];
				double[] vec2=normAssociationMatrix[i];
				if(indexes.contains(i)) 
					cosSimilarity=0;
				else
					cosSimilarity = calcCosSimilarity(vec1,vec2);
				scalarClusterMatrix[i] += cosSimilarity;	
	}
	}
	double max=0;
	int maxIndex=0;
	Map<Integer,Double> sortedScalarVector = new HashMap<Integer,Double>();
	for(i=0;i<scalarClusterMatrix.length;i++){
		sortedScalarVector.put(i,scalarClusterMatrix[i]);
	}
	sortedScalarVector = utilities.sortByComparator(sortedScalarVector);
	Map<Integer,Double> topIndexes = new HashMap<Integer,Double>();
	int count=0;
	for(Entry<Integer,Double> entry2 : sortedScalarVector.entrySet()) {
		if(count<10){
			topIndexes.put(entry2.getKey(),entry2.getValue());
			count++;
		}
	}
	Map<String,Double> elabQuery=new HashMap<String,Double>();
	for (Entry<String,Integer> entry2 : termIndex.entrySet()) {
		if(topIndexes.containsKey(entry2.getValue()) && entry2.getKey().length()>3){
			elabQuery.put(entry2.getKey(),mapiDF.get(entry2.getKey()));
		}
	}
	elabQuery = utilities.sortByComparator(elabQuery);
	return elabQuery;
}

//finding cosine similarity of a document with centroid
public static double calcCosSimilarity(double[] vec1,double[] vec2){
	double cosSimilarity=0;
	double mag1=0;
	double mag2=0;
	for(int i=0;i<vec1.length;i++){
		cosSimilarity  += vec1[i]*vec2[i];
		mag1 += Math.pow(vec1[i], 2);
		mag2 += Math.pow(vec2[i], 2);
	}
	
	cosSimilarity /= Math.sqrt(mag1)*Math.sqrt(mag2); 
	
	return cosSimilarity;
}

//doing clustering
public static HashMap<Integer, Cluster> clusterAnalysis(int numClusters,HashMap<Integer, HashMap<String,Double>> docTermMap,Map<Integer,Double> sortedMap,double[] dcmgntdTFIDF, HashMap<String,Double> mapiDF){
	
	ArrayList<Integer> seedDocID = new ArrayList<Integer>();
	
	HashMap<Integer, Cluster> clusters = new HashMap<Integer, Cluster>();
	int numOfResults = sortedMap.size();
	
	int numOfDocs = (numOfResults<50)? numOfResults:50;
	int i=0;
	int[] topDocsIds=new int[numOfDocs];
	for (Entry<Integer, Double> entry : sortedMap.entrySet()) {
		if(i<numOfDocs){
			topDocsIds[i++]=entry.getKey();
		}
	}
	i=0;
	int seedDoc;
	while(i<numClusters){
		seedDoc = 0 + (int)(Math.random()*numOfDocs);
		
		if(!seedDocID.contains(seedDoc)){
			seedDocID.add(seedDoc);
			
			Cluster c = new Cluster();
			c.document.add(topDocsIds[seedDoc]);
		//	c.document.add(topDocsIds[seedDoc]);
			c.centroid = docTermMap.get(topDocsIds[seedDoc]);
			//System.out.println(i);
			clusters.put(i, c);
			i++;
			//System.out.println(i);
		}
	}
	HashMap<String,Double> docVector;
	boolean isChange = true;
	int nearestCluster;
	int originalCluster;
	int iter=1;
	while(isChange){
		isChange=false;
		for(i =0;i<numOfDocs;i++){
			int docID = topDocsIds[i];
			docVector = docTermMap.get(docID);
			nearestCluster=findNearestCluster(docID,docVector,clusters,dcmgntdTFIDF,mapiDF);
			originalCluster=findOriginalCluster(docID,clusters);
			if(iter==1){
				if(!clusters.get(nearestCluster).document.contains(docID))
					clusters.get(nearestCluster).document.add(docID);
				//System.out.println("iteration 1" + docID + " -> " + nearestCluster);
				isChange=true;
			}
			else if(nearestCluster != originalCluster){
				
				int index = clusters.get(originalCluster).document.indexOf(docID);
				clusters.get(originalCluster).document.remove(index);
				clusters.get(nearestCluster).document.add(docID);
				//System.out.println(docID + ":" + originalCluster + " -> " + nearestCluster);
				isChange=true;
			}
		}
		clusters=calcNewCentroid(clusters,docTermMap);
		iter++;
	}
	
	return clusters;
}

//calculating new centroid of a cluster
public static HashMap<Integer, Cluster> calcNewCentroid( HashMap<Integer, Cluster> clusters,HashMap<Integer, HashMap<String,Double>> docTermMap ){
	HashMap<Integer, Cluster> Newclusters = new HashMap<Integer, Cluster>();
	for(int i=0;i<clusters.size();i++){
		HashMap<String,Double> newcentroid = new HashMap<String,Double>();
		Cluster c= clusters.get(i);
		ArrayList documents= clusters.get(i).document;
		for(int j=0;j<documents.size();j++){
			HashMap<String,Double> docVector = docTermMap.get(documents.get(j));
			for (Entry<String,Double> entry : docVector.entrySet()) {
				if(newcentroid.containsKey(entry.getKey())){
					newcentroid.put(entry.getKey(), (newcentroid.get(entry.getKey()) + entry.getValue()/documents.size()));
				}
				else{
					newcentroid.put(entry.getKey(), entry.getValue()/documents.size());
				}
			
			}
		}
		c.centroid = newcentroid;
		Newclusters.put(i, c);
	}
	return Newclusters;
}

//finding the original cluster of a document
public static int findOriginalCluster(int docID,HashMap<Integer, Cluster> clusters){
	int originalCluster=0;
	for(int i=0;i<clusters.size();i++){
		if(clusters.get(i).document.contains(docID)){
			originalCluster=i;
			break;
		}
	}
	return originalCluster;
}

//findinng the closest cluster of a document
public static int findNearestCluster(int docID,HashMap<String,Double> docVector,HashMap<Integer, Cluster> clusters,double[] dcmgntdTFIDF,HashMap<String,Double> mapiDF){
	int nearestClusterKey=0;
	double maxDisSimilarity=1000;
	double maxSimilarity=0;
	double docClusterSimilarity;
	double clusterMagnitude;
	for(int i=0;i<clusters.size();i++){
		docClusterSimilarity=0.0;
		
		Cluster Clustercentroid = clusters.get(i);
		clusterMagnitude =  findClusterCentroidMag(Clustercentroid);
		/*for (Entry<String,Double> entry : mapiDF.entrySet()) {
			if(Clustercentroid.centroid.containsKey(entry.getKey())){
				
					if(docVector.containsKey(entry.getKey()))
						docClusterSimilarity += Math.pow((docVector.get(entry.getKey()) - (Clustercentroid.centroid.get(entry.getKey()))),2);
					else
						docClusterSimilarity += Math.pow((0 - (Clustercentroid.centroid.get(entry.getKey()))),2);
					
					docClusterSimilarity= docClusterSimilarity/(clusterMagnitude * dcmgntdTFIDF[docID]);
				}
				else if(docVector.containsKey(entry.getKey())){
						docClusterSimilarity += Math.pow((docVector.get(entry.getKey()) - 0),2);
						docClusterSimilarity= docClusterSimilarity/(clusterMagnitude * dcmgntdTFIDF[docID]);
				}
		}

			
		if(docClusterSimilarity<maxDisSimilarity){
			nearestClusterKey=i;
			maxDisSimilarity=docClusterSimilarity;
		}*/
		
		for(Entry<String,Double> entry : docVector.entrySet()){
			if(Clustercentroid.centroid.containsKey(entry.getKey())){
				docClusterSimilarity += entry.getValue()*Clustercentroid.centroid.get(entry.getKey());
			}
		}
		docClusterSimilarity /= clusterMagnitude*dcmgntdTFIDF[docID];
		if(docClusterSimilarity > maxSimilarity){
			maxSimilarity=docClusterSimilarity;
			nearestClusterKey=i;
		}
	}
	return nearestClusterKey;
}

//finding the magnitude of a ceentroid
public static double findClusterCentroidMag(Cluster Clustercentroid)
{
	double clusterMagnitude=0.0;
	HashMap<String,Double> centroid = Clustercentroid.centroid;
	for (Entry<String,Double> entry : centroid.entrySet())
		clusterMagnitude+= Math.pow(entry.getValue(),2);
	clusterMagnitude= Math.sqrt(clusterMagnitude);
	
	return clusterMagnitude;
}
	//calcualting idf of all terms
	public static HashMap<String,Double> calcTermIDF(IndexReader r,TermEnum t,int doclimit) throws IOException{
		HashMap<String,Double> mapiDF = new HashMap<>();
		double docCount = 0;
		double N= doclimit;
		int i=0;
		while(t.next())
		{
			docCount = 0;
			//System.out.println(i++);
			
			Term te = new Term("contents", t.term().text());
			TermDocs td = r.termDocs(te);
			while(td.next())
			{if(td.doc() < doclimit)
				docCount++;
			}
			//docCount = r.docFreq(te);
			if(docCount == 0)
				mapiDF.put(t.term().text(),0.0);
			else
				mapiDF.put(t.term().text(), Math.log(N/docCount));
		}
		
		return mapiDF;
	}
	
	//Authority hubs calculation
	public static int[][] calcAuthHub(Map<Integer,Double> sortedMap,int k,LinkAnalysis l){
		
		Date querystart = new Date();
		int[] link;
		int[] citation;
		ArrayList<Integer> baseSet = new ArrayList<Integer>();
		int count = 0;
		
		//Calculating Base Set
		for (Entry<Integer, Double> entry : sortedMap.entrySet()) {
			//Condition for size of root set
			if(++count > k) break;
			
			if(!baseSet.contains(entry.getKey()))
				baseSet.add(entry.getKey());
			link = l.getLinks(entry.getKey());
			citation = l.getCitations(entry.getKey());
			for(int i=0;i<link.length;i++)
				if(!baseSet.contains(link[i]))
					baseSet.add(link[i]);
			for(int i=0;i<citation.length;i++)
				if(!baseSet.contains(citation[i]))
					baseSet.add(citation[i]);
			}
		//System.out.println("size of base set" + baseSet.size());
		Date queryend = new Date();
		Long timetaken = queryend.getTime() - querystart.getTime();
	//	System.out.println( (double) timetaken + " Calculation of Base Set");
		
		//Calculating adjacency matrix to get authority and hub vectors
		int[][] adjAuthHub = new int[baseSet.size()][baseSet.size()];
		for(int i=0;i<baseSet.size();i++)
		{
			link = l.getLinks(baseSet.get(i));
			for(int j=0;j<link.length;j++)
				if(baseSet.contains(link[j]))
						adjAuthHub[i][baseSet.indexOf(link[j])] = 1;
		}
		
		//Calculating transpose of the above adj matrix
		int[][] transAdjAuthHub = new int[baseSet.size()][baseSet.size()];
		for(int i=0;i<baseSet.size();i++)
			for(int j=0;j<baseSet.size();j++)
				transAdjAuthHub[i][j] = adjAuthHub[j][i];
		
		float[] vectorAuth = new float[baseSet.size()];
		
		Arrays.fill(vectorAuth, (float)1/baseSet.size());
		float[] vectorHub = new float[baseSet.size()];
		
		Arrays.fill(vectorHub, (float)1/baseSet.size());
		int[][] vectorAuthHub = new int[baseSet.size()][2];
		float epsilon = (float) 0.001;
		
		float[][] ATA =  new float[baseSet.size()][baseSet.size()];
		float[][] AAT =  new float[baseSet.size()][baseSet.size()];
		float temp=0;
		
		//Calculating A*A`
		for(int i=0;i<baseSet.size();i++){
			for(int j=0;j<baseSet.size();j++){
				for(int m=0;m<baseSet.size();m++){
					temp  = temp +  adjAuthHub[i][m]*transAdjAuthHub[m][j];
				}
				AAT[i][j]=temp;
				temp=0;
			}
		}
		
		//Calculating A`*A
		for(int i=0;i<baseSet.size();i++){
			for(int j=0;j<baseSet.size();j++){
				for(int m=0;m<baseSet.size();m++){
					temp  = temp +  transAdjAuthHub[i][m]*adjAuthHub[m][j];
				}
				ATA[i][j]=temp;
				temp=0;
			}
		}
		
		
		Date queryend1 = new Date();
		timetaken = queryend1.getTime() - queryend.getTime();
		//System.out.println( (double) timetaken + " Calculation of Adjacency Matrices A, A', AA', A'A");
		
		//Calculating Authority Vector
		boolean flag = true;
		temp=0;
		float mag = 0;
		while(flag){
			float[] newvectorAuth = new float[baseSet.size()];
			for(int i=0;i<baseSet.size();i++){
				
					
					for(int m=0;m<baseSet.size();m++){
						temp = temp +ATA[i][m]*vectorAuth[m];
					}
					newvectorAuth[i] = 	temp;					
					//vectorAuthHub[i][0] = temp;
					mag += Math.pow(temp, 2);
					temp=0;
					/*if(Math.abs(newvectorAuth[i] - vectorAuth[i]) > epsilon){
						flag = flag | true;
					}*/
						
				}
			//Normalizing Authority Vector
			for(int i=0;i<newvectorAuth.length;i++)
			{
				newvectorAuth[i]=newvectorAuth[i]/(float)Math.sqrt(mag);
				//vectorAuthHub[i][1]=vectorAuthHub[i][1]/(float)Math.sqrt(mag);
			}
			for(int i=0;i<baseSet.size();i++){
				if(Math.abs(newvectorAuth[i] - vectorAuth[i]) > epsilon){
					flag = flag | true;
				}
			}
					
			/*for(int i=0;i<vectorAuth.length;i++)
			{
				vectorAuth[i]=vectorAuth[i]/(float)Math.sqrt(mag);
				//vectorAuthHub[i][0]=vectorAuthHub[i][0]/(float)Math.sqrt(mag);
			}*/
			vectorAuth=newvectorAuth;
			flag = false;
			mag=0;
			
		}
		
		//Calculating Hub Vector
		temp=0;
		flag = true;
		while(flag){
			float[] newvectorHub = new float[baseSet.size()];
			for(int i=0;i<baseSet.size();i++){
				flag = false;
					for(int m=0;m<baseSet.size();m++){
						temp = temp +AAT[i][m]*vectorHub[m];
					}
					newvectorHub[i] = 	temp;
					//vectorAuthHub[i][1] = temp;
					mag += Math.pow(temp, 2);
					temp=0;
					/*if(Math.abs(newvectorHub[i] - vectorHub[i]) > epsilon){
						flag = flag | true;
					}*/
						
				}
			//Normalizing hub Vector
			for(int i=0;i<vectorHub.length;i++)
			{
				newvectorHub[i]=newvectorHub[i]/(float)Math.sqrt(mag);
				//vectorAuthHub[i][1]=vectorAuthHub[i][1]/(float)Math.sqrt(mag);
			}
			for(int i=0;i<baseSet.size();i++){
				if(Math.abs(newvectorHub[i] - vectorHub[i]) > epsilon){
					flag = flag | true;
				}
			/*for(i=0;i<vectorHub.length;i++)
			{
				vectorHub[i]=vectorHub[i]/(float)Math.sqrt(mag);
				//vectorAuthHub[i][1]=vectorAuthHub[i][1]/(float)Math.sqrt(mag);
			}*/
			mag=0;
			vectorHub=newvectorHub;
			flag = false;
			
		}
		}
		
		//Sorting Authority and Hub Vectors
		Date queryend2 = new Date();
		timetaken = queryend2.getTime() - queryend1.getTime();
	//	System.out.println( (double) timetaken + " Calculation of Hub and Authority vectors");
		ArrayList<Data> listAuthHubDocId = new ArrayList<Data>();
		for(int i=0;i<vectorHub.length;i++)
		{		
			listAuthHubDocId.add(new Data(vectorAuth[i], vectorHub[i], baseSet.get(i)));
		}
		Collections.sort(listAuthHubDocId, new Comparator<Data>(){
			public int compare(Data d1, Data d2)
			{
				return Float.compare(d2.Auth, d1.Auth);
			}
		});
		
		for(int i=0;i<listAuthHubDocId.size();i++)
		{
			vectorAuthHub[i][0] = listAuthHubDocId.get(i).getDocID();
		}
		
		Collections.sort(listAuthHubDocId, new Comparator<Data>(){
			public int compare(Data d1, Data d2)
			{
				return Float.compare(d2.Hub, d1.Hub);
			}
		});
		
		for(int i=0;i<listAuthHubDocId.size();i++)
		{
			vectorAuthHub[i][1] = listAuthHubDocId.get(i).getDocID();
		}
		Date queryend3 = new Date();
		timetaken = queryend3.getTime() - queryend2.getTime();
		//System.out.println( (double) timetaken + " Sorting the vectors to get top results");
	return vectorAuthHub;
	}
	
	//page rank calcualtion
	public static double[] calcPageRank(int doclimit,LinkAnalysis l,double w1){
		//doclimit=4;
		double[] pageRank = new double[doclimit];
		Arrays.fill(pageRank, (double)1/doclimit);
		int[] link;
		int[] citation;
		double epsilon = 0.001;
		boolean flag = true;
		double temp=0;
		int citSize;
		int linkSize;
		ArrayList<Integer> cit;
		//double w;
		//Scanner sc = new Scanner(System.in);
		
		//System.out.print("Enter The Random Surfer Probability for Sink Nodes> ");
		//w = Double.parseDouble(sc.nextLine());
		int iterations=0;
		while(flag)
		{
			System.out.println(iterations++);
			double[] newPageRank = new double[doclimit];
			flag = false;
			for(int j=0;j<doclimit;j++)
				{
						
						citation = l.getCitations(j);
						citSize = citation.length;
						if(citSize != 0){
							cit = new ArrayList<Integer>();
							for(int z=0;z<citSize;z++){
								cit.add(citation[z]);
							}
							for(int n=0;n<doclimit;n++){
								linkSize = l.getLinks(n).length;
								if(cit.contains(n))
									temp +=  pageRank[n]*(w1/(double)linkSize + (1.0-w1)/(double)doclimit);
								else
									if(linkSize == 0)
										temp +=  pageRank[n]*(1.0/(double)doclimit);
									else
										temp+=pageRank[n]*(1.0-w1)/(double)doclimit;
							}
						}
						else
							temp=0;
						
						/*temp = w1*temp + ((1-w1)*temp2/doclimit);*/
						
						newPageRank[j] = temp;
						temp=0;
						if(Math.abs(newPageRank[j] - pageRank[j]) > epsilon)
							flag = flag | true;					
				}
			pageRank = newPageRank;
		}
		HashMap<Integer,Double> pagerankhash=new HashMap<>();
		int i=0;
		for(Double j:pageRank){
			pagerankhash.put(i++,j);
		}
		Map<Integer,Double> sortedpageRank = utilities.sortByComparatorPR(pagerankhash);
		double min;
		double max;
		
		max = sortedpageRank.get(9048);
		min = 0.0;
		
		
		
		i=0;
		for (int j=0;j<pageRank.length;j++) {
			pageRank[j] = (pageRank[j] - min)/(max-min);
			
		}
		return pageRank;
		
		/*PrintStream console = System.out;
		File file = new File("pagerank.txt");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
		
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		utilities.printMap(sortedpageRank,"Nitesh");*/
		//System.setOut(console);
		//System.out.print("Enter The Page Rank Weight> ");
		//w = Double.parseDouble(sc.nextLine());
		
		//Normalize Page rank
		/*double pageRankMag=0;
		for(i=0;i<pageRank.length;i++){
			pageRankMag += Math.pow(pageRank[i], 2);
		}
		pageRankMag = Math.sqrt(pageRankMag);*/
		//Combine Vector Space
/*		for(int i=0;i<pageRank.length;i++){
			pageRank[i] = w*pageRank[i]/pageRankMag;
			if(sortedMap.containsKey(i))
				pageRank[i] = pageRank[i] + (1-w)*sortedMap.get(i);  
			
		}
*/		
	}
}
	

class Data {

	float Auth;
	float Hub;
	int DocID;
	
	public Data(float a, float h, int d){

		Auth = a;
		Hub = h;
		DocID = d;
    }

	public int getDocID() {
		// TODO Auto-generated method stub
		return this.DocID;
	}

	public float getHub() {
		// TODO Auto-generated method stub
		return this.Hub;
	}

	public float getAuth() {
		// TODO Auto-generated method stub
		return this.Auth;
	}
}


