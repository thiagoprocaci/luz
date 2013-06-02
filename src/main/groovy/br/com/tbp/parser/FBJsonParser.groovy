package br.com.tbp.parser

import br.com.tbp.model.Edge
import br.com.tbp.model.Graph
import br.com.tbp.model.Message
import br.com.tbp.model.Node
import groovy.json.JsonSlurper

class FBJsonParser {

    def Graph parse(String jsonText) {
        def json = new JsonSlurper().parseText(jsonText)
        def graph = buildGraph(json)
        return graph
    }

    private Graph buildGraph(json) {
        Graph graph = new Graph();
        Map<Edge, Edge> edgeMap = new HashMap<Edge, Edge>()
        List<Node> userList = new ArrayList<Node>()
        Node user1 = null
        Node user2 = null
        Map<Node, List<Message>> commentsMap = new HashMap<Node, List<Message>>()

        // iteracao nos posts
        def posts = json.data
        posts.each { post ->
            // recupera autor do post
            def postAuthor = post.from
            user1 = new Node(postAuthor.id, postAuthor.name)
            userList.add(user1)
            def message = new Message(post.id, post.message, getDate(post.created_time.trim()), user1)
            def previousMessage = addMessage(user1, message, null, commentsMap)
            // iteracao nos comentarios do post
            if (post.comments != null && post.comments.data != null && post.comments.data.from != null) {
                post.comments.data.each { data ->
                    user2 = new Node(data.from.id, data.from.name)
                    userList.add(user2)
                    message = new Message(data.id, data.message, getDate(data.created_time.trim()), user2)
                    previousMessage = addMessage(user2, message, previousMessage, commentsMap)
                }

            }
            createEdge(userList, edgeMap)
            graph.getNodeSet().addAll(userList)
            userList.clear()
        }
        graph.getEdgeSet().addAll(edgeMap.values())
        graph.getNodeSet().each { n ->
            n.setMessages(commentsMap.get(n))
        }

        return graph
    }

    private Date getDate(String s) {
        Integer year = Integer.valueOf(s.subSequence(0, 4).toString());
        Integer month = Integer.valueOf(s.subSequence(5, 7).toString());
        Integer day = Integer.valueOf(s.subSequence(8, 10).toString());
        Integer hour = Integer.valueOf(s.subSequence(11, 13).toString());
        Integer min = Integer.valueOf(s.subSequence(14, 16).toString());
        Integer sec = Integer.valueOf(s.subSequence(17, 19).toString());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        return calendar.getTime()
    }


    private Message addMessage(Node node, Message message, Message previousMessage, Map<Node, List<Message>> commentsMap) {
        if (message != null) {
            if (!commentsMap.containsKey(node)) {
                commentsMap.put(node, new ArrayList<Message>())
            }
            if (previousMessage != null) {
                previousMessage.next = message
                message.previous = previousMessage
            }
            commentsMap.get(node).add(message)
        }
        return message
    }


    private void createEdge(userList, edgeMap) {
        // percorrer a lista de traz pra frente
        int maxIndex = userList.size() - 1
        for (int i = maxIndex; i >= 1; i--) {
            for (int j = i - 1; j >= 0; j--) {
                createEdge(edgeMap, userList.get(i), userList.get(j))
            }
        }
    }

    private void createEdge(edgeMap, user1, user2) {
        if (!user1.equals(user2)) {
            Edge edge = new Edge(user1, user2)
            if (edgeMap.get(edge) != null) {
                edge = edgeMap.get(edge)
                edge.increaseWeight()
            } else {
                edgeMap.put(edge, edge)
            }
        }
    }
}
