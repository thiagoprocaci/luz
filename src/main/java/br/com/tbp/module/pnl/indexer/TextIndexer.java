package br.com.tbp.module.pnl.indexer;

import br.com.tbp.module.pnl.preprocessor.PortugueseAnalyzer;
import br.com.tbp.module.pnl.support.Doc;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


import java.io.File;
import java.io.IOException;

import java.util.Set;

public class TextIndexer {

    private PortugueseAnalyzer portugueseAnalyzer;

    public TextIndexer(PortugueseAnalyzer portugueseAnalyzer) {
        this.portugueseAnalyzer = portugueseAnalyzer;
    }


    public Directory index(Set<Doc> docs) throws IOException {
        Directory dir = FSDirectory.open(new File("src/main/resources/index".replace("/", File.separator)));
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, portugueseAnalyzer.getAnalyzer());
        IndexWriter indexWriter = new IndexWriter(dir, config);
        Document d = null;
        for (Doc doc : docs) {
            d = new Document();
            d.add(new Field("text", doc.getText(), Field.Store.YES, Field.Index.ANALYZED));
            d.add(new Field("id", doc.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            indexWriter.addDocument(d);
        }
        indexWriter.close();
        return dir;
    }

}
