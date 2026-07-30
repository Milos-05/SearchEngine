package org;

import lombok.Data;
import org.model.PageMetaData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Data
public class PageRepository {
    private Map<String,Map<Integer,Integer>> invertedIndex;
    private Map<Integer,Integer> wordCount;
    private Map<Integer, PageMetaData> metaData;

    public PageRepository(){
        invertedIndex=new ConcurrentHashMap<>();
        wordCount=new ConcurrentHashMap<>();
        metaData=new ConcurrentHashMap<>();
    }
    public void addToMetaData(PageMetaData pageMetaData){
        metaData.put(pageMetaData.getId(), pageMetaData);
    }
    public void addToWordCount(int id,int count){
        wordCount.put(id,count);
    }
    public void addToInvertedIndex(String word,int id){
        Map<Integer,Integer> countForId=invertedIndex.getOrDefault(word, new ConcurrentHashMap<>());
        countForId.put(id, countForId.getOrDefault(id, 0)+1);
        invertedIndex.put(word,countForId);
    }
}
