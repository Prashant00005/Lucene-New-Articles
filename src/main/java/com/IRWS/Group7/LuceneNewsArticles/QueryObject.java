package com.IRWS.Group7.LuceneNewsArticles;

/**
 * This is a model for query object  
 *  @author IR Group 7, M.Sc. Students, TCD
 */

public class QueryObject {
	
	private int queryNumber;
	private String queryTitle;
	private String queryDescription;
	private String queryNarrative;
	public int getQueryNumber() {
		return queryNumber;
	}
	public void setQueryNumber(int queryNumber) {
		this.queryNumber = queryNumber;
	}
	public String getQueryTitle() {
		return queryTitle;
	}
	public void setQueryTitle(String queryTitle) {
		this.queryTitle = queryTitle;
	}
	public String getQueryDescription() {
		return queryDescription;
	}
	public void setQueryDescription(String queryDescription) {
		this.queryDescription = queryDescription;
	}
	public String getQueryNarrative() {
		return queryNarrative;
	}
	public void setQueryNarrative(String queryNarrative) {
		this.queryNarrative = queryNarrative;
	}
}

