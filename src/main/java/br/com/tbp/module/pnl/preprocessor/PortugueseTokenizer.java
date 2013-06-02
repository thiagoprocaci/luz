package br.com.tbp.module.pnl.preprocessor;


import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class PortugueseTokenizer {

    private PortugueseAnalyzer portugueseAnalyzer;


    public PortugueseTokenizer(PortugueseAnalyzer portugueseAnalyzer) {
        this.portugueseAnalyzer = portugueseAnalyzer;
    }

    public Map<String, Integer> tokenizer(String text) throws IOException {
        TokenStream stream = portugueseAnalyzer.getAnalyzer().tokenStream(null, new StringReader(text));
        String word = null;
        Map<String, Integer> result = new HashMap<String, Integer>();
        while (stream.incrementToken()) {
            word = stream.getAttribute(CharTermAttribute.class).toString();
            if (isWordValid(word)) {
                if(!result.containsKey(word)) {
                    result.put(word, 0);
                }
                result.put(word, result.get(word)+ 1);
            }
        }
        return result;
    }

    private boolean isWordValid(String word) {
        if (word == null || word.length() <= 3 ||
                word.startsWith("1") || word.startsWith("2") || word.startsWith("3")
                || word.startsWith("4") || word.startsWith("5") || word.startsWith("6")
                || word.startsWith("7") || word.startsWith("8") || word.startsWith("9")
                || word.startsWith("0") || word.contains(".")) {
            return false;
        }
        return true;
    }
}
