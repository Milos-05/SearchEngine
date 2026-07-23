package org;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Crawler crawler=new Crawler();
        crawler.start();
        Thread.sleep(50000);
  }
}