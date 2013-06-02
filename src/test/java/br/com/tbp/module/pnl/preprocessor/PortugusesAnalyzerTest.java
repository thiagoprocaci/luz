package br.com.tbp.module.pnl.preprocessor;


import br.com.tbp.file.FileReader;
import br.com.tbp.model.Graph;
import br.com.tbp.model.Message;
import br.com.tbp.model.Node;
import br.com.tbp.parser.FBJsonParser;
import junit.framework.Assert;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
