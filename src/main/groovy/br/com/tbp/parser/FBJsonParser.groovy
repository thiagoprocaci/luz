package br.com.tbp.parser

import groovy.json.*
import br.com.tbp.model.Node
import br.com.tbp.model.Edge
import br.com.tbp.model.Graph

class FBJsonParser {

    def Graph parse(String jsonText) {
        def json = new JsonSlurper().parseText(jsonText)
        def graph =  buildGraph(json)
        return graph
    }

    private Graph buildGraph(json) {
        Graph graph = new Graph();
        Map<Edge,Edge> edgeMap = new HashMap<Edge,Edge>()
        List<Node> userList = new ArrayList<Node>()
        Node user1 = null
        Node user2 = null

        // iteracao nos posts
        def posts = json.data
        posts.each { post ->

            // recupera autor do post
            def postAuthor = post.from
            user1 = new Node(postAuthor.id, postAuthor.name)
            userList.add(user1)
            // iteracao nos comentarios do post
            if(post.comments != null && post.comments.data != null && post.comments.data.from != null) {
                def commentsAuthors = post.comments.data.from
                commentsAuthors.each { ca ->
                    user2 = new Node(ca.id, ca.name)
                    userList.add(user2)
                }
            }
            createEdge(userList, edgeMap)
            graph.getNodeSet().addAll(userList)
            userList.clear()
        }
        graph.getEdgeSet().addAll(edgeMap.values())
        return graph
    }

    private void createEdge(userList, edgeMap) {
        // percorrer a lista de traz pra frente
        int maxIndex = userList.size() - 1
        for (int i = maxIndex; i >= 1; i--) {
            for (int j = i -1; j >= 0; j--) {
                 createEdge(edgeMap, userList.get(i), userList.get(j))
            }
        }
    }

    private void createEdge(edgeMap, user1, user2) {
        if(!user1.equals(user2)) {
            Edge edge = new Edge(user1, user2)
            if(edgeMap.get(edge) != null) {
                edge = edgeMap.get(edge)
                edge.increaseWeight()
            } else  {
                edgeMap.put(edge, edge)
            }
        }
    }
}
