package br.com.tbp.module.pnl.preprocessor;


import br.com.tbp.module.graphanalysis.support.FileReader;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class PortugusesAnalyzerTest {

    private PortugueseAnalyzer portugueseAnalyzer;
    private String jsonString;

    @Before
    public void doBefore() {
        StopWordManager stopWordManager = new StopWordManager();
        portugueseAnalyzer = new PortugueseAnalyzer(stopWordManager);
        jsonString = FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt");
    }

    @Test
    public void testTokenizer() throws IOException {
        Set<String> result = portugueseAnalyzer.tokenizer(jsonString);
        for (String s: result) {
            Assert.assertNotNull(s);
        }
    }

}
