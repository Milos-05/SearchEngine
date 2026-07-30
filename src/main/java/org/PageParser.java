package org;

import lombok.Getter;
import org.Utils.Tokenizer;

import java.util.List;
import java.util.concurrent.*;

@Getter
public class PageParser {
    private BlockingQueue<PageContent> pages;
    private PageRepository repository;

    public PageParser(PageRepository repository){
        pages = new LinkedBlockingQueue<>();
        this.repository=repository;
    }

    public void start(){
        int threads=Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        while (!pages.isEmpty()) {
            executor.submit(this::index);
        }
        System.out.println(repository.invertedIndex);
        System.out.println(repository.metaData);
    }

    private  void index() {
        try {
            PageContent currentPage = pages.poll(5, TimeUnit.SECONDS);
            if(currentPage==null) return;

            List<String> words= Tokenizer.tokenize(currentPage.getText());
            PageMetaData metaData=new PageMetaData(currentPage.getUrl());
            int id=metaData.getId();

            repository.addToMetaData(metaData);
            repository.addToWordCount(id, words.size());
            words.forEach(s->repository.addToInvertedIndex(s,id));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void addPageToQueue(PageContent content){
        pages.add(content);
    }

}
