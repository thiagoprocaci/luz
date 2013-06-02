package br.com.tbp.module.semantic;


import br.com.tbp.model.Graph;
import br.com.tbp.module.graphanalysis.algorithm.Degree;
import br.com.tbp.module.graphanalysis.algorithm.GraphDistance;
import br.com.tbp.module.graphanalysis.algorithm.Modularity;
import br.com.tbp.module.graphanalysis.algorithm.PageRank;
import br.com.tbp.module.pnl.preprocessor.MessageGraphTokenizer;
import br.com.tbp.module.pnl.preprocessor.PortugueseAnalyzer;
import br.com.tbp.module.pnl.preprocessor.PortugueseTokenizer;
import br.com.tbp.module.pnl.preprocessor.StopWordManager;
import br.com.tbp.parser.FBJsonParser;

import java.io.File;
import java.io.IOException;

public class NetworkSemanticStructure {

    private Degree degree;
    private GraphDistance graphDistance;
    private Modularity modularity;
    private PageRank pageRank;
    private FBJsonParser fbJsonParser;
    private OntologyBuilder ontologyBuilder;
    private MessageGraphTokenizer messageGraphTokenizer ;


    public NetworkSemanticStructure(Degree degree, GraphDistance graphDistance, Modularity modularity, PageRank pageRank, FBJsonParser fbJsonParser, OntologyBuilder ontologyBuilder, MessageGraphTokenizer messageGraphTokenizer) {
        this.degree = degree;
        this.graphDistance = graphDistance;
        this.modularity = modularity;
        this.pageRank = pageRank;
        this.fbJsonParser = fbJsonParser;
        this.ontologyBuilder = ontologyBuilder;
        this.messageGraphTokenizer = messageGraphTokenizer;
    }

    public File build(String unstructuredData) throws IOException {
        // facade que executa todos os passos para a estruturação inicial rede
        Graph graph = fbJsonParser.parse(unstructuredData);
        degree.execute(graph);
        graphDistance.execute(graph);
        modularity.execute(graph, true, true);
        pageRank.execute(graph,true);
        messageGraphTokenizer.buildTokens(graph);
        return ontologyBuilder.buildOntology(graph);
    }

    public static void main(String... a) throws IOException {
        Degree degree = new Degree();
        GraphDistance graphDistance = new GraphDistance();
        Modularity modularity = new Modularity();
        PageRank pageRank = new PageRank();
        FBJsonParser fbJsonParser = new FBJsonParser();
        OntologyBuilder ontologyBuilder = new OntologyBuilder()  ;
        StopWordManager stopWordManager = new StopWordManager();
        PortugueseAnalyzer portugueseAnalyzer = new PortugueseAnalyzer(stopWordManager);
        PortugueseTokenizer portugueseTokenizer = new PortugueseTokenizer(portugueseAnalyzer);
        MessageGraphTokenizer messageGraphTokenizer = new MessageGraphTokenizer(portugueseTokenizer);

        NetworkSemanticStructure networkSemanticStructure = new NetworkSemanticStructure(degree,graphDistance,modularity,pageRank,fbJsonParser, ontologyBuilder,messageGraphTokenizer);
        String jsonString = br.com.tbp.file.FileReader.readFile("src/test/resources/br/com/tbp/parser/feed_cibercultura.txt");
        networkSemanticStructure.build(jsonString);
    }

}
