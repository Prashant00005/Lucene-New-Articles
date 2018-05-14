package com.IRWS.Group7.LuceneNewsArticles;

import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
public class Main {

    public static void main(String[] args) throws Exception {
        Options options = new Options();

        
        Option inputData = new Option("d", "dataPath",
        true, "Queries file. Default: data/");
        inputData.setRequired(false);
        options.addOption(inputData);

        Option outputIndex = new Option("index_path",
                true, "Folder to store the Index. Default: index_folder");
        outputIndex.setRequired(false);
        options.addOption(outputIndex);
//
        Option outputResultsOpt = new Option("o", "output",
                true, "Output search results file. Default: results.txt");
        outputResultsOpt.setRequired(false);
        options.addOption(outputResultsOpt);
//
        Option relevance = new Option("r", "relevance", true,
                "Relevant documents file. Default: cran/cranqrel");
        relevance.setRequired(false);
        options.addOption(relevance);
//
        Option configOption = new Option("g", "config",
                true, "Indexer configuration one between:" +
                " StandardEnglish, Stemmer, Ngram. Default: StandardEnglish_BM25");
        configOption.setRequired(false);
        options.addOption(configOption);

        Option contentBoostOption = new Option("b", "contentBoost",
                true, "The boost applied to content. Default: 2.0");
        contentBoostOption.setRequired(false);
        options.addOption(contentBoostOption);

        Option runIndexOption = new Option("i", "index",
                false, "Run the documents indexation");
        runIndexOption.setRequired(false);
        options.addOption(runIndexOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("my-first-search-engine", options);
            System.exit(1);
            return;
        }

        String dataPath = cmd.getOptionValue("dataPath", "data/");
        String boostValue = cmd.getOptionValue("contentBoost", "2.0");
        String indexPath = cmd.getOptionValue("index_path",
        		"index_folder");
        String outputResults = cmd.getOptionValue("output",
        		"results.txt");
        String configuration = cmd.getOptionValue("config",
        		"Stemmer_BM25");
        Float contentBoost = new Float(cmd.getOptionValue("contentBoost",
        		"2.0"));
        Boolean runIndex = cmd.hasOption("index");
        //        String cranQryRel = cmd.getOptionValue("relevance",
//      "cran/cranqrel");

        NewsQueryParsing qp = new NewsQueryParsing("data/CS7IS3-Assignment2-Topics");
//        System.out.println("Two first queries:");
//        qp.showQueries(2);
        FTParser ftParser = new FTParser(dataPath);
//        ftParser.parseDocs();
//        System.out.println("Two first documents:");
//        ftParser.showDocuments(2);
        
        
        LuceneIndex index = new LuceneIndex(ftParser, indexPath, configuration, runIndex);

        index.search(qp, outputResults, contentBoost, false);
    }
}
