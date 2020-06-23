package com.tiagocoelho.storeage.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

/**
 * @author Tiago Rodrigues Coelho
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Sale {

    // ======================================
    // = Attributes =
    // ======================================
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @XmlTransient
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;

    @NotNull
    @Column(name = "created_at")
    private Date createdAt;

    @NotEmpty
    @OneToMany(cascade = CascadeType.ALL)
    private List<SaleItem> saleItems = new ArrayList<>();

    // ======================================
    // = Getters and Setters =
    // ======================================
    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void addItem(SaleItem item) {
        saleItems.add(item);
    }

    public SaleItem removeItem(SaleItem item) {
        saleItems.remove(item);
        return item;
    }

    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    public Float getTotal() {
        if (getSaleItems() == null || getSaleItems().isEmpty()) {
            return 0f;
        }

        Float total = 0f;

        for (SaleItem item : getSaleItems()) {
            total += (item.getSubTotal());
        }
        return total;
    }
}
