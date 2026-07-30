package org.Utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TextExtractor {
    public static String getText(Document document){
        Element content = document.selectFirst("#mw-content-text");
        if(content==null)return null;
        content.select("table.ambox, div.hatnote, div.sbox, span.mw-editsection").remove();
        return content.text();
    }
}
