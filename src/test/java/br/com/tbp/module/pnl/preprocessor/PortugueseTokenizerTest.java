package br.com.tbp.module.pnl.preprocessor;


import br.com.tbp.file.FileReader;
import br.com.tbp.model.Graph;
import br.com.tbp.model.Message;
import br.com.tbp.model.Node;
import br.com.tbp.parser.FBJsonParser;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PortugueseTokenizerTest {

    private PortugueseAnalyzer portugueseAnalyzer;
    private String jsonString;
    private FBJsonParser fbJsonParser;
    private Graph graph;
    private PortugueseTokenizer portugueseTokenizer;

    @Before
    public void doBefore() {
        jsonString = FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt");
        fbJsonParser = new FBJsonParser();
        StopWordManager stopWordManager = new StopWordManager();
        portugueseAnalyzer = new PortugueseAnalyzer(stopWordManager);
        portugueseTokenizer = new PortugueseTokenizer(portugueseAnalyzer);
    }

    @Test
    public void testTokenizer() throws IOException {
        Map<String, Integer> result = portugueseTokenizer.tokenizer(jsonString);
        for (String s : result.keySet()) {
            Assert.assertNotNull(s);
            Assert.assertTrue(s.length() > 3);
            Assert.assertFalse(s.startsWith("0"));
            Assert.assertFalse(s.startsWith("1"));
            Assert.assertFalse(s.startsWith("2"));
            Assert.assertFalse(s.startsWith("3"));
            Assert.assertFalse(s.startsWith("4"));
            Assert.assertFalse(s.startsWith("5"));
            Assert.assertFalse(s.startsWith("6"));
            Assert.assertFalse(s.startsWith("7"));
            Assert.assertFalse(s.startsWith("8"));
            Assert.assertFalse(s.startsWith("9"));
            Assert.assertTrue(result.get(s) > 0);
        }
    }

}


