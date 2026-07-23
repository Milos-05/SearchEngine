package org;

import com.google.common.util.concurrent.RateLimiter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Crawler {
    private final BlockingQueue<String> urlQueue;
    private final ExecutorService executor;
    private final WikiHttpFetcher fetcher;
    private final Set<String> visited;
    private final RateLimiter rateLimiter;

    private static final int MAX_REQUESTS = 100;
    private static final double RATE = 200.0/60.0;

    public Crawler() {
        urlQueue = new LinkedBlockingQueue<>();
        fetcher = new WikiHttpFetcher();
        visited = ConcurrentHashMap.newKeySet();
        rateLimiter=RateLimiter.create(RATE);
        executor = Executors.newVirtualThreadPerTaskExecutor();
    }
    public void start(){
        urlQueue.add("https://en.wikipedia.org/wiki/Java_(programming_language)");
        int i=0;
        while (true) {
            i++;
            rateLimiter.acquire();
            int finalI = i;
            executor.submit(() -> {
                try {
                    String url = urlQueue.poll(5, TimeUnit.SECONDS);
                    if(url==null){
                        return;
                    }

                    System.out.println( finalI +": Uzeo url: " + url);
                    Document doc = fetcher.getWikiResponse(url);
                    if (doc == null) {
                        System.err.println("Failed to fetch: " + url);
                        return;
                    }

                    Elements wikiLinks = doc.select("a[rel=mw:WikiLink]");
                    for (Element element : wikiLinks) {
                        String newUrl = element.attr("abs:href");
                        if(visited.add(newUrl)) {
                            urlQueue.add(newUrl);
                        }
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}