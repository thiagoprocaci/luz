package br.com.tbp.module.pnl.preprocessor;


import br.com.tbp.file.FileReader;
import br.com.tbp.model.Graph;
import br.com.tbp.model.Node;
import br.com.tbp.model.Token;
import br.com.tbp.parser.FBJsonParser;
import static junit.framework.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MessageGraphTokenizerTest {

    private FBJsonParser fbJsonParser;
    private MessageGraphTokenizer messageGraphTokenizer;
    private PortugueseAnalyzer portugueseAnalyzer;
    private StopWordManager stopWordManager;
    private Graph graph;

    @Before
    public void doBefore() {
        stopWordManager = new StopWordManager();
        portugueseAnalyzer = new PortugueseAnalyzer(stopWordManager);
        messageGraphTokenizer = new MessageGraphTokenizer(portugueseAnalyzer);
        fbJsonParser = new FBJsonParser();
        graph = fbJsonParser.parse(FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt"));
    }

    @Test
    public void testBuildTokens() throws IOException {
        messageGraphTokenizer.buildTokens(graph);
        assertNotNull(graph.getTokens());
        assertFalse(graph.getTokens().isEmpty());

        for (Token token : graph.getTokens()) {
            assertNotNull(token);
            assertNotNull(token.getWord());
            assertNotNull(token.getId());
            assertNotNull(token.getFrequency());
            assertFalse(token.getFrequency().isEmpty());

            for (Node node : token.getFrequency().keySet()) {
                assertNotNull(node);
                assertFalse(token.getFrequency().get(node) < 1);
            }

        }
    }


}
