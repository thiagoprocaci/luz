package br.com.tbp.printer;


import br.com.tbp.file.FileManager;
import br.com.tbp.model.Edge;
import br.com.tbp.model.Graph;
import br.com.tbp.model.Node;

public class GMLPrinter {

    private FileManager fileManager;

    public GMLPrinter(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void print(Graph graph) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("graph \n");
        buffer.append("[ \n");
        buffer.append("  directed 0 \n");

        for (Node node : graph.getNodeSet()) {
            buffer.append("  node \n");
            buffer.append("  [ \n");
            buffer.append("    id ");
            buffer.append(node.getId());
            buffer.append("\n");
            buffer.append("    label ");
            buffer.append("\"");
            buffer.append(node.getName());
            buffer.append("\"");
            buffer.append("\n");
            buffer.append("  ] \n");
        }

        for (Edge edge: graph.getEdgeSet()) {
            buffer.append("  edge \n");
            buffer.append("  [ \n");
            buffer.append("    source ");
            buffer.append(edge.getNode1().getId());
            buffer.append("\n");
            buffer.append("    target ");
            buffer.append(edge.getNode2().getId());
            buffer.append("\n");
            buffer.append("    weight ");
            buffer.append(edge.getWeight());
            buffer.append("\n");
            buffer.append("  ] \n");
        }

        buffer.append("] \n");
        fileManager.save(buffer.toString());

    }
}

