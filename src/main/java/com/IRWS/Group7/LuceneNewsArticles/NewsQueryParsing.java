package com.IRWS.Group7.LuceneNewsArticles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



/**
 * This class parses news queries from file  
 * @author IR Group 7, M.Sc. Students, TCD
 */
public class NewsQueryParsing {

	public ArrayList<QueryObject> queryObjectList = new ArrayList<QueryObject>();
	boolean bDescription = false;
	boolean bNarrative = false;
	int queryNumber = 0;
	String queryTitle = "";
	String queryDescription = "";
	String queryNarrative = "";
	BufferedReader br;

	 /**
     * read news queries and update the objects list with the queries
     * @param query file path
     */
	
	public NewsQueryParsing(String queryPath)  
	{
		try {
		String line = "";
		StringBuilder sb = new StringBuilder();
		QueryObject queryObject ;
		
		 br = new BufferedReader(new FileReader(new File(queryPath)));
		
            while((line = br.readLine()) != null){
                if(line.startsWith("<top>") || line.isEmpty()){
                	bDescription = false;   
                	bNarrative = false;
                  continue;
                }
                else  if(line.startsWith("<num>")){
                	queryNumber = Integer.parseInt(line.substring(14, 17));
                	
                  }
                else  if(line.startsWith("<title>")){
                	queryTitle = line.substring(8, line.length()-1);
                	
                  }
                else if(line.startsWith("<desc>")){
                	bDescription = true;                              	
                  }
               
                else if(line.startsWith("<narr>")){
                	bDescription = false;   
                	bNarrative = true;                              	
                  }
                
                else if(line.startsWith("</top>")){
                	queryObject = new QueryObject();
                	bDescription = false;   
                	bNarrative = false;
                	queryDescription = 	queryDescription.substring(1);
                	queryNarrative = 	queryNarrative.substring(1);
                	queryObject.setQueryNumber(queryNumber);
                	queryObject.setQueryTitle(queryTitle);
                	queryObject.setQueryDescription(queryDescription);
                	queryObject.setQueryNarrative(queryNarrative);
                	queryObjectList.add(queryObject);
                	queryObject = null;
                	queryDescription = "";
                	queryNarrative = "";
                }
                else if(bDescription == true && !line.isEmpty()){
                	queryDescription += " " + line;                     	
                	
                  }
                else if(bNarrative == true && !line.isEmpty()){
                	
                	queryNarrative += " " + line;                     	
                  }     
            }

           } catch (Exception ex) {
             ex.printStackTrace();
             
           }
           finally {
        	   try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
                  

           }
	}
	
	 /**
     * print all the queries
     */
	public void showQueries()
	{
		showQueries(0);
	}

	public void showQueries(int maxQ) {
		// TODO Auto-generated method stub
		int count = 0;
		for (QueryObject queryObject : this.queryObjectList) {
			System.out.println(queryObject.getQueryNumber());
			System.out.println(queryObject.getQueryTitle());
			System.out.println(queryObject.getQueryDescription());
			System.out.println(queryObject.getQueryNarrative());
			System.out.println("\n");
			count++;
			if(maxQ != 0 && count >= maxQ)
				break;
		}
	}
	
	

}

