package br.com.tbp.model;

public class User extends CoreEntity {    
    
    private String name;  
    
    public User(String id, String name) {        
        setId(id);
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
   
}
