package br.com.tbp.module;


import br.com.tbp.model.Graph;
import br.com.tbp.module.graphanalysis.algorithm.Degree;
import br.com.tbp.module.graphanalysis.algorithm.GraphDistance;
import br.com.tbp.module.graphanalysis.algorithm.Modularity;
import br.com.tbp.module.graphanalysis.algorithm.PageRank;
import br.com.tbp.module.pnl.preprocessor.MessageGraphTokenizer;
import br.com.tbp.module.semantic.NetworkSemanticStructure;
import br.com.tbp.module.semantic.OntologyBuilder;
import br.com.tbp.parser.FBJsonParser;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NetworkSemanticStructureTest {

    private Degree degree;
    private GraphDistance graphDistance;
    private Modularity modularity;
    private PageRank pageRank;
    private FBJsonParser fbJsonParser;
    private OntologyBuilder ontologyBuilder;
    private NetworkSemanticStructure networkSemanticStructure;
    private MessageGraphTokenizer messageGraphTokenizer ;

    @Before
    public void doBefore() {
        degree = mock(Degree.class);
        graphDistance = mock(GraphDistance.class);
        modularity = mock(Modularity.class);
        pageRank = mock(PageRank.class);
        fbJsonParser = mock(FBJsonParser.class);
        ontologyBuilder = mock(OntologyBuilder.class);
        messageGraphTokenizer = mock(MessageGraphTokenizer.class);
        networkSemanticStructure = new NetworkSemanticStructure(degree, graphDistance, modularity, pageRank, fbJsonParser, ontologyBuilder,messageGraphTokenizer);
    }

    @Test
    public void testBuild() throws IOException {
        String text = "text";
        Graph graph = new Graph();
        graph.setId("graph");
        when(fbJsonParser.parse(text)).thenReturn(graph);
        File f = new File("");
        when(ontologyBuilder.buildOntology(graph)).thenReturn(f);

        File f2 = networkSemanticStructure.build(text);
        verify(fbJsonParser).parse(text);
        verify(degree).execute(graph);
        verify(graphDistance).execute(graph);
        verify(modularity).execute(graph, true, true);
        verify(pageRank).execute(graph, true);
        verify(messageGraphTokenizer).buildTokens(graph);
        verify(ontologyBuilder).buildOntology(graph);
        Assert.assertEquals(f, f2);

    }


}
