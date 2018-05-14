package com.IRWS.Group7.LuceneNewsArticles;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.min;

public class QuerySearcher {

    private final TrecWriter tWriter;
    private QueryBuilder queryBuilder;
    private final Float contentBoost;
    private Directory directory;
    private DirectoryReader ireader;
    private IndexSearcher isearcher;
    private double sumPrecision = 0., sumRecall=0.;
    private int nbQueries = 0;
    private NewsQueryParsing newsQP;
    public QuerySearcher(String indexPath, QueryBuilder queryBuilder, String output_results,
                         Similarity similarity, Float contentBoost) throws IOException {
        directory = FSDirectory.open(Paths.get(indexPath));
        ireader = DirectoryReader.open(directory);
        isearcher = new IndexSearcher(ireader);
        isearcher.setSimilarity(similarity);
        this.queryBuilder = queryBuilder;
        this.contentBoost = contentBoost;

        this.tWriter = new TrecWriter(output_results);
    }

    public void search(QueryObject q, boolean debug) throws IOException, ParseException {
        if (debug)
            System.out.println("Searching for: " + q);


        String queryTxt = "  " + q.getQueryDescription()  + " " +  q.getQueryTitle() ;
        
        queryTxt = queryStemming(queryTxt);
        System.out.print(queryTxt + "\n");
        Query query1 = queryBuilder.createBooleanQuery("title", queryTxt);
        Query query2 = queryBuilder.createBooleanQuery("content", queryTxt);
        BoostQuery q2b = new BoostQuery(query2, contentBoost);
        BooleanQuery combined = new BooleanQuery.Builder()
                .add(query1, BooleanClause.Occur.SHOULD)
                .add(q2b, BooleanClause.Occur.SHOULD)
                .build();


        TopDocs topDocs = isearcher.search(combined, 1000);
        ScoreDoc[] hits = topDocs.scoreDocs;
        if (debug) {
            System.out.println("Found " + hits.length + " documents");
            for (int i = 0; i < hits.length; i++){
                Document hitDoc = isearcher.doc(hits[i].doc);
                System.out.println(i + ") " + hitDoc.get("doc_id") + " " + hits[i].score + " : "
                        + hitDoc.get("title"));
            }
        }

        tWriter.write(isearcher, hits, q);
    }


    public void close() throws IOException {
        tWriter.close();
        ireader.close();
        directory.close();

    }

    public String queryStemming(String queryText)
	{
//    	String[] stopWords = new String[]{ " i " , " a ", " acts ", " and ", " about ", " an ", " any " , " am ", ",", " are ", " as ", " at ", " be ", " been ", " by ", " do ", " done ", " does " , " cause ", "caused", " can " , " com ", " for ", " from ", " find " , " has " , " how ", " have " , " in ", " is ", " it ", " known " , " list " , " not ", " new " , " of ", " on ", " or ", " other " , " states " , " that ", " than " , " the ", " this ", " there ", " to ", " us " , " use " , " uses " , " steps ",  " was ", " what ", " when ", " where ", " who ", " will ", " with ", " the ", " www " , " you "};
//        
    		queryText = queryText.toLowerCase();
//        for (String stopword : stopWords) {
//        	queryText = queryText.replaceAll("(?i)"+stopword, " ");
//        }
        
//        queryText = queryText.replaceAll("\\d","");
//        queryText = queryText.replaceAll("[ ](?=[ ])|[^A-Za-z ]++","");
//        queryText = queryText.replace("  ", " ");
//        queryText = queryText.replace("?", "");
//        queryText = queryText.replace("\"", "");
//        queryText = queryText.replace("?", "");
        queryText = queryText.replace("what", "");
        queryText = queryText.replace(" how ", " ");
        queryText = queryText.replace(" a ", " ");
        queryText = queryText.replace(" in ", " ");
        queryText = queryText.replace(" is ", " ");
        queryText = queryText.replace(" are ", " ");
        queryText = queryText.replace(" or ", " ");
        queryText = queryText.replace(" the ", " ");
        queryText = queryText.replace(" of ", " ");
        queryText = queryText.replace(" to ", " ");
        queryText = queryText.replace(" has ", " ");
        queryText = queryText.replace(" by ", " ");
        queryText = queryText.replace(" for ", " ");
        queryText = queryText.replace(" as ", " ");
        queryText = queryText.replace(" at ", " ");
        queryText = queryText.replace(" an ", " ");
        queryText = queryText.replace(" and ", " ");
        queryText = queryText.replace(" that ", " ");
        queryText = queryText.replace(" there ", " ");
        queryText = queryText.replace(" have ", " ");
        queryText = queryText.replace(" do ", " ");
        queryText = queryText.replace(" be ", " ");
        queryText = queryText.replace(" i.e., ", " ");
        queryText = queryText.replace("  ", " ");
//        return Arrays.stream(queryText.split(" ")).distinct().collect(Collectors.joining(" "));
       return queryText;
		
	}
    
}
