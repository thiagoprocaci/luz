package br.com.tbp.model;

public class User extends CoreEntity {    
    
    private String name;  
    
    public User(String id, String name) {        
        if(id == null) {
            throw new IllegalArgumentException("The user id can not be null");
        }
        setId(id.trim());
        this.name = name.trim();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
