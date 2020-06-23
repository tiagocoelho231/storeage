package com.tiagocoelho.storeage.view;

import com.tiagocoelho.storeage.model.Product;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import javax.persistence.TypedQuery;

@Named
@Stateful
@ConversationScoped
public class ProductBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
	 * Support creating and retrieving Product entities
     */
    private Long id;
    private Product product;

    private String barCode;
    private Integer quantity;

    @Inject
    private Conversation conversation;
    @PersistenceContext(unitName = "storeage", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private long count;
    private List<Product> pageItems;
    private Product example = new Product();
    @Resource
    private SessionContext sessionContext;
    private Product add = new Product();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getBarCode() {
        return this.barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void increase() {
        TypedQuery<Product> query = entityManager.createNamedQuery(Product.FIND_BY_BARCODE, Product.class);
        query.setParameter("barCode", barCode);
        Product product = query.getSingleResult();
        product.setQuantity(product.getQuantity() + quantity);
        resetForm();
    }

    public void decrease() {
        TypedQuery<Product> query = entityManager.createNamedQuery(Product.FIND_BY_BARCODE, Product.class);
        query.setParameter("barCode", barCode);
        Product product = query.getSingleResult();
        product.setQuantity(product.getQuantity() - quantity);
        resetForm();
    }

    public void resetForm() {
        this.barCode = null;
        this.quantity = null;
    }

    public String create() {
        this.conversation.begin();
        this.conversation.setTimeout(1800000L);
        return "create?faces-redirect=true";
    }

    public void retrieve() {
        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        if (this.conversation.isTransient()) {
            this.conversation.begin();
            this.conversation.setTimeout(1800000L);
        }

        if (this.id == null) {
            this.product = this.example;
        } else {
            this.product = findById(getId());
        }
    }

    public Product findById(Long id) {

        return this.entityManager.find(Product.class, id);
    }

    public String update() {
        this.conversation.end();

        try {
            if (this.id == null) {
                this.entityManager.persist(this.product);
            } else {
                this.entityManager.merge(this.product);
            }
            return "index?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        this.conversation.end();

        try {
            Product deletableEntity = findById(getId());

            this.entityManager.remove(deletableEntity);
            this.entityManager.flush();
            return "index?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public void remove(Product product) {
        try {
            Product deletableEntity = findById(product.getId());
            this.entityManager.remove(deletableEntity);
            this.entityManager.flush();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }
    }

    public List<Product> getPageItems() {
        return this.pageItems;
    }

    public long getCount() {
        return this.count;
    }

    public List<Product> getAll() {

        CriteriaQuery<Product> criteria = this.entityManager.getCriteriaBuilder().createQuery(Product.class);
        return this.entityManager.createQuery(criteria.select(criteria.from(Product.class))).getResultList();
    }

    public Converter getConverter() {

        final ProductBean ejbProxy = this.sessionContext.getBusinessObject(ProductBean.class);

        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {

                return ejbProxy.findById(Long.valueOf(value));
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {

                if (value == null) {
                    return "";
                }

                return String.valueOf(((Product) value).getId());
            }
        };
    }

    public Product getAdd() {
        return this.add;
    }

    public Product getAdded() {
        Product added = this.add;
        this.add = new Product();
        return added;
    }
}
