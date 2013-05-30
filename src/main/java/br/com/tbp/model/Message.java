package br.com.tbp.model;



import java.util.Date;

public class Message extends CoreEntity {
    private String content;
    private Message next;
    private Message previous;
    private Date createdTime;
    private Node author;

    public Message(String id, String content, Date createdTime, Node author) {
        this.content = (content == null ? "" : content.trim()) ;
        this.createdTime = createdTime;
        this.author = author;
        setId(id);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message getNext() {
        return next;
    }

    public void setNext(Message next) {
        this.next = next;
    }

    public Message getPrevious() {
        return previous;
    }

    public void setPrevious(Message previous) {
        this.previous = previous;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Node getAuthor() {
        return author;
    }

    public void setAuthor(Node author) {
        this.author = author;
    }

}
