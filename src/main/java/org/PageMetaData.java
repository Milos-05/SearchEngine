package org;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class PageMetaData {
    private static AtomicInteger counter=new AtomicInteger(0);
    private int id;
    private String url;
    public PageMetaData(String url){
        id=counter.getAndIncrement();
        this.url=url;
    }

}
