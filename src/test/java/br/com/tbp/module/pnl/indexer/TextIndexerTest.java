package br.com.tbp.module.pnl.indexer;


import br.com.tbp.file.FileReader;
import br.com.tbp.module.pnl.preprocessor.PortugueseAnalyzer;
import br.com.tbp.module.pnl.preprocessor.StopWordManager;
import br.com.tbp.module.pnl.support.Doc;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TextIndexerTest {

    private TextIndexer textIndexer;
    private String jsonString;

    @Before
    public void doBefore() {
        StopWordManager stopWordManager = new StopWordManager();
        PortugueseAnalyzer portugueseAnalyzer = new PortugueseAnalyzer(stopWordManager);
        textIndexer = new TextIndexer(portugueseAnalyzer);
        jsonString = FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt");
        cleanFolder();
    }

    @After
    public void doAfter() {
        cleanFolder();
    }

    @Test
    public void index() throws IOException {
        Doc doc = new Doc("id", jsonString);
        Set<Doc> docs = new HashSet<Doc>();
        docs.add(doc);
        textIndexer.index(docs);
        File file = new File("src/main/resources/index".replace("/", File.separator));
        File[] files = file.listFiles();
        Assert.assertNotNull(files);
        Assert.assertEquals(11, files.length);
        for (File f : files) {
            Assert.assertNotNull(f);
        }
    }

    private void cleanFolder() {
        File file = new File("src/main/resources/index".replace("/", File.separator));
        File[] files = file.listFiles();
        // limpa lixo
        for (File f : files) {
            if(!f.getName().contains("a.txt")) {
                f.delete();
            }
        }
    }
}
