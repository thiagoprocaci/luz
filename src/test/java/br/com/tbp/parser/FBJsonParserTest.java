package br.com.tbp.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import br.com.tbp.algorithm.GraphDistance;
import br.com.tbp.file.FileManager;
import br.com.tbp.model.Graph;
import br.com.tbp.model.User;
import br.com.tbp.printer.GMLPrinter;
import br.com.tbp.parser.FBJsonParser;
import org.junit.Before;
import org.junit.Test;

public class FBJsonParserTest {
    private String jsonString;
    private FBJsonParser fbJsonParser;

    private String read(File file) throws IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(file));
            while ((sCurrentLine = br.readLine()) != null) {
                buffer.append(sCurrentLine);
                buffer.append("\n");
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return buffer.toString();
    }

    @Before
    public void doBefore() throws IOException {
        String path = "src/test/resources/br/com/tbp/parser/feed_cibercultura.txt";
        path = path.replace("/", File.separator);
        File file = new File(path);
        jsonString = read(file);
        fbJsonParser = new FBJsonParser();
    }

    @Test
    public void testParse() throws  IOException {
       Graph graph = fbJsonParser.parse(jsonString);
        FileManager fileManager = new FileManager();
       GMLPrinter printer = new GMLPrinter(fileManager);
    //   printer.print(graph);
        GraphDistance graphDistance = new GraphDistance();
        graphDistance.execute(graph);

        System.out.println("Diameter " + graph.getDiameter());
        System.out.println("AvgDist " + graph.getAvgDist());
        System.out.println("Radius " + graph.getRadius());
        System.out.println("ShortestPaths " + graph.getShortestPaths());

        System.out.println("===========================");

        for (User user: graph.getNodeSet()) {
            System.out.println("Nome " + user.getName());
            System.out.println("Betweenness " + user.getBetweenness());
            System.out.println("Eccentricity " + user.getEccentricity());
            System.out.println("Closeness " + user.getCloseness());
            System.out.println("===========================");
        }
    }
}
