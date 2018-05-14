package com.IRWS.Group7.LuceneNewsArticles;

public class  QueryCleaner {

	public static String removeStopWords(String queryText)
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
