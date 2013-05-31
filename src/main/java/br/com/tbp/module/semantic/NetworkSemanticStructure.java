package br.com.tbp.module.semantic;


import br.com.tbp.model.Graph;
import br.com.tbp.module.graphanalysis.algorithm.Degree;
import br.com.tbp.module.graphanalysis.algorithm.GraphDistance;
import br.com.tbp.module.graphanalysis.algorithm.Modularity;
import br.com.tbp.module.graphanalysis.algorithm.PageRank;
import br.com.tbp.parser.FBJsonParser;

import java.io.File;

public class NetworkSemanticStructure {

    private Degree degree;
    private GraphDistance graphDistance;
    private Modularity modularity;
    private PageRank pageRank;
    private FBJsonParser fbJsonParser;
    private OntologyBuilder ontologyBuilder;

    public NetworkSemanticStructure(Degree degree, GraphDistance graphDistance, Modularity modularity, PageRank pageRank, FBJsonParser fbJsonParser, OntologyBuilder ontologyBuilder) {
        this.degree = degree;
        this.graphDistance = graphDistance;
        this.modularity = modularity;
        this.pageRank = pageRank;
        this.fbJsonParser = fbJsonParser;
        this.ontologyBuilder = ontologyBuilder;
    }

    public File build(String unstructuredData) {
        // facade que executa todos os passos para a estruturação inicial rede
        Graph graph = fbJsonParser.parse(unstructuredData);
        degree.execute(graph);
        graphDistance.execute(graph);
        modularity.execute(graph, true, true);
        pageRank.execute(graph,true);
        return ontologyBuilder.buildOntology(graph);
    }

}
