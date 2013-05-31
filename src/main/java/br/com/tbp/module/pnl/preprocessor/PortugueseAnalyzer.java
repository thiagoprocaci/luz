package br.com.tbp.module.pnl.preprocessor;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PortugueseAnalyzer {

    private StopWordManager stopWordManager;
    private Analyzer analyzer;

    public PortugueseAnalyzer(StopWordManager stopWordManager) {
        this.stopWordManager = stopWordManager;
    }

    public Map<String, Integer> tokenizer(String text) throws IOException {
        TokenStream stream = getAnalyzer().tokenStream(null, new StringReader(text));
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

    public Analyzer getAnalyzer() throws FileNotFoundException {
        if (analyzer == null) {
            Set<String> stopWords = new HashSet<String>();
            stopWords.addAll(stopWordManager.loadStopWords());
            analyzer = new BrazilianAnalyzer(Version.LUCENE_35, stopWords);
        }
        return analyzer;
    }

    private boolean isWordValid(String word) {
        if (word == null || word.length() <= 3 ||
                word.startsWith("1") || word.startsWith("2") || word.startsWith("3")
                || word.startsWith("4") || word.startsWith("5") || word.startsWith("6")
                || word.startsWith("7") || word.startsWith("8") || word.startsWith("9")
                || word.startsWith("0")) {
            return false;
        }
        return true;
    }

}

