package br.com.tbp.module.semantic

import br.com.tbp.model.Graph
import br.com.tbp.parser.FBJsonParser

class OntologyBuilderTest extends GroovyTestCase {

    private OntologyBuilder ontologyBuilder
    private Graph graph


    public OntologyBuilderTest() {
        String jsonString = br.com.tbp.file.FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt")
        FBJsonParser fbJsonParser = new FBJsonParser()
        ontologyBuilder = new OntologyBuilder()
        graph = fbJsonParser.parse(jsonString);
    }

    void testOntologyBuilder() {
        File ontologyFile = ontologyBuilder.buildOntology(graph)
        assertNotNull(ontologyFile)

        String expectedString = br.com.tbp.file.FileReader.readFile("src/test/resources/br/com/tbp/module/semantic/expected_ontology.xml")
        expectedString = expectedString.replaceAll("\\s+","");

        String ontologyString = br.com.tbp.file.FileReader.readFile(ontologyFile)
        ontologyString =  ontologyString.replaceAll("\\s+","");

        assertEquals(expectedString, ontologyString)
    }
}
