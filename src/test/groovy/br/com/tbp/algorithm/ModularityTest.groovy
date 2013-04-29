package br.com.tbp.algorithm

import br.com.tbp.parser.FBJsonParser
import br.com.tbp.support.FileReader
import br.com.tbp.model.Graph
import br.com.tbp.model.Node
import br.com.tbp.printer.GMLPrinter
import br.com.tbp.file.FileManager


class ModularityTest extends GroovyTestCase {

    private String jsonStringExpected;
    private String jsonString;
    private FBJsonParser fbJsonParser;
    private Modularity modularity;

    public ModularityTest() {
        jsonString = FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt")
        fbJsonParser = new FBJsonParser()
        modularity = new Modularity()
        jsonStringExpected = FileReader.readFile("src/test/resources/br/com/tbp/parser/fb.txt")
    }

    void testCompute() {
        Graph graph = fbJsonParser.parse(jsonString);
        FileManager fileManager = new FileManager();
        GMLPrinter printer = new GMLPrinter(fileManager);

        printer.print(graph)
        modularity.setRandom(true)
        modularity.setUseWeight(true)
        modularity.execute(graph)

        println(graph.getModularity())
        println(graph.getModularityResolution())
        println(graph.getNumberOfCommunities())

        for (Node node: graph.getNodeSet()) {
            println(node.name + " " + node.modularityClass);
        }
    }
}