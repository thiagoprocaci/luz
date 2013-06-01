package br.com.tbp.module.pnl.mining;


import br.com.tbp.file.FileReader;
import br.com.tbp.model.Graph;
import br.com.tbp.model.Message;
import br.com.tbp.model.Node;
import br.com.tbp.module.pnl.indexer.TextIndexer;
import br.com.tbp.module.pnl.preprocessor.PortugueseAnalyzer;
import br.com.tbp.module.pnl.preprocessor.StopWordManager;
import br.com.tbp.module.pnl.support.Doc;
import br.com.tbp.parser.FBJsonParser;
import org.apache.lucene.queryParser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class QueryProcessorTest {
    private QueryProcessor queryProcessor;
    private Set<Doc> docs;

    @Before
    public void doBefore() {
        String jsonString = FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt");
        FBJsonParser fbJsonParser = new FBJsonParser();

        StopWordManager stopWordManager = new StopWordManager();
        PortugueseAnalyzer portugueseAnalyzer = new PortugueseAnalyzer(stopWordManager);


        TextIndexer textIndexer = new TextIndexer(portugueseAnalyzer);
        queryProcessor = new QueryProcessor(portugueseAnalyzer, textIndexer);
        Graph graph = fbJsonParser.parse(jsonString);

        Doc doc = null;
        docs = new HashSet<Doc>();
        int i = 0;
        for (Node node : graph.getNodeSet()) {
            for (Message message : node.getMessages()) {
                if (message.getContent() != null) {
                    doc = new Doc(message.getId(), message.getContent());
                    docs.add(doc);
                    i++;
                }
            }
        }
    }

    @After
    public void doAfter() {
        File file = new File("src/main/resources/index".replace("/", File.separator));
        File[] files = file.listFiles();
        // limpa lixo
        for (File f : files) {
            if(!f.getName().contains("a.txt")) {
                f.delete();
            }
        }
    }

    @Test
    public void testRunQuery() throws IOException, ParseException {

        // TODO terminar esse teste
        queryProcessor.runQuery("versao ", docs);


    }


}
