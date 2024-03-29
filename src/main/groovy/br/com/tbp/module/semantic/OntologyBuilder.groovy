package br.com.tbp.module.semantic

import br.com.tbp.file.FileManager
import br.com.tbp.file.FileReader
import br.com.tbp.model.*

class OntologyBuilder {

    def File buildOntology(Graph graph) {
        def buffer = new StringBuffer()
        def community = new HashMap<Integer, List<Node>>()
        def messageMap = new HashMap<String, List<Message>>()

        buildMetadata(graph, buffer)
        buildNodes(graph.nodeSet, buffer, community, messageMap)
        buildEdges(graph.edgeSet, buffer)
        buildCommunity(community, buffer)
        messageMap.keySet().each { key ->
            buildMessages(messageMap.get(key), buffer)
        }
        buildTokens(graph.getTokens(), buffer)

        def fileContent = FileReader.readFile("src/main/resources/networkTemplate.owl");
        fileContent = fileContent.replace("#INSTANCES", buffer.toString())
        FileManager fileManager = new FileManager()
        fileManager.save(fileContent, "ontology.xml")
    }

    private void buildMetadata(Graph graph, StringBuffer buffer) {
        buffer.append("    <owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
        buffer.append(graph.id)
        buffer.append("\"> \n")
        buffer.append("        <rdf:type rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#NetworkMetadata\"/> \n")
        buffer.append("        <networkAvgDist rdf:datatype=\"&xsd;double\">")
        buffer.append(graph.avgDist)
        buffer.append("</networkAvgDist> \n")
        buffer.append("        <networkModularity rdf:datatype=\"&xsd;double\">")
        buffer.append(graph.modularity)
        buffer.append("</networkModularity> \n")
        buffer.append("        <networkModularityResolution rdf:datatype=\"&xsd;double\">")
        buffer.append(graph.modularityResolution)
        buffer.append("</networkModularityResolution> \n")
        buffer.append("        <networkRadius rdf:datatype=\"&xsd;integer\">")
        buffer.append(graph.radius)
        buffer.append("</networkRadius> \n")
        buffer.append("        <networkNumberOfCommunities rdf:datatype=\"&xsd;integer\">")
        buffer.append(graph.numberOfCommunities)
        buffer.append("</networkNumberOfCommunities> \n")
        buffer.append("        <networkDiameter rdf:datatype=\"&xsd;integer\">")
        buffer.append(graph.diameter)
        buffer.append("</networkDiameter> \n")
        buffer.append("        <networkShortestPaths rdf:datatype=\"&xsd;integer\">")
        buffer.append(graph.shortestPaths)
        buffer.append("</networkShortestPaths> \n")
        buffer.append("    </owl:NamedIndividual> \n")
    }


    private void buildCommunity(HashMap<Integer, List<Node>> community, StringBuffer buffer) {
        def keys = community.keySet()
        keys.each { key ->
            buffer.append("    <owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
            buffer.append(key)
            buffer.append("\"> \n")
            buffer.append("        <rdf:type rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#Community\"/> \n")
            buffer.append("        <communityId rdf:datatype=\"&xsd;string\">")
            buffer.append(key)
            buffer.append("</communityId> \n")
            def list = community.get(key)
            list.each { node ->
                buffer.append("        <hasUser rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
                buffer.append(node.id)
                buffer.append("\"/> \n")
            }
            buffer.append("    </owl:NamedIndividual> \n")
        }
    }

    private void buildEdges(Set<Edge> edges, StringBuffer buffer) {
        edges.each { edge ->
            buffer.append("    <owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
            buffer.append(edge.id)
            buffer.append("\"> \n")
            buffer.append("        <hasSourceNode rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
            buffer.append(edge.node1.id)
            buffer.append("\"/> \n")
            buffer.append("        <hasDestNode rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
            buffer.append(edge.node2.id)
            buffer.append("\"/> \n")
            buffer.append("        <rdf:type rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#Edge\"/> \n")
            buffer.append("        <edgeWeight rdf:datatype=\"&xsd;float\">")
            buffer.append(edge.weight)
            buffer.append("</edgeWeight> \n")
            buffer.append("        <edgeId rdf:datatype=\"&xsd;string\">")
            buffer.append(edge.id)
            buffer.append("</edgeId> \n")
            buffer.append("    </owl:NamedIndividual> \n")
        }
    }

    private void buildNodes(Set<Node> nodes, StringBuffer buffer, HashMap<Integer, List<Node>> community, HashMap<String, List<Message>> messageMap) {
        nodes.each { node ->
            buffer.append("    <owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
            buffer.append(node.id)
            buffer.append("\"> \n")
            buffer.append("        <rdf:type rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#User\"/> \n")
            buffer.append("        <userDegree rdf:datatype=\"&xsd;double\">")
            buffer.append(node.degree)
            buffer.append("</userDegree> \n")
            buffer.append("        <userPagerank rdf:datatype=\"&xsd;double\">")
            buffer.append(node.pagerank)
            buffer.append("</userPagerank> \n")
            buffer.append("        <userCloseness rdf:datatype=\"&xsd;double\">")
            buffer.append(node.closeness)
            buffer.append("</userCloseness> \n")
            buffer.append("        <userBetweenness rdf:datatype=\"&xsd;double\">")
            buffer.append(node.betweenness)
            buffer.append("</userBetweenness> \n")
            buffer.append("        <userEccentricity rdf:datatype=\"&xsd;double\">")
            buffer.append(node.eccentricity)
            buffer.append("</userEccentricity> \n")
            buffer.append("        <userId rdf:datatype=\"&xsd;string\">")
            buffer.append(node.id)
            buffer.append("</userId> \n")
            buffer.append("        <userName rdf:datatype=\"&xsd;string\">")
            buffer.append(node.name)
            buffer.append("</userName> \n")
            buffer.append("    </owl:NamedIndividual> \n")

            if (community.get(node.modularityClass) == null) {
                community.put(node.modularityClass, new ArrayList<Node>())
            }
            community.get(node.modularityClass).add(node)
            messageMap.put(node.id, node.messages)
        }
    }

    private void buildTokens(Set<Token> tokens, StringBuffer buffer) {
        StringBuffer bufferFrequency = null
        for (Token token : tokens) {
            bufferFrequency = new StringBuffer()
            buffer.append("    <owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
            buffer.append(token.getId())
            buffer.append("\"> \n")
            buffer.append("        <rdf:type rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#Token\"/> \n")
            buffer.append("        <tokenId rdf:datatype=\"&xsd;string\">")
            buffer.append(token.getId())
            buffer.append("</tokenId> \n")
            buffer.append("        <tokenWord rdf:datatype=\"&xsd;string\">")
            buffer.append(token.getWord())
            buffer.append("</tokenWord> \n")

            for (Node node : token.getFrequency().keySet()) {
                buffer.append("        <hasTokenFrequency rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
                buffer.append(token.getId())
                buffer.append("-")
                buffer.append(node.getId())
                buffer.append("\"/> \n")

                bufferFrequency.append("    <owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
                bufferFrequency.append(token.getId())
                bufferFrequency.append("-")
                bufferFrequency.append(node.getId())
                bufferFrequency.append("\"> \n")
                bufferFrequency.append("        <rdf:type rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#TokenFrequency\"/> \n")
                bufferFrequency.append("        <tokenFrequencyCount rdf:datatype=\"&xsd;integer\">")
                bufferFrequency.append(token.getFrequency().get(node))
                bufferFrequency.append("</tokenFrequencyCount> \n")
                bufferFrequency.append("        <tokenFrequencyId rdf:datatype=\"&xsd;string\">")
                bufferFrequency.append(token.getId())
                bufferFrequency.append("-")
                bufferFrequency.append(node.getId())
                bufferFrequency.append("</tokenFrequencyId> \n")
                bufferFrequency.append("        <belongsToUser rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
                bufferFrequency.append(node.getId())
                bufferFrequency.append("\"/> \n")
                bufferFrequency.append("        <belongsToToken rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
                bufferFrequency.append(token.getId())
                bufferFrequency.append("\"/> \n")
                bufferFrequency.append("    </owl:NamedIndividual> \n");
            }
            buffer.append("    </owl:NamedIndividual> \n");
            buffer.append(bufferFrequency)


        }
    }

    private void buildMessages(List<Message> messages, StringBuffer buffer) {
        messages.each { m ->
            buffer.append("    <owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
            buffer.append(m.id)
            buffer.append("\"> \n")
            buffer.append("        <rdf:type rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#Message\"/> \n")
            buffer.append("        <messageCreatedTime rdf:datatype=\"&xsd;dateTime\">")
            buffer.append(m.createdTime)
            buffer.append("</messageCreatedTime> \n")
            buffer.append("        <messageContent rdf:datatype=\"&xsd;string\"><![CDATA[")
            buffer.append(m.content)
            buffer.append("]]></messageContent> \n")
            buffer.append("        <messageId rdf:datatype=\"&xsd;string\">")
            buffer.append(m.id)
            buffer.append("</messageId> \n")
            buffer.append("        <hasAuthor rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
            buffer.append(m.author.id)
            buffer.append("\"/> \n")
            if (m.previous != null) {
                buffer.append("        <hasPreviousMessage rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
                buffer.append(m.previous.id)
                buffer.append("\"/> \n")
            }
            if (m.next != null) {
                buffer.append("        <hasNextMessage rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
                buffer.append(m.next.id)
                buffer.append("\"/> \n")
            }
            buffer.append("    </owl:NamedIndividual> \n");
        }
    }
}
