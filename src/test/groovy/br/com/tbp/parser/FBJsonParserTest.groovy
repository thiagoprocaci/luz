package br.com.tbp.parser


import br.com.tbp.model.Graph
import br.com.tbp.model.Node
import groovy.json.JsonSlurper
import br.com.tbp.model.Edge
import br.com.tbp.file.FileReader



class FBJsonParserTest extends GroovyTestCase {

    private String jsonString;
    private String jsonStringExpected;
    private FBJsonParser fbJsonParser;

    public FBJsonParserTest() {
        jsonString = FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt")
        fbJsonParser = new FBJsonParser()
        jsonStringExpected = FileReader.readFile("src/test/resources/br/com/tbp/parser/fb.txt")
    }

    void testParse()  {
        Graph graph = fbJsonParser.parse(jsonString);

        def json = new JsonSlurper().parseText(jsonStringExpected)
        def nodes = json.nodeSet
        def edges = json.edgeSet
        assertEquals(nodes.size(), graph.nodeSet.size())
        assertEquals(edges.size(), graph.edgeSet.size())

        nodes.each { node ->
           assertTrue(graph.nodeSet.contains(new Node(node.id, node.name)))
        }

       // graph.getNodeSet().each { n ->
        //    println(n.name)
         //   println(n.comments)
         //   println("===============")
       // }

        edges.each { edge ->
            def node1 = new Node(edge.node1.id, edge.node1.name)
            def node2 = new Node(edge.node2.id, edge.node2.name)
            assertTrue(graph.edgeSet.contains(new Edge(node1, node2)))
        }
    }


}
