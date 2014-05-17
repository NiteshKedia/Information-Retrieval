package com.ir.nitesh.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import com.ir.process.*;



import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;

@SuppressWarnings("unchecked")
public class SearchIR extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private HashMap<String,Double> mapiDF;
	private Map<Integer,Double> sortedIDF;
	private double[] pageRank;
	private IndexReader r;
	private int doclimit;
	private double[] dcmgntdTFIDF;
	private Map<Integer,Double> sortedMap;
	private LinkAnalysis l;
	private HashMap<Integer, HashMap<String,Double>> docTermMap;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException{
			
			}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		//System.out.println("----- InsertCustomerServlet -----");
		try {
			Date start = new Date();
		String Query=request.getParameter("query");
		double queryMag=0;
		//System.out.println(Query);
		int numOfResults;
		String[] query = Query.split(" ");
		HashMap<String,Integer> map = new HashMap();
		double[] docSimilarity = new double[doclimit];
		map = utilities.getUniqueCount(query);
		//System.out.println("query processed");
		for (Entry<String, Integer> entry : map.entrySet()) {
			queryMag = queryMag + entry.getValue()*entry.getValue();
			Term te = new Term("contents", entry.getKey());
			try {
				TermDocs td = r.termDocs(te);
				double N= doclimit;
				td = r.termDocs(te);
				while(td.next()){
					if(td.doc() < doclimit)
						docSimilarity[td.doc()] = docSimilarity[td.doc()] + td.freq() *mapiDF.get(entry.getKey());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("TFIDF calculated");
			double N= doclimit;
		}
		for(int i =0 ; i < docSimilarity.length; i++){
			if(docSimilarity[i] != 0){
				docSimilarity[i] = docSimilarity[i]/(dcmgntdTFIDF[i]*Math.sqrt(queryMag));
			}
		}
		int i=0;
		HashMap<Integer,Double> Similarity=new HashMap<>();
		for(Double j:docSimilarity){
			Similarity.put(i++,j);
		}
		sortedMap = utilities.sortByComparator(Similarity);
		numOfResults = sortedMap.size();
		
		
		RequestDispatcher dispatcher=request.getRequestDispatcher("/Index.jsp");
		
		String radioOption = request.getParameter("option");
		
		//Vector Similarity
		if("VS".equals(radioOption)){
			//System.out.println("m here too");
			ArrayList<DisplayObject> displayObjectList = new ArrayList<DisplayObject>();
			int numClusters=3;
		//	String[][] topCluster = new String[3][3];
			int countCluster0=0,countCluster1=0,countCluster2=0;
			HashMap<Integer, Cluster> clusters= utilities.clusterAnalysis(numClusters,docTermMap,sortedMap,dcmgntdTFIDF,mapiDF);
			for(i=0;i<clusters.size();i++){
				Cluster c= clusters.get(i);
				ArrayList documents= clusters.get(i).document;
				//System.out.print("Cluster "+ i +": ");
				for(int j=0;j<documents.size();j++){
					//System.out.print(documents.get(j) + ",");
				}
				//System.out.println("");
			}
			
			String[][] topCluster = new String[numClusters][3];
			
			int[] countVec = new int[numClusters];
			int count=0;
			for (Entry<Integer, Double> entry : sortedMap.entrySet()) {
				if(count++==50 || count++==numOfResults)
					break;
				for(i=0;i<numClusters;i++){
					if(clusters.get(i).document.contains(entry.getKey()) && countVec[i]<3){
						Document d = r.document(entry.getKey());
						String url = d.getFieldable("path").stringValue();
						String url1 = url.replace("%%", "/");
						topCluster[i][countVec[i]] = url1;
						countVec[i]++;
					}
				}
			}
			
			//cluster summary
			ArrayList<Set<String>> topClusterWords = new  ArrayList<>();
			for(i=0;i<clusters.size();i++){
				Cluster c= clusters.get(i);
				ArrayList documents= clusters.get(i).document;
				//System.out.println("");
				Map<String,Double> centroid = c.centroid;
				centroid=utilities.sortByComparator(centroid);
				count=0;
				Set<String> s =  new TreeSet<String>();
				for (Entry<String,Double> entry : centroid.entrySet()) {
					if(mapiDF.containsKey(entry.getKey()) && mapiDF.get(entry.getKey()) >2 && entry.getKey().matches("[a-zA-Z]+") && count++ <10)
					s.add(entry.getKey());
				}
				topClusterWords.add(s);
			}
			ArrayList<Set<String>> newListSet = new ArrayList<>();
			for(i=0;i<3;i++){
				Set<String> x = new TreeSet<String>(topClusterWords.get(i));
				x.removeAll(topClusterWords.get((i+1)%3));
				x.removeAll(topClusterWords.get((i+2)%3));
				newListSet.add(x);
			}
			request.setAttribute("clusterDescription",newListSet);
			request.setAttribute("clusters", topCluster);
			for(i=0;i<3;i++){
				//System.out.print("Cluster"+i+ ": ");
				for(int j=0;j<3;j++){
					//System.out.print(topCluster[i][j] + ",");
				}
				//System.out.println(" ");
			}
			
			//scalar clustering
			Map<String,Double>  elabQ = utilities.scalarCluster(map, docTermMap,sortedMap,mapiDF);
			//System.out.println(elabQuery);
			ArrayList<String> elabQuery = new ArrayList<String>();
			count=0;
			for (Entry<String,Double> entry : elabQ.entrySet()) {
				if(count<5){
					elabQuery.add(Query+ " " + entry.getKey());
					//System.out.println(Query+ " " + entry.getKey());
				}
			}
			
			
			request.setAttribute("elaboratedQuery", elabQuery);
			i=0;
			
			String folder = "C:\\Users\\Nitesh\\Google Drive\\2nd Sem Courses\\IR\\irs13\\Projectclass\\result3\\";
			
			//snippets
			for (Entry<Integer, Double> entry : sortedMap.entrySet()) {
					if(++i<10){
					Document d = r.document(entry.getKey());
					String url = d.getFieldable("path").stringValue();
					org.jsoup.nodes.Document doc = Jsoup.parse(new File(folder+url), "UTF-8");
					org.jsoup.select.Elements content = doc.select("title");
					org.jsoup.select.Elements snippet1 = doc.select("li");
					org.jsoup.select.Elements snippet2 = doc.select("p");
					String snippettext1 = snippet1.text();
					String snippettext2 = snippet2.text();
					String snippettext = snippettext1 +"."+snippettext2;
					String[] snippetlist=snippettext.split("\\.");
					//System.out.println(snippetlist.length);
					String linkTitle = content.text();
					String url1 = url.replace("%%", "%25%25");
					String url2 = url.replace("%%", "/");
					String link="result3/"+url1;
					

					DisplayObject dd = new DisplayObject();
					dd.link=link;
					//System.out.println(link);
					if(linkTitle != null)
						dd.url=url2;
						int maxcount=1;
						dd.linkTitle=linkTitle;
						for(int k=0;k<snippetlist.length;k++){
							count=0;
							
							for(int j=0;j<query.length;j++){
								//System.out.println(query[j]);
								if(snippetlist[k].toLowerCase().contains(query[j]))
									count++;
								if(count >=maxcount){
									//System.out.println("inside");
									dd.snippet=snippetlist[k];
									//System.out.println(snippetlist[k]);
									maxcount=count;
									//System.out.println(maxcount);
								}
									
								//}
						}
						}
						//System.out.println(dd);
						//System.out.println("======================================");
						displayObjectList.add(dd);
										
					//System.out.println(entry.getKey()+":"+entry.getValue());
					}
			}
			Date end = new Date();
			Long timetaken = end.getTime() - start.getTime();
			request.setAttribute("TimeTaken", timetaken);
			request.setAttribute("NumResults", numOfResults);
			request.setAttribute("TFIDF", displayObjectList);
			
			//request.setAttribute("TFIDF", sortedMap);
		}
		
		//page rank
		else if("PR".equals(radioOption)){
			 double w1 = Double.parseDouble(request.getParameter("rsprob"));
			//pageRank = utilities.calcPageRank(doclimit,l,w1);
			double w2=Double.parseDouble(request.getParameter("prweight"));
			for (Entry<Integer, Double> entry : sortedMap.entrySet()) {
				
				entry.setValue(w2*pageRank[entry.getKey()] + (1-w2)*entry.getValue());
			}
			sortedMap = utilities.sortByComparatorPR(sortedMap);
			request.setAttribute("PAGERANK", sortedMap);
		}
		
		//Authority hubs
		else{
			int sizeRootSet = 10;
			int temp;
			if (request.getParameter("rootset") != null)
			{
				temp = Integer.parseInt(request.getParameter("rootset"));
				sizeRootSet = (numOfResults > temp)?temp:numOfResults;
			}
			int[][] vectorAuthHub = utilities.calcAuthHub(sortedMap, sizeRootSet,l);
			
			request.setAttribute("AUTHHUB", vectorAuthHub);
			
			
			
			
			
		}
		dispatcher.forward(request, response);
		}catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void init() throws ServletException {  
        System.out.println("In the init() Method");  
        
        dcmgntdTFIDF = new double[doclimit];
        double w1 = 0.8;
        doclimit = 25054;
        
        try {
			r = IndexReader.open(FSDirectory.open(new File("C:\\Users\\Nitesh\\workspace\\WebAppIR\\index")));
			TermEnum t = r.terms();
			
			//calcualting IDF
			mapiDF =utilities.calcTermIDF(r,t,doclimit);
			sortedIDF = utilities.sortByComparatorasc(mapiDF);
			t = r.terms();
			
			//calculating document mangnitude
			dcmgntdTFIDF = utilities.calcDocMagTFIDF(r,t,mapiDF,doclimit);
			t = r.terms();
			
			//calculating doc term index for clustering and scalar cluster
			docTermMap = utilities.calcDocTermMap(r, t, mapiDF, doclimit);
			//System.out.println("IDF Calculated");
			l = new LinkAnalysis();
			//System.out.println("link done");
			
			//page rank
			pageRank = utilities.calcPageRank(doclimit,l,w1);
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //System.out.println("m here");
        return;
    } 
	 
	        
    }




