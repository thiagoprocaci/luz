package br.com.tbp.module.semantic

import br.com.tbp.model.Graph
import br.com.tbp.module.pnl.preprocessor.MessageGraphTokenizer
import br.com.tbp.module.pnl.preprocessor.PortugueseAnalyzer
import br.com.tbp.module.pnl.preprocessor.PortugueseTokenizer
import br.com.tbp.module.pnl.preprocessor.StopWordManager
import br.com.tbp.parser.FBJsonParser

class OntologyBuilderTest extends GroovyTestCase {

    private OntologyBuilder ontologyBuilder
    private Graph graph
    private MessageGraphTokenizer messageGraphTokenizer
    private StopWordManager stopWordManager
    private PortugueseAnalyzer portugueseAnalyzer
    private PortugueseTokenizer portugueseTokenizer;



    public OntologyBuilderTest() {
        String jsonString = br.com.tbp.file.FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt")
        FBJsonParser fbJsonParser = new FBJsonParser()
        ontologyBuilder = new OntologyBuilder()
        graph = fbJsonParser.parse(jsonString);

        stopWordManager = new StopWordManager();
        portugueseAnalyzer = new PortugueseAnalyzer(stopWordManager)
        portugueseTokenizer = new PortugueseTokenizer(portugueseAnalyzer)
        messageGraphTokenizer = new MessageGraphTokenizer(portugueseTokenizer)
        messageGraphTokenizer.buildTokens(graph)
    }

    void testOntologyBuilder() {
        File ontologyFile = ontologyBuilder.buildOntology(graph)
        assertNotNull(ontologyFile)

        String expectedString = br.com.tbp.file.FileReader.readFile("src/test/resources/br/com/tbp/module/semantic/expected_ontology.xml")
        expectedString = expectedString.replaceAll("\\s+","");

        String ontologyString = br.com.tbp.file.FileReader.readFile(ontologyFile)
        ontologyString =  ontologyString.replaceAll("\\s+","");

        assertEquals(expectedString.length(), ontologyString.length())
    }
}
