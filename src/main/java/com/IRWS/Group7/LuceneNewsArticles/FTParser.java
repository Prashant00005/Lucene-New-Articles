package com.IRWS.Group7.LuceneNewsArticles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.IRWS.Group7.LuceneNewsArticles.FTParser.MyParserException;


public class FTParser {

	private String[] editions = {"fbis", "fr94", "ft", "latimes"};
//	private String[] editions = {"testEdition"};
	private ArrayList<ArrayList<File>> xmlFiles = new ArrayList<ArrayList<File>>();
	private Boolean verbose = false;

	public ArrayList<DocumentObject> documentObjectList = new ArrayList<DocumentObject>();

	/**
	 * Create a new instance of FTPaser. 
	 * Go through the dataPath directories and find all files.
	 * @param dataPath the root path containing the data files and directories.
	 */
	public FTParser(String dataPath) {
		// find all files for all news journals editions
		for(String path: this.editions) {
			ArrayList<File> files = getAllFiles(new File(Paths.get(dataPath, path).toString()));
			xmlFiles.add(files);
			if (this.verbose)
				System.out.println("Found " + files.size() + " files in path " + path);
		}
	}

	public void parseDocs() throws IOException {
		parseDocs(0);
		
	}
	/**
	 * Parse up to maxDocument using SAX and the ElementHandler
	 * @param maxDoc the maximum number of documents to parse
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException 
	 */
	public void parseDocs(int maxDoc) throws IOException{ 
		int numDoc = 0;
		SGMLHandler handler = new SGMLHandler(this.documentObjectList);
		System.out.println("Parsing documents: ");
		for(ArrayList<File> editionFiles: this.xmlFiles) {
			for(File sgmlFile: editionFiles) {
				handler.nbDocParsed = 0;
				handler.maxDocs = maxDoc - numDoc;
				try {
//					System.out.println("Parsing file: " + sgmlFile.getName());
					parseSGML(sgmlFile, handler);
				}catch (MyParserException e) {
					System.out.println("Max docs reached while parsing");
				}

				numDoc += handler.nbDocParsed;
//				System.out.printf("\rNB DOC parsed: %6d", numDoc);
				if(maxDoc != 0 && numDoc >= maxDoc)
					break;
			}
			if(maxDoc != 0 && numDoc >= maxDoc)
				break;
		}
		System.out.println("Done parsing documents.");
	}

	/**
	 * Recursively get all files in the provided curDir path
	 * @param curDir the File in which to look for other files
	 * @return
	 */
	private ArrayList<File> getAllFiles(File curDir) {
		ArrayList<File> files = new ArrayList<File>();

		if(!curDir.exists()) {
			System.out.println("Path does not exists: " + curDir.getAbsolutePath()); 
		}
		File[] filesList = curDir.listFiles();
		for(File f : filesList){
			if(f.isDirectory())
				files.addAll(getAllFiles(f));
			if(f.isFile()){
				// skip readme and 
				if(f.getName().startsWith("read"))
					continue;
				files.add(f);
				if(verbose)
					System.out.println(f.getName());
			}
		}
		return files;
	}


	class MyParserException extends Exception {
		private static final long serialVersionUID = 1323671638422114869L;
		public MyParserException(String string) {
			super(string);
		}
	}


	/**
	 * Function originally from https://stackoverflow.com/questions/4867894/sgml-parser-in-java
	 * design limitations:
	 * a tag must hold in one line i.e. you must have < and > in the line 
	 * @param sgmlFile
	 * @throws MyParserException 
	 * @throws IOException 
	 */
	void parseSGML(File sgmlFile, SGMLHandler handler) throws IOException, MyParserException{

		String line = null;
		boolean countStart = false;
		int headerTagsCounter = 0;
		int startOfTag;
		int endOfTag;

		String currentData = null;
		String currentTag = null;
		String tagArguments = null;
		
		int lineNb = 0;
		try(BufferedReader br = new BufferedReader(new FileReader(sgmlFile))) {
			while(br.ready() && !handler.stopParsing){
				line = br.readLine().trim();
				lineNb++;
				
				while(line.length() > 0) {
					// Find tag opening
					startOfTag = line.indexOf('<');
					if(startOfTag > -1){ // "LINE BEGINNING <TAG ARGUMENTS> REST OF THE LINE"
						// if tag is not at the beginning, parse text before the tag 
						if(startOfTag > 0) {
							currentData = line.substring(0, startOfTag).trim();
							if(currentData.length() > 0) {
								handler.characters(currentData);
							}
						}
						endOfTag = line.indexOf('>');
						if(endOfTag == -1) {
							throw new MyParserException("Missing closing tag > on line " + lineNb + " in file: " + sgmlFile.getName());
						}
						// extract tag content
						currentTag = line.substring(startOfTag + 1, endOfTag);
						// extract rest of the line
						currentData = line.substring(endOfTag + 1, line.length());

						// find arguments to the tag to split the name
						endOfTag = currentTag.indexOf(' ');
						if(endOfTag > 0 && endOfTag < currentTag.length()) {
							tagArguments = currentTag.substring(endOfTag + 1);
							currentTag = currentTag.substring(0, endOfTag);
						}else{
							tagArguments = new String("");
						}
						if(currentTag.length() > 0 && currentTag.charAt(0) == '/'){
							handler.endElement(currentTag.substring(1));
						}else {
							handler.startElement(currentTag, tagArguments);
						}
						line = currentData;
					}else {
						// no tag in the line
						handler.characters(line);
						line = "";
					}
				}
			}
		} 
	}

	public void showDocuments() {
		showDocuments(0);
	}

	public void showDocuments(int maxDoc) {
		int count = 0;
		
		for(DocumentObject doc: this.documentObjectList) {
			System.out.println("ID:");
			System.out.println(doc.getId());
			System.out.println("HEADER");
			System.out.println(doc.getHeader());
			System.out.println("TEXT");
			System.out.println(doc.getText());
			count++;
			if(maxDoc != 0 && count >= maxDoc) 
				break;
		}
	}
	
	private int currEdition = 0;
	private int currFile = -1;
	private ArrayList<DocumentObject> currentDocList = new ArrayList<>();
	private int currDocInFile = 0;
	
	public DocumentObject nextDocument() throws IOException {
		currDocInFile++;
		// in case of parsing all current docs or empty doc list, load next document list
		if(currentDocList == null || currDocInFile >= currentDocList.size()) {
			// read 1 doc
			// find next doc
			currFile++;
			if(currFile >= xmlFiles.get(currEdition).size()) {
				//change edition
				currFile = 0;
				currEdition++;
				if(currEdition >= xmlFiles.size()){
					// all editions processed
					System.out.println("returning null");
					return null;
				}
			}
			this.currentDocList.clear();
			SGMLHandler handler = new SGMLHandler(this.currentDocList);
			try {
				parseSGML(xmlFiles.get(currEdition).get(currFile), handler);
//				System.out.println("Parsing file: " + xmlFiles.get(currEdition).get(currFile));
//				System.out.println("Found nbdoc: " + currentDocList.size());
				currDocInFile = 0;
			}catch (MyParserException e) {
				System.out.println("Max docs reached while parsing: " + e.getMessage());
			}
		}
		return currentDocList.get(currDocInFile);
	};
}