package br.com.tbp.module.graphanalysis.algorithm

import br.com.tbp.file.FileReader
import br.com.tbp.model.Edge
import br.com.tbp.model.Graph
import br.com.tbp.model.Node
import br.com.tbp.parser.FBJsonParser
import groovy.json.JsonSlurper

class GraphDistanceTest extends GroovyTestCase {

    private String jsonStringExpected;
    private String jsonString;
    private FBJsonParser fbJsonParser;
    private GraphDistance graphDistance;

    public GraphDistanceTest() {
        jsonString = FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt");
        fbJsonParser = new FBJsonParser();
        graphDistance = new GraphDistance();
        jsonStringExpected = FileReader.readFile("src/test/resources/br/com/tbp/parser/fb.txt")
    }

    void testCompute() {
        Graph graph = fbJsonParser.parse(jsonString);
        graphDistance.execute(graph);

        def json = new JsonSlurper().parseText(jsonStringExpected)
        def nodes = json.nodeSet
        def edges = json.edgeSet

        assertEquals(nodes.size(), graph.nodeSet.size())
        assertEquals(edges.size(), graph.edgeSet.size())

        def nodeMap = new HashMap<String, Node>()
        def edgeMap = new HashMap<String, Edge>()

        nodes.each { node ->
            Node n = new Node(node.id, node.name)
            n.betweenness = node.betweenness
            n.closeness = node.closeness
            n.eccentricity = node.eccentricity
            nodeMap.put(n.id, n)
        }

        edges.each { edge ->
            def node1 = new Node(edge.node1.id, edge.node1.name)
            def node2 = new Node(edge.node2.id, edge.node2.name)
            def e = new Edge(nodeMap.get(node1.id), nodeMap.get(node2.id))
            e.weight = edge.weight
            edgeMap.put(e.id, e)
        }

        assertEquals(json.diameter, graph.getDiameter())
        assertEquals(json.avgDist, graph.getAvgDist())
        assertEquals(json.shortestPaths, graph.getShortestPaths())
        assertEquals(json.radius, graph.getRadius())

        graph.nodeSet.each { node ->
            Node n = nodeMap.get(node.id)
            assertEquals(n.betweenness, node.betweenness)
            assertEquals(n.eccentricity, node.eccentricity)
            assertEquals(n.closeness, node.closeness)
        }

        graph.edgeSet.each { e ->
            Edge e2 = edgeMap.get(e.id)
            assertEquals(e2.weight, e.weight)
        }
    }

}
