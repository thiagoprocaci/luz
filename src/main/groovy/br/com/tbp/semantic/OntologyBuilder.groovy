package br.com.tbp.semantic

import br.com.tbp.file.FileManager
import br.com.tbp.model.Graph
import br.com.tbp.model.Node
import br.com.tbp.file.FileReader



class OntologyBuilder {

   // public static final String ONTOLOGY_PREFIX = 'http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#'


    def buildOntology(Graph graph) {
        def buffer = new StringBuffer()
        def community = new HashMap<Integer,List<Node>>()

        graph.nodeSet.each { node ->
            buffer.append("<owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
            buffer.append(node.id)
            buffer.append("\"> \n")
            buffer.append("    <rdf:type rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#User\"/> \n")
            buffer.append("    <userDegree rdf:datatype=\"&xsd;double\">")
            buffer.append(node.degree)
            buffer.append("</userDegree> \n")
            buffer.append("    <userPagerank rdf:datatype=\"&xsd;double\">")
            buffer.append(node.pagerank)
            buffer.append("</userPagerank> \n")
            buffer.append("    <userCloseness rdf:datatype=\"&xsd;double\">")
            buffer.append(node.closeness)
            buffer.append("</userCloseness> \n")
            buffer.append("    <userBetweenness rdf:datatype=\"&xsd;double\">")
            buffer.append(node.betweenness)
            buffer.append("</userBetweenness> \n")
            buffer.append("    <userEccentricity rdf:datatype=\"&xsd;double\">")
            buffer.append(node.eccentricity)
            buffer.append("</userEccentricity> \n")
            buffer.append("    <userId rdf:datatype=\"&xsd;string\">")
            buffer.append(node.id)
            buffer.append("</userId> \n")
            buffer.append("    <userName rdf:datatype=\"&xsd;string\">")
            buffer.append(node.name)
            buffer.append("</userName> \n")
            buffer.append("</owl:NamedIndividual> \n")

            if(community.get(node.modularityClass) == null) {
                community.put(node.modularityClass, new ArrayList<Node>())
            }
            community.get(node.modularityClass).add(node)
       }

       graph.edgeSet.each { edge ->
           buffer.append("<owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
           buffer.append(edge.id)
           buffer.append("\"> \n")
           buffer.append("    <hasSourceNode rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
           buffer.append(edge.node1.id)
           buffer.append("\"/> \n")
           buffer.append("    <hasDestNode rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
           buffer.append(edge.node2.id)
           buffer.append("\"/> \n")
           buffer.append("    <rdf:type rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#Edge\"/> \n")
           buffer.append("    <edgeWeight rdf:datatype=\"&xsd;float\">")
           buffer.append(edge.weight)
           buffer.append("</edgeWeight> \n")
           buffer.append("    <edgeId rdf:datatype=\"&xsd;string\">")
           buffer.append(edge.id)
           buffer.append("</edgeId> \n")
           buffer.append("</owl:NamedIndividual> \n")
       }

       def keys = community.keySet()
       keys.each { key ->
           buffer.append("<owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
           buffer.append(key)
           buffer.append("\"> \n")
           buffer.append("    <rdf:type rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#Community\"/> \n")
           buffer.append("    <communityId rdf:datatype=\"&xsd;string\">")
           buffer.append(key)
           buffer.append("</communityId> \n")
           def list = community.get(key)
           list.each { node ->
               buffer.append("    <hasUser rdf:resource=\"http://www.semanticweb.org/thiago/ontologies/2013/4/Ontology1369703745905.owl#")
               buffer.append(node.id)
               buffer.append("\"/> \n")
           }
           buffer.append("</owl:NamedIndividual> \n")
       }

        def fileContent = FileReader.readFile("src/main/resources/networkTemplate.owl");
        fileContent = fileContent.replace("#INSTANCES", buffer.toString())
        FileManager fileManager = new FileManager()
        fileManager.save(fileContent, "ontology.xml")

    }



}
