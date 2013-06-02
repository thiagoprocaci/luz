package br.com.tbp.module.pnl.preprocessor;


import br.com.tbp.model.Graph;
import br.com.tbp.model.Message;
import br.com.tbp.model.Node;
import br.com.tbp.model.Token;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MessageGraphTokenizer {
    private PortugueseTokenizer portugueseTokenizer;

    public MessageGraphTokenizer(PortugueseTokenizer portugueseTokenizer) {
        this.portugueseTokenizer = portugueseTokenizer;
    }

    public void buildTokens(Graph graph) throws IOException {
        Map<Node, String> messages = getMessages(graph);
        Set<Token> tokens = findTokens(messages);
        graph.setTokens(tokens);
    }

    private Set<Token> findTokens(Map<Node, String> messages) throws IOException {
        Map<Token, Token> tokens = new HashMap<Token, Token>();
        Map<String, Integer> tokenCount = null;
        Token token = null;
        for (Node node : messages.keySet()) {
            tokenCount = portugueseTokenizer.tokenizer(messages.get(node));
            for (String string : tokenCount.keySet()) {
                token = new Token(string, string);
                if (!tokens.containsKey(token)) {
                    token.setFrequency(new HashMap<Node, Integer>());
                    tokens.put(token, token);
                }
                token = tokens.get(token);
                token.getFrequency().put(node, tokenCount.get(string));
            }
        }
        return new HashSet<Token>(tokens.values());
    }


    private Map<Node, String> getMessages(Graph graph) {
        StringBuffer buffer = null;
        Map<Node, String> messageMap = new HashMap<Node, String>();
        for (Node node : graph.getNodeSet()) {
            buffer = new StringBuffer();
            for (Message message : node.getMessages()) {
                if (message.getContent() != null) {
                    buffer.append(message.getContent());
                    buffer.append(" ");
                }
                messageMap.put(node, buffer.toString());
            }
        }
        return messageMap;
    }


}
