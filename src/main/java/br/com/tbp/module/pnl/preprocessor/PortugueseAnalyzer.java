package br.com.tbp.module.pnl.preprocessor;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.util.Version;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class PortugueseAnalyzer {

    private StopWordManager stopWordManager;
    private Analyzer analyzer;

    public PortugueseAnalyzer(StopWordManager stopWordManager) {
        this.stopWordManager = stopWordManager;
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

