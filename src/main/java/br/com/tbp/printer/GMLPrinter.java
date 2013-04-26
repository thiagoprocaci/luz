package br.com.tbp.printer;


import br.com.tbp.file.FileManager;
import br.com.tbp.model.Edge;
import br.com.tbp.model.Graph;
import br.com.tbp.model.User;

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

        for (User user: graph.getUserSet()) {
            buffer.append("  node \n");
            buffer.append("  [ \n");
            buffer.append("    id ");
            buffer.append(user.getId());
            buffer.append("\n");
            buffer.append("    label ");
            buffer.append("\"");
            buffer.append(user.getName());
            buffer.append("\"");
            buffer.append("\n");
            buffer.append("  ] \n");
        }

        for (Edge edge: graph.getEdgeSet()) {
            buffer.append("  edge \n");
            buffer.append("  [ \n");
            buffer.append("    source ");
            buffer.append(edge.getUser1().getId());
            buffer.append("\n");
            buffer.append("    target ");
            buffer.append(edge.getUser2().getId());
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

