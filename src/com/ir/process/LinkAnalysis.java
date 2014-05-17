package com.ir.process;

import java.io.*;

public class LinkAnalysis {

	//please set the correct path of the file to run the program
	public static final String linksFile = "C:\\Users\\Nitesh\\workspace\\WebAppIR\\IntLinks.txt";
	public static final String citationsFile = "C:\\Users\\Nitesh\\workspace\\WebAppIR\\IntCitations.txt";
	public static int numDocs = 25054;

	private int[][] links;
	private int[][] citations;
	
	public LinkAnalysis()
	{
		try
		{
			links = new int[numDocs][];
			BufferedReader br = new BufferedReader(new FileReader(linksFile));
			String s = "";
			while ((s = br.readLine())!=null)
			{
				String[] words = s.split("->"); // split the src->dest1,dest2,dest3 string
				int src = Integer.parseInt(words[0]);
				if (words.length > 1 && words[1].length() > 0)
				{
					String[] dest = words[1].split(",");
					links[src] = new int[dest.length];
					for (int i=0; i<dest.length; i++)
					{
						links[src][i] = Integer.parseInt(dest[i]);
					}
				}
				else
				{
					links[src] = new int[0];
				}
			}
			br.close();
			
			citations = new int[numDocs][];
			br = new BufferedReader(new FileReader(citationsFile));
			s = "";
			while ((s = br.readLine())!=null)
			{
				String[] words = s.split("->"); // split the src->dest1,dest2,dest3 string
				int src = Integer.parseInt(words[0]);
				if (words.length > 1 && words[1].length() > 0)
				{
					String[] dest = words[1].split(",");
					citations[src] = new int[dest.length];
					for (int i=0; i<dest.length; i++)
					{
						citations[src][i] = Integer.parseInt(dest[i]);
					}
				}
				else
				{
					citations[src] = new int[0];
				}

			}
			br.close();
		}
		catch(NumberFormatException e)
		{
			System.err.println("links file is corrupt: ");
			e.printStackTrace();			
		}
		catch(IOException e)
		{
			System.err.println("Failed to open links file: ");
			e.printStackTrace();
		}
	}
	
	public int[] getLinks(int docNumber)
	{
		return links[docNumber];
	}
	
	public int[] getCitations(int docNumber)
	{
		return citations[docNumber];
	}
	
	public static void main(String[] args)
	{
		LinkAnalysis.numDocs = 25054;
		LinkAnalysis l = new LinkAnalysis();
		
		// Find all the document numbers that doc #3 points to
		System.out.print("Document number 3 points to: ");
		int[] links3 = l.getLinks(3);
		for(int pb:links3)
		{
			System.out.print(pb + ",");
		}
		
		// Find all the document numbers that point to doc #3
		System.out.print("\nDocument number 3 is pointed by: ");
		int[] cit3 = l.getCitations(3);
		for(int pb:cit3)
		{
			System.out.print(pb + ",");
		}
	}
}
