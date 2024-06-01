package com.jewelrymanagement.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    public Role() {

    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role(Integer role_id, String name) {
        this.role_id = role_id;
        this.name = name;
    }
    public Role(String name) {
        this.name = name;
    }
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer role_id;

    private String name;


}
