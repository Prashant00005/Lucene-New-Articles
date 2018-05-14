package com.IRWS.Group7.LuceneNewsArticles;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.InvalidParameterException;

public class LuceneIndex {
    private final String configuration;
    private String indexPath;

    public Analyzer getAnalyser(){
        /**Filters StandardTokenizer with StandardFilter,
        LowerCaseFilter and StopFilter, using a list of English stop words.*/
        if(configuration.startsWith("StandardEnglish"))
            return new StandardAnalyzer(EnglishAnalyzer.getDefaultStopSet());
        else if (configuration.startsWith("Stemmer"))
            return MyAnalyzer.getStemmer();
        else if (configuration.startsWith("Ngram"))
            return MyAnalyzer.getNgram();
        else
            throw new InvalidParameterException("Invalid configuration " +
                    configuration);

    }
    public LuceneIndex(FTParser ftParser, String indexPath,
                       String configuration, Boolean runIndexing) throws IOException {
        this.indexPath = indexPath;
        this.configuration = configuration;
        if (runIndexing) {
			Analyzer analyzer = this.getAnalyser();
			Directory directory = FSDirectory.open(Paths.get(indexPath));
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			config.setRAMBufferSizeMB(50.);
			IndexWriter iwriter = new IndexWriter(directory, config);
			FieldType typeTitle = new FieldType();
			typeTitle.setStoreTermVectors(true);
			typeTitle.setStored(true);
			typeTitle.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
			FieldType typeContent = new FieldType();
			typeContent.setStoreTermVectors(true);
			typeContent.setStored(true);
			typeContent.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);

        
			System.out.println("Indexing documents...");
			DocumentObject obj;
			int count = 0;
			while((obj = ftParser.nextDocument()) != null){
				Document doc = new Document();
	//            System.out.println("id:" + obj.getId());
				doc.add(new Field("title", obj.getHeader(), typeTitle));
				doc.add(new Field("content", obj.getText(), typeContent));
				doc.add(new StringField("doc_id",
						String.valueOf(obj.getId()),
						Field.Store.YES));
				iwriter.addDocument(doc);
				count++;
				if(count % 10000 == 0) {
					System.out.println("Document indexed: " + count);
	//            	break;
	//            	iwriter.flush();
				}
			}
			System.out.println("Done indexing documents.");
			iwriter.close();
			directory.close();
    	}
    }


    public void search(NewsQueryParsing queries, String output_results,
                       Float contentBoost, boolean debug) throws IOException, ParseException {
        Similarity similarity;
        if(this.configuration.endsWith("BM25"))
            similarity = new BM25Similarity();
        else if(this.configuration.endsWith("TFIDF"))
            similarity = new ClassicSimilarity();
        else if(this.configuration.endsWith("Bool"))
            similarity = new BooleanSimilarity();
        else
            throw new InvalidParameterException("Invalid configuration " +
                    configuration);

        Analyzer analyzer = this.getAnalyser();
        QueryBuilder qBuilder = new QueryBuilder(analyzer);

        QuerySearcher qSearcher = new QuerySearcher(this.indexPath, qBuilder,
                output_results, similarity, contentBoost);

        int nbQueries = 0;
        System.out.println("Running Queries...");
        for(QueryObject q: queries.queryObjectList) {

            qSearcher.search(q, debug);

            debug = false;
        }
        System.out.println("Done with queries.");

//        qSearcher.printStat();
        qSearcher.close();
    }
}
