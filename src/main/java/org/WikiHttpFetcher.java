package org;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class WikiHttpFetcher {
    private static final HttpClient client=HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public Document getWikiResponse(String url){
        try {
            HttpRequest request = createReq(URI.create(url));
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode()!=200){
                System.err.println("Bad request"+response.statusCode());
                return null;
            }
            System.out.println(response.uri());
            System.out.println(response.headers());
            System.out.println("----");
            Document doc= Jsoup.parse(response.body(),url);
            return doc;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid URL: " + url);
            return null;
        } catch (IOException e) {
            System.err.println("Fetch failed: " + url);
            return null;
        } catch (InterruptedException e) {
            return null;
        }
    }

    public static HttpRequest createReq(URI uri) {
         return HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                     .timeout(Duration.ofSeconds(5))
                    .header("User-Agent", "WikiCrawler")
                    .build();
    }


}
