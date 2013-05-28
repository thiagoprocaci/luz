package br.com.tbp.module.pnl.mining;


import br.com.tbp.file.FileReader;
import br.com.tbp.model.Graph;
import br.com.tbp.model.Node;
import br.com.tbp.module.pnl.indexer.TextIndexer;
import br.com.tbp.module.pnl.preprocessor.PortugueseAnalyzer;
import br.com.tbp.module.pnl.preprocessor.StopWordManager;
import br.com.tbp.module.pnl.support.Doc;
import br.com.tbp.parser.FBJsonParser;
import org.apache.lucene.queryParser.ParseException;
import org.junit.Before;
import org.junit.Test;

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
            for(String comment : node.getComments() ) {
                doc = new Doc(node.getId() + "-" + i , comment);
                docs.add(doc);
                i++;
            }

        }
    }

    @Test
    public void testRunQuery() throws IOException, ParseException {

        // TODO terminar esse teste
        queryProcessor.runQuery("versao ", docs);

   //     StopWordManager stopWordManager = new StopWordManager();
   //     PortugueseAnalyzer portugueseAnalyzer = new PortugueseAnalyzer(stopWordManager);

      //  for (String s : portugueseAnalyzer.tokenizer(docs.toString())) {
       //     System.out.println(s);
      //  }

    }


}
