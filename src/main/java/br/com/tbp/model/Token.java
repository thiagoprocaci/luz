package br.com.tbp.model;


import java.util.Map;

public class Token extends CoreEntity {
    private String word;
    private Map<Node, Integer> frequency;

    public Token(String id, String word) {
        if (id == null) {
            throw new IllegalArgumentException("The user id can not be null");
        }
        this.word = word;
        setId(id);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Map<Node, Integer> getFrequency() {
        return frequency;
    }

    public void setFrequency(Map<Node, Integer> frequency) {
        this.frequency = frequency;
    }
}
