package br.com.tbp.module.graphanalysis.algorithm;


import br.com.tbp.parser.FBJsonParser;
import br.com.tbp.module.graphanalysis.support.FileReader

import br.com.tbp.model.Graph;

class DegreeTest extends GroovyTestCase {

    private String jsonString
    private FBJsonParser fbJsonParser
    private Degree degree

    public  DegreeTest() {
        jsonString = FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt")
        fbJsonParser = new FBJsonParser()
        degree = new Degree()
    }

    void testCompute() {
        Graph graph = fbJsonParser.parse(jsonString)
        degree.execute(graph)
        graph.nodeSet.each { node ->
            if(node.id == "100001811053281") {
                assertEquals(1.0, node.degree)
            } else if (node.id == "100002164977849" ) {
                assertEquals(5.0, node.degree)
            } else if (node.id == "100000401238331") {
                assertEquals(2.0, node.degree)
            } else if (node.id == "100000000103086") {
                assertEquals(4.0, node.degree)
            } else if (node.id == "100000348062784") {
                assertEquals(2.0, node.degree)
            } else if (node.id == "100000015795070") {
                assertEquals(2.0, node.degree)
            } else if(node.id == "100000108890489") {
                assertEquals(0.0, node.degree)
            } else {
                fail("unknown node!")
            }
        }
    }
}
