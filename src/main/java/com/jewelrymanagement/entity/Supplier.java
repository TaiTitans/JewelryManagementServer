package com.jewelrymanagement.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "suppliers")
@Data
public class Supplier {
    public Supplier(Integer supplier_id, String supplier_name, String address, Integer phone, String email) {
        this.supplier_id = supplier_id;
        this.supplier_name = supplier_name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Integer supplier_id;

    @Column(name = "supplier_name")
    private String supplier_name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private Integer phone;

    @Column(name = "email")
    private String email;


    public Supplier() {

    }

}
