package br.com.tbp.module.pnl.preprocessor;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class PortugueseAnalyzer {

    private StopWordManager stopWordManager;
    private Analyzer analyzer;

    public PortugueseAnalyzer(StopWordManager stopWordManager) {
        this.stopWordManager = stopWordManager;
    }

    public Set<String> tokenizer(String text) throws IOException {
        TokenStream stream = getAnalyzer().tokenStream(null, new StringReader(text));
        String word = null;
        Set<String> result = new HashSet<String>();
        while (stream.incrementToken()) {
            word = stream.getAttribute(CharTermAttribute.class).toString();
            if (word != null) {
                result.add(word);
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

}

