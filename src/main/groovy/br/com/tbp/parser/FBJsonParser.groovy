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
        Map<Node, List<String>>  commentsMap = new HashMap<Node, List<String>>()

        // iteracao nos posts
        def posts = json.data
        posts.each { post ->
            // recupera autor do post
            def postAuthor = post.from
            user1 = new Node(postAuthor.id, postAuthor.name)
            userList.add(user1)
            addComment(user1, post.message, commentsMap)
            // iteracao nos comentarios do post
            if(post.comments != null && post.comments.data != null && post.comments.data.from != null) {
                post.comments.data.each { data ->
                    user2 = new Node(data.from.id, data.from.name)
                    userList.add(user2)
                    addComment(user2, data.message, commentsMap)
                }

            }
            createEdge(userList, edgeMap)
            graph.getNodeSet().addAll(userList)
            userList.clear()
        }
        graph.getEdgeSet().addAll(edgeMap.values())
        graph.getNodeSet().each { n ->
            n.setComments(commentsMap.get(n))
        }
        return graph
    }

    private addComment(Node node, String comment, Map<Node, List<String>> commentsMap) {
        if(comment != null) {
            if(!commentsMap.containsKey(node)) {
                commentsMap.put(node, new ArrayList<String>())
            }
            commentsMap.get(node).add(comment)
        }
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
