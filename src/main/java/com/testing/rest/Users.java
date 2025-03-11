package com.testing.rest;

import org.eclipse.jnosql.databases.mongodb.mapping.ObjectIdConverter;

import jakarta.nosql.Column;
import jakarta.nosql.Convert;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

@Entity
public class Users {

    @Id
    @Convert(ObjectIdConverter.class)
    private String id;
    
    @Column
    private String name;
    
    @Column
    private String email;
    
    @Column
    private String provider;
    
    @Column
    private String role;
    
    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
