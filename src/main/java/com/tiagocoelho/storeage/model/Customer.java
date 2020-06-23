package com.tiagocoelho.storeage.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

/**
 * @author Tiago Rodrigues Coelho
 */
@Entity
@DiscriminatorValue("C")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = Customer.GET_RELATED_SALES_BY_ID, query = "SELECT s FROM Sale s WHERE s.customer.id = :customerId")
public class Customer {

    public static final String GET_RELATED_SALES_BY_ID = "Customer.getRelatedSalesById";

    // ======================================
    // = Attributes =
    // ======================================
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @XmlTransient
    private Long id;

    @NotNull
    @Column(length = 100)
    private String name;

    @NotNull
    @Column(length = 11)
    private String cpf;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Address address = new Address();

    @NotNull
    @Column(length = 20)
    private String phone;

    // ======================================
    // = Constructors =
    // ======================================
    public Customer() {
    }

    public Customer(String name, String cpf, Address address, String phone) {
        this.name = name;
        this.cpf = cpf;
        this.address = address;
        this.phone = phone;
    }

    // ======================================
    // = Getters and Setters =
    // ======================================
    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
