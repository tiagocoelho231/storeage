package com.tiagocoelho.storeage.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.*;

/**
 * @author Tiago Rodrigues Coelho
 */
@Entity
@DiscriminatorValue("I")
public class SaleItem {

    // ======================================
    // = Attributes =
    // ======================================
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @XmlTransient
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Product product;

    @Min(0)
    private Integer quantity;

    // ======================================
    // = Constructors =
    // ======================================
    public SaleItem() {
    }

    public SaleItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getSubTotal() {
        return getProduct().getPrice() * getQuantity();
    }
}
