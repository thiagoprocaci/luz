package br.com.tbp.algorithm

import br.com.tbp.support.FileReader
import br.com.tbp.model.Graph
import br.com.tbp.model.Node
import br.com.tbp.model.Edge
import br.com.tbp.parser.FBJsonParser


class ModularityTest extends GroovyTestCase {


    private String xmlString
    private Modularity modularity


    public ModularityTest() {
        xmlString = FileReader.readFile("src/test/resources/br/com/tbp/algorithm/PowerGrid.graphml")
        modularity = new Modularity()
    }


    void testCompute2() {

        def xml = new XmlSlurper().parseText(xmlString)
        def nodes = xml.graph.node
        def edges = xml.graph.edge

        def nodeMap = new HashMap<String, Node>()

        nodes.each { node ->
            def n = new Node(node.@id.text(), node.@id.text())
            nodeMap.put(n.id, n)
        }

         def edgeMap = new HashMap<String, Edge>()

        edges.each { edge ->
            def node1 = nodeMap.get(edge.@source.text())
            def node2 = nodeMap.get(edge.@target.text())
            def e = new Edge(node1, node2)
            e.setWeight(1F)
            edgeMap.put(e.id, e)
        }

        Graph graph = new Graph();
        graph.getEdgeSet().addAll(edgeMap.values())
        graph.getNodeSet().addAll(nodeMap.values())

        modularity.execute(graph, false, false)

        assertTrue(0.93 < graph.getModularity())
        assertTrue(0.93 < graph.getModularityResolution())
        assertTrue(30 < graph.getNumberOfCommunities() && graph.getNumberOfCommunities() < 40)

    }
}