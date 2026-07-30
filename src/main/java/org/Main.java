package org;

import org.Utils.Tokenizer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        PageRepository repository=new PageRepository();
        PageParser parser=new PageParser(repository);
        WikiHttpFetcher fetcher=new WikiHttpFetcher();
        Crawler crawler=new Crawler(parser,fetcher);

        crawler.start();
        parser.start();


        Thread.sleep(50000);
  }
}