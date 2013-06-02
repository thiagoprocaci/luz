package br.com.tbp.module.pnl.preprocessor;

import junit.framework.Assert;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

public class PortugusesAnalyzerTest {

    private PortugueseAnalyzer portugueseAnalyzer;


    @Before
    public void doBefore() {
        StopWordManager stopWordManager = new StopWordManager();
        portugueseAnalyzer = new PortugueseAnalyzer(stopWordManager);
    }

    @Test
    public void testGetAnalyzer() throws FileNotFoundException {
        Analyzer analyzer = portugueseAnalyzer.getAnalyzer();
        Assert.assertNotNull(analyzer);
        Assert.assertTrue(analyzer instanceof BrazilianAnalyzer);
    }





}
