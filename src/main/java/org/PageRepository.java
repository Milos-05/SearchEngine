package org;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Data
public class PageRepository {
     Map<String,Map<Integer,Integer>> invertedIndex;
     Map<Integer,Integer> wordCount;
     Map<Integer,PageMetaData> metaData;
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
