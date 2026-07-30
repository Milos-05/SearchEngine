package org;

import com.google.common.util.concurrent.RateLimiter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Set;
import java.util.concurrent.*;

import static org.Utils.TextExtractor.getText;

public class Crawler {
    private final BlockingQueue<Url> urlQueue;
    private final ExecutorService executor;
    private final WikiHttpFetcher fetcher;
    private final Set<String> visited;
    private final RateLimiter rateLimiter;
    private final PageParser pageParser;

    private static final int MAX_REQUESTS = 20;
    private static final double RATE = 3.0;

    public Crawler(PageParser parser) {
        urlQueue = new LinkedBlockingQueue<>();
        fetcher = new WikiHttpFetcher();
        visited = ConcurrentHashMap.newKeySet();
        rateLimiter=RateLimiter.create(RATE);
        executor = Executors.newVirtualThreadPerTaskExecutor();
        pageParser = parser;
    }
    public void start(){
        Url input=new Url("https://en.wikipedia.org/wiki/Java_(programming_language)",null);
        urlQueue.add(input);
        visited.add(input.getUrl());
       for(int i=0;i<MAX_REQUESTS;i++){
            rateLimiter.acquire();
            executor.submit(this::processUrl);
        }
    }

    private void processUrl() {
        try {
            Url url = urlQueue.poll(5, TimeUnit.SECONDS);
            if(url==null) return;

            Document doc = fetchDocument(url);
            if (doc == null) return;

            addDocumentToParser(doc, url);

            getLingsAndAddThemToQueue(doc, url);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void getLingsAndAddThemToQueue(Document doc, Url url) {
        Elements wikiLinks = doc.select("a[rel=mw:WikiLink]");
        int count=wikiLinks.size();
        System.out.println("Added url: " + url +" added "+count+" new urls, and total urls waiting="+urlQueue.size());
        for (Element element : wikiLinks) {
            String link = element.attr("abs:href");
            if(visited.add(link)) {
                Url newUrl =new Url(link, url);
                urlQueue.add(newUrl);
            }
        }
    }

    private Document fetchDocument(Url url) {
        Document doc = fetcher.getWikiResponse(url.getUrl());
        if (doc == null) {
            System.err.println("Failed to fetch: " + url);
            return null;
        }
        return doc;
    }

    private void addDocumentToParser(Document doc,Url url){
        String text=getText(doc);
        if(text!=null) {
            pageParser.addPageToQueue(new PageContent(text,url.getUrl()));
        }
    }
}