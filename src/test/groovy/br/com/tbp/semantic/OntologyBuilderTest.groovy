package br.com.tbp.semantic

import br.com.tbp.model.Graph
import br.com.tbp.module.graphanalysis.algorithm.Degree
import br.com.tbp.module.graphanalysis.algorithm.GraphDistance
import br.com.tbp.module.graphanalysis.algorithm.Modularity
import br.com.tbp.module.graphanalysis.algorithm.PageRank
import br.com.tbp.parser.FBJsonParser


class OntologyBuilderTest extends GroovyTestCase {

    private String jsonString
    private FBJsonParser fbJsonParser
    private OntologyBuilder ontologyBuilder
    private GraphDistance graphDistance
    private Degree degree
    private Modularity modularity
    private PageRank pageRank

    public OntologyBuilderTest() {
        jsonString = br.com.tbp.file.FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt")
        fbJsonParser = new FBJsonParser()
        ontologyBuilder = new OntologyBuilder()
        graphDistance = new GraphDistance()
        degree = new Degree()
        modularity = new Modularity()
        pageRank = new PageRank()
    }

    void testOntologyBuilder() {
        Graph graph = fbJsonParser.parse(jsonString);
        graphDistance.execute(graph)
        degree.execute(graph)
        pageRank.execute(graph, false)
        modularity.execute(graph, true, false)
        ontologyBuilder.buildOntology(graph)
        // TODO terminar esse teste
    }
}
