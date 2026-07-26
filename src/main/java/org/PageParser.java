package org;

import lombok.Getter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class PageParser {
    private BlockingQueue<PageContent> pages;

    public PageParser(){
        pages = new LinkedBlockingQueue<>();
    }

    public void start(){
        int threads=Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threads);
    }
}
