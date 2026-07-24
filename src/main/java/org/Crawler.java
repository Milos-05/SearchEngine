package org;

import com.google.common.util.concurrent.RateLimiter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Set;
import java.util.concurrent.*;

public class Crawler {
    private final BlockingQueue<Url> urlQueue;
    private final ExecutorService executor;
    private final WikiHttpFetcher fetcher;
    private final Set<String> visited;
    private final RateLimiter rateLimiter;

    private static final int MAX_REQUESTS = 1_000_000;
    private static final double RATE = 3.0;

    public Crawler() {
        urlQueue = new LinkedBlockingQueue<>();
        fetcher = new WikiHttpFetcher();
        visited = ConcurrentHashMap.newKeySet();
        rateLimiter=RateLimiter.create(RATE);
        executor = Executors.newVirtualThreadPerTaskExecutor();
    }
    public void start(){
        Url input=new Url("https://en.wikipedia.org/wiki/Java_(programming_language)",null);
        urlQueue.add(input);
        visited.add(input.getUrl());
       for(int i=0;i<MAX_REQUESTS;i++){
            rateLimiter.acquire();
            int finalI = i;
            executor.submit(() -> {
                try {
                    Url url = urlQueue.poll(5, TimeUnit.SECONDS);
                    if(url==null){
                        return;
                    }
                    Document doc = fetcher.getWikiResponse(url.getUrl());

                    if (doc == null) {
                        System.err.println("Failed to fetch: " + url);
                        return;
                    }
                    String text=getText(doc);
                    if(text!=null) {
                        Main.pageTexts.add(new PageContent(text,url.getUrl()));
                    }
                    Elements wikiLinks = doc.select("a[rel=mw:WikiLink]");
                    int count=wikiLinks.size();
                    System.out.println( finalI +": Added url: " + url+" added "+count+" new urls, and total urls waiting="+urlQueue.size());
                    for (Element element : wikiLinks) {
                        String link = element.attr("abs:href");
                        if(visited.add(link)) {
                            Url newUrl =new Url(link, url);
                            urlQueue.add(newUrl);
                        }
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
    private String getText(Document document){
        Element content = document.selectFirst("#mw-content-text");
        if(content==null)return null;
        document.select("table.ambox, div.hatnote, div.sbox, span.mw-editsection").remove();
        return content.text();
    }
}