package com.tiagocoelho.storeage.view;

import com.tiagocoelho.storeage.model.Customer;
import com.tiagocoelho.storeage.model.Sale;

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
public class CustomerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
     * Support creating and retrieving Customer entities
     */
    private Long id;
    private Customer customer;
    @Inject
    private Conversation conversation;

    @PersistenceContext(unitName = "storeage", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private long count;
    private List<Customer> pageItems;
    private Customer example = new Customer();
    @Resource
    private SessionContext sessionContext;
    private Customer add = new Customer();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
            this.customer = this.example;
        } else {
            this.customer = findById(getId());
        }
    }

    public Customer findById(Long id) {
        return this.entityManager.find(Customer.class, id);
    }

    public String update() {
        try {
            if (this.id != null) {
                this.entityManager.persist(this.customer);
            } else {
                this.entityManager.merge(this.customer);
            }
            this.conversation.end();
            return "index?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        this.conversation.end();

        try {
            Customer deletableEntity = findById(getId());

            this.entityManager.remove(deletableEntity);
            this.entityManager.flush();
            return "index?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public void remove(Customer customer) {
        try {
            Customer deletableEntity = findById(customer.getId());
            this.entityManager.remove(deletableEntity);
            this.entityManager.flush();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }
    }

    public List<Sale> getRelatedSales() {
        TypedQuery<Sale> query = entityManager.createNamedQuery(Customer.GET_RELATED_SALES_BY_ID, Sale.class);
        query.setParameter("customerId", id);
        List<Sale> sales = query.getResultList();
        return sales;
    }

    public List<Customer> getPageItems() {
        return this.pageItems;
    }

    public long getCount() {
        return this.count;
    }

    public List<Customer> getAll() {

        CriteriaQuery<Customer> criteria = this.entityManager.getCriteriaBuilder().createQuery(Customer.class);
        return this.entityManager.createQuery(criteria.select(criteria.from(Customer.class))).getResultList();
    }

    public Converter getConverter() {

        final CustomerBean ejbProxy = this.sessionContext.getBusinessObject(CustomerBean.class);

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

                return String.valueOf(((Customer) value).getId());
            }
        };
    }

    public Customer getAdd() {
        return this.add;
    }

    public Customer getAdded() {
        Customer added = this.add;
        this.add = new Customer();
        return added;
    }
}
