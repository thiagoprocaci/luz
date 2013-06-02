package br.com.tbp.module.pnl.preprocessor;

import br.com.tbp.file.FileReader;
import br.com.tbp.model.Graph;
import br.com.tbp.model.Node;
import br.com.tbp.model.Token;
import br.com.tbp.parser.FBJsonParser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

public class MessageGraphTokenizerTest {

    private FBJsonParser fbJsonParser;
    private MessageGraphTokenizer messageGraphTokenizer;
    private PortugueseAnalyzer portugueseAnalyzer;
    private StopWordManager stopWordManager;
    private PortugueseTokenizer portugueseTokenizer;
    private Graph graph;

    @Before
    public void doBefore() {
        stopWordManager = new StopWordManager();
        portugueseAnalyzer = new PortugueseAnalyzer(stopWordManager);
        portugueseTokenizer = new PortugueseTokenizer(portugueseAnalyzer);
        messageGraphTokenizer = new MessageGraphTokenizer(portugueseTokenizer);
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
