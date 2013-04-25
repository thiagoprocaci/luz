package br.com.tpb.parser

import groovy.json.*
import br.com.tbp.model.User
import br.com.tbp.model.Edge

class FBJsonParser {

    def parse(String jsonText) {
        def json = new JsonSlurper().parseText(jsonText)
        def edgeSet =  buildGraph(json)

        edgeSet.each { edge ->
            System.out.println(edge.user1.name)
            System.out.println(edge.user2.name)
            System.out.println(edge.weight)
            System.out.println("=========================")
        }

    }

    private Set<User> buildUser(json) {
        Set<User> userSet = new HashSet<User>()
        //autores dos posts
        def postAuthors = json.data.from
        postAuthors.each { author ->
            userSet.add(new User(author.id, author.name))
        }
        // comentarios dos posts
        def commentsAuthors = json.data.comments.data.from
        commentsAuthors.each { cas ->
            cas.each  { ca ->
                userSet.add(new User(ca.id, ca.name))
            }
        }
        // talvez considerar os likes depois..
        return  userSet
    }

    private Set<Edge> buildGraph(json) {
        Map<Edge,Edge> edgeMap = new HashMap<Edge,Edge>()
        User user1 = null
        User user2 = null
        Edge edge = null

        // iteracao nos posts
        def posts = json.data
        posts.each { post ->
            // recupera autor do post
            def postAuthor = post.from
            user1 = new User(postAuthor.id, postAuthor.name)

            // iteracao nos comentarios do post
            def commentsAuthors = posts.comments.data.from
            commentsAuthors.each { cas ->
                cas.each  { ca ->
                    user2 = new User(ca.id, ca.name)
                    edge = new Edge(user1, user2)
                    if(edgeMap.get(edge) != null) {
                      edge = edgeMap.get(edge)
                      edge.increaseWeight()
                    } else  {
                        edgeMap.put(edge, edge)
                    }
                }
            }
        }
        return edgeMap.values()

    }

}
