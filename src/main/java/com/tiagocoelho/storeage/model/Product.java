package com.tiagocoelho.storeage.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.*;

/**
 * @author Tiago Rodrigues Coelho
 */
@Entity
@DiscriminatorValue("P")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = Product.FIND_BY_BARCODE, query = "SELECT p FROM Product p WHERE p.barCode = :barCode")
public class Product {

    public static final String FIND_BY_BARCODE = "Product.findByBarCode";

    // ======================================
    // = Attributes =
    // ======================================
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @XmlTransient
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 20, name = "barcode")
    private String barCode;

    @Min(0)
    private Float price;

    @Min(0)
    private Integer quantity;

    // ======================================
    // = Constructors =
    // ======================================
    public Product() {
    }

    public Product(String name, String barCode, Float price, Integer quantity) {
        this.name = name;
        this.barCode = barCode;
        this.price = price;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarCode() {
        return this.barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // ======================================
    // = Methods hash, equals, toString =
    // ======================================
    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (id != null) {
            result += "id: " + id;
        }
        if (name != null) {
            result += "name: " + name;
        }
        if (barCode != null) {
            result += ", barCode: " + barCode;
        }
        if (price != null) {
            result += ", price: " + price;
        }
        if (quantity != null) {
            result += ", quantity: " + quantity;
        }
        return result;
    }
}
