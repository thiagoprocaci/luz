package br.com.tbp.module.pnl.mining;


import br.com.tbp.module.pnl.indexer.TextIndexer;
import br.com.tbp.module.pnl.preprocessor.PortugueseAnalyzer;
import br.com.tbp.module.pnl.support.Doc;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.util.Set;

public class QueryProcessor {

    private PortugueseAnalyzer portugueseAnalyzer;
    private TextIndexer textIndexer;

    public QueryProcessor(PortugueseAnalyzer portugueseAnalyzer, TextIndexer textIndexer) {
        this.portugueseAnalyzer = portugueseAnalyzer;
        this.textIndexer = textIndexer;
    }

    public void runQuery(String string, Set<Doc> docs) throws IOException, ParseException {
        Directory dir = textIndexer.index(docs);
        IndexReader reader = IndexReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        QueryParser parser = new QueryParser(Version.LUCENE_35, "text", portugueseAnalyzer.getAnalyzer());

        Query query = parser.parse(string);
        //TopDocs hits = searcher.search(query,100);
        TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
        searcher.search(query, collector);
        ScoreDoc[] scoreDocs = collector.topDocs().scoreDocs;

        System.out.println(scoreDocs.length);
        for (ScoreDoc sd : scoreDocs) {
            Document d = searcher.doc(sd.doc);
            System.out.println("Arquivo: " + d);

            System.out.println("Detalhando os resultados:");
            System.out.println(searcher.explain(query, sd.doc));
            System.out.println("**********************************************");
        }

        searcher.close();
        reader.close();

    }
}
