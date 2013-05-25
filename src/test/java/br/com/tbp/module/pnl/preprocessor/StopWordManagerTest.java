package br.com.tbp.module.pnl.preprocessor;


import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class StopWordManagerTest {

    private StopWordManager stopWordManager;

    @Before
    public void doBefore() {
        stopWordManager = new StopWordManager();
    }

    @Test
    public void testLoadStopWords() throws FileNotFoundException {
        Set<String> stopWords = stopWordManager.loadStopWords();
        assertNotNull(stopWords);
        assertEquals(345, stopWords.size());
        for (String string : stopWords) {
            assertNotNull(string);
        }
    }
}
