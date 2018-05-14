package com.IRWS.Group7.LuceneNewsArticles;

/**
 * This is a model for query object  
 *  @author IR Group 7, M.Sc. Students, TCD
 */

public class DocumentObject {
	
	private String id;
	private String header;
	private String text;

	public DocumentObject(String id, String header, String text) {
		this.id = id;
		this.header = header;
		this.text = text;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getHeader() {
		return header;
	}
	
	public String getText() {
		return text;
	}
}