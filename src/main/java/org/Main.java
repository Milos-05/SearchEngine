package org;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
public static BlockingQueue<PageContent> pageTexts;

    public static void main(String[] args) throws InterruptedException {
        pageTexts=new LinkedBlockingQueue<>();
        Crawler crawler=new Crawler();
        crawler.start();
        Thread.sleep(50000);
  }
}