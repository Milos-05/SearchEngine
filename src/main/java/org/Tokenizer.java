package org;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "and", "or", "but", "in", "on", "at", "to", "for",
            "of", "with", "by", "is", "are", "was", "were", "be", "been", "being",
            "have", "has", "had", "do", "does", "did", "will", "would", "could",
            "should", "may", "might", "must", "can", "this", "that", "these", "those",
            "it", "its", "as", "from", "not", "no", "so", "than", "then", "there",
            "which", "who", "whom", "whose", "what", "when", "where", "why", "how"
    );
    private static final Pattern PATTERN=Pattern.compile("[a-zA-Z0-9]+");

    public static List<String> tokenize(String raw){
        if(raw == null || raw=="")return List.of();

        Matcher matcher=PATTERN.matcher(raw.toLowerCase());
        List<String> tokens=new ArrayList<>();
        while (matcher.find()){
            String match=matcher.group();
            if(!STOP_WORDS.contains(match)){
                tokens.add(match);
            }
        }
        return tokens;
    }

}
