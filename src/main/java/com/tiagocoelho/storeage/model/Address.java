package com.tiagocoelho.storeage.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

/**
 * @author Tiago Rodrigues Coelho
 */
@Entity
@DiscriminatorValue("A")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Address {

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
    private String street;

    @NotNull
    private Integer number;

    @NotNull
    @Column(length = 100)
    private String neighborhood;

    @NotNull
    @Column(length = 100)
    private String city;

    @NotNull
    @Column(length = 100, name = "t_state")
    private String state;

    // ======================================
    // = Constructors =
    // ======================================
    public Address() {
    }

    public Address(String street, Integer number, String neighborhood, String city, String state) {
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
