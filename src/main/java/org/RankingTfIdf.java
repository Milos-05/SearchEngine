package org;

import java.util.Map;

public class RankingTfIdf {
    public static double calculateTf(PageRepository repository,String term,int id){
        Map<Integer,Integer> termFrequencies=repository.getInvertedIndex().get(term);
        if(termFrequencies == null) return 0.0;

        int termCount=termFrequencies.getOrDefault(id,0);
        int totalWords=repository.getWordCount().getOrDefault(id,0);

        if(totalWords == 0) return 0.0;
        return (double) termCount /totalWords;
    }
    public static double calculateIdf(PageRepository repository, String term){
        Map<Integer,Integer> termFrequencies=repository.getInvertedIndex().get(term);

        int totalNumberOfDocuments=repository.getWordCount().size();
        int totalNumberOfDocumentsContainingTerm=termFrequencies.size();

        return Math.log((double) totalNumberOfDocumentsContainingTerm /totalNumberOfDocuments);
    }
    public static double calculateTfIdf(PageRepository repository, String term, int id){
        return calculateTf(repository, term, id)*calculateIdf(repository, term);
    }
}
