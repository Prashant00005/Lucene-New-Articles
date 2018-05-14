package com.IRWS.Group7.LuceneNewsArticles;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class TrecWriter {
    private PrintWriter lineWriter = null;
    private FileWriter fWriter  = null;

    public TrecWriter(String path) throws IOException {
        this.fWriter = new FileWriter(path, false);
        this.lineWriter =  new PrintWriter(fWriter);
    }
    public void write(IndexSearcher isearcher, ScoreDoc[] hits, QueryObject q) throws IOException {
        // Write to results file
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            lineWriter.printf("%d 0 %s 0 %f std\n",
                    q.getQueryNumber(), hitDoc.get("doc_id"), hits[i].score);
        }
    }
    public void close() throws IOException {
        lineWriter.close();
        fWriter.close();
    }
}
