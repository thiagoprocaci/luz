package br.com.tbp.module.graphanalysis.algorithm

import br.com.tbp.file.FileReader
import br.com.tbp.model.Graph
import br.com.tbp.parser.FBJsonParser

class PageRankTest extends GroovyTestCase {

    private String jsonString
    private FBJsonParser fbJsonParser
    private PageRank pageRank

    public PageRankTest() {
        jsonString = FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt")
        fbJsonParser = new FBJsonParser()
        pageRank = new PageRank()
    }

    void testCompute() {
        Graph graph = fbJsonParser.parse(jsonString);
        pageRank.execute(graph, false)

        graph.nodeSet.each { node ->
            if ("Roberto Lima".equals(node.name)) {
                assertEquals(0.07462164409699146, node.pagerank)
            } else if ("Pimentel Mariano".equals(node.name)) {
                assertEquals(0.2957059760794221, node.pagerank)
            } else if ("Cristiane Tavares".equals(node.name)) {
                assertEquals(0.1241005014065115, node.pagerank)
            } else if ("Cristiane Iglesias".equals(node.name)) {
                assertEquals(0.23298067531476743, node.pagerank)
            } else if ("Thiago Baesso Procaci".equals(node.name)) {
                assertEquals(0.1241005014065115, node.pagerank)
            } else if ("Claudia Cappelli".equals(node.name)) {
                assertEquals(0.1241005014065115, node.pagerank)
            } else if ("Renata Araujo".equals(node.name)) {
                assertEquals(0.02439024499276793, node.pagerank)
            } else {
                fail()
            }
        }
    }

}
