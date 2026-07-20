package org;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Queue;
import java.util.concurrent.*;

public class Crawler {
   private BlockingQueue<String> urlQueue;
   private ExecutorService executor;
   private WikiHttpFetcher fetcher;
    public Crawler(){
        urlQueue=new LinkedBlockingQueue<>();
        fetcher=new WikiHttpFetcher();
        urlQueue.add("https://en.wikipedia.org/wiki/Java_(programming_language)");
        executor = Executors.newVirtualThreadPerTaskExecutor();
        for(int i=0;i<10;i++){
            executor.submit(()->{
                try {
                   String url = urlQueue.poll(5, TimeUnit.SECONDS);
                    System.out.println("Uzeo url: "+url);
                Document doc= fetcher.getWikiResponse(url);
                    if (doc == null) {
                        System.err.println("Failed to fetch: " + url);
                        return;
                    }
                Elements wikiLinks = doc.select("a[rel=mw:WikiLink]");
                for(Element element:wikiLinks){
                    String newUrl=element.attr("href");
                    urlQueue.add(newUrl);
                }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
    public void awaitCompletion() throws InterruptedException {
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

}
