package org;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Url {
    private String url;
    private Url parent;
    private int depth;
    public Url(String url,Url parent){
        this.url=url;
        this.parent=parent;
        if(parent==null){
            this.depth=0;
        }else {
            this.depth=parent.depth+1;
        }
    }

    @Override
    public String toString() {
        return "Url{" +
                "url='" + url + '\'' +
                ", parent=" + (parent != null ? parent.url : "null") +
                ", depth=" + depth +
                '}';
    }
}
