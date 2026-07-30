package org.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private static final Set<String> STOP_WORDS = Set.of(
            "i", "me", "my", "myself", "we", "our", "ours", "ourselves",
            "you", "your", "yours", "yourself", "yourselves", "he", "him",
            "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what",
            "which", "who", "whom", "this", "that", "these", "those", "am",
            "is", "are", "was", "were", "be", "been", "being", "have", "has",
            "had", "having", "do", "does", "did", "doing", "a", "an", "the",
            "and", "but", "if", "or", "because", "as", "until", "while", "of",
            "at", "by", "for", "with", "about", "against", "between", "into",
            "through", "during", "before", "after", "above", "below", "to",
            "from", "up", "down", "in", "out", "on", "off", "over", "under",
            "again", "further", "then", "once", "here", "there", "when",
            "where", "why", "how", "all", "any", "both", "each", "few", "more",
            "most", "other", "some", "such", "no", "nor", "not", "only", "own",
            "same", "so", "than", "too", "very", "s", "t", "can", "will",
            "just", "don", "should", "now"
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
