package com.IRWS.Group7.LuceneNewsArticles;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;


class MyAnalyzer {
    private final int maxNgram;
    private final int minNgram;

    public MyAnalyzer(int minNgram, int maxNgram) {
        this.minNgram = minNgram;
        this.maxNgram = maxNgram;
    }
    static Analyzer getStemmer(){
        return new Analyzer(){
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer token = new StandardTokenizer();
                TokenStream filtered = new LowerCaseFilter(token);
                filtered = new StopFilter(filtered,
                        EnglishAnalyzer.getDefaultStopSet());
                filtered = new PorterStemFilter(filtered);
                return new TokenStreamComponents(token, filtered);
            }
        };
    }
    static Analyzer getNgram(){
        return new Analyzer(){
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer token = new StandardTokenizer();
                TokenStream filtered = new LowerCaseFilter(token);
                filtered = new StopFilter(filtered,
                        EnglishAnalyzer.getDefaultStopSet());
                filtered = new PorterStemFilter(filtered);
//                filtered = new NGramTokenFilter(filtered, 2, 3);
                filtered = new ShingleFilter(filtered,
                        2,2);
                return new TokenStreamComponents(token, filtered);
            }
        };
    }
}

