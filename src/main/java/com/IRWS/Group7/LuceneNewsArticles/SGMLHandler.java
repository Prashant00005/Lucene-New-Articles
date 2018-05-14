package com.IRWS.Group7.LuceneNewsArticles;

import java.util.ArrayList;

class SGMLHandler {

	public ArrayList<DocumentObject> documentObjectList = null;
	boolean bDoc = false;
	boolean bDocNo = false;
	boolean bHeader = false;
	boolean bText = false;

	String docNo = null;
	String header = null;
	String text = null;

	public int nbDocParsed = 0;
	public int maxDocs = 0;
	public boolean stopParsing = false;

	public SGMLHandler(ArrayList<DocumentObject> list) {
		this.documentObjectList = list;
		this.maxDocs = 0;
	}

	public void startElement(String qName, String attributes) {
//		System.out.println("Start Element: " + qName);
		if (qName.equalsIgnoreCase("doc")) {
			bDoc = true;
			docNo = "";
			header = "";
			text = "";
		}else if(qName.equalsIgnoreCase("docno")) {
			bDocNo = true;
		}else if(qName.equalsIgnoreCase("header")) {
			bHeader = true;
		}else if(qName.equalsIgnoreCase("text")) {
			bText = true;
		}else if(qName.equalsIgnoreCase("!--")) {
		}else {
//			System.out.println("Tag: \"" + qName + "\" with attribute \"" + attributes + "\" is ignored.");
		}
	}

	public void endElement(String qName) {
//		System.out.println("End Element: " + qName);
		if (qName.equalsIgnoreCase("doc")) {
			this.documentObjectList.add(
					new DocumentObject(this.docNo, this.header, this.text));
			this.bDoc = false;
			this.nbDocParsed++;
			if(this.maxDocs > 0 && this.nbDocParsed >= this.maxDocs)
				this.stopParsing = true;
		}else if (qName.equalsIgnoreCase("header")) {
			this.bHeader = false;
		}
	}

	public void characters(String content){
		if(bDocNo){
			docNo += content;
//			System.out.println("Doc No: " + content);
			bDocNo = false;
		}else if(bHeader){
			header += content + " ";
//			System.out.println("Header: " + content);
		}else if(bText){
			text += content + " ";
//			System.out.println("Text: " + content);
		}else{
//			System.out.println("skipped chars: " + content);
		}
	}

}
