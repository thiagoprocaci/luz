package br.com.tbp.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import br.com.tpb.parser.FBJsonParser;
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
        fbJsonParser.parse(jsonString);
    }
}
