package br.com.tbp.printer;


import br.com.tbp.file.FileManager;
import br.com.tbp.model.Graph;
import br.com.tbp.parser.FBJsonParser;
import br.com.tbp.support.FileReader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;

public class GMLPrinterTest {

    private String jsonString;
    private FBJsonParser fbJsonParser;
    private GMLPrinter gmlPrinter;
    private FileManager fileManager;

    @Before
    public void doBefore() {
        jsonString = FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt");
        fbJsonParser = new FBJsonParser();

        fileManager = new FileManager();
        gmlPrinter = new GMLPrinter(fileManager);
    }

    @Test
    public void testPrint() {
        Graph graph = fbJsonParser.parse(jsonString);
        gmlPrinter.print(graph);
        String expectedContent = FileReader.readFile("src/test/resources/br/com/tbp/printer/fb_.gml").trim().replaceAll("\\s","").replaceAll("\\n","");
        String content = FileReader.readFile("files" +  File.separator + "fb.gml").trim().replaceAll("\\s","").replaceAll("\\n","");
        assertEquals(expectedContent, content);
    }

}
