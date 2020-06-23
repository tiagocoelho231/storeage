package com.tiagocoelho.storeage.view;

import com.tiagocoelho.storeage.model.Customer;
import com.tiagocoelho.storeage.model.Product;
import com.tiagocoelho.storeage.model.Sale;
import com.tiagocoelho.storeage.model.SaleItem;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
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
import java.util.Date;
import java.util.List;
import javax.enterprise.context.spi.AlterableContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Named
@Stateful
@SessionScoped
public class SaleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private BeanManager beanManager;

    private Long id;
    private Sale sale = new Sale();
    private String saleItemBarCode;
    private Integer saleItemQuantity;
    @PersistenceContext(unitName = "storeage", type = PersistenceContextType.EXTENDED)
    private EntityManager em;
    @Resource
    private SessionContext sessionContext;
    private Sale add = new Sale();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public String getSaleItemBarCode() {
        return saleItemBarCode;
    }

    public void setSaleItemBarCode(String saleItemName) {
        this.saleItemBarCode = saleItemName;
    }

    public Integer getSaleItemQuantity() {
        return saleItemQuantity;
    }

    public void setSaleItemQuantity(Integer saleItemQuantity) {
        this.saleItemQuantity = saleItemQuantity;
    }

    public String setCustomer(Customer customer) {
        sale.setCustomer(customer);
        return "/vendor/sale?faces-redirect=true";
    }

    public void addItem() {
        try {
            TypedQuery<Product> query = em.createNamedQuery(Product.FIND_BY_BARCODE, Product.class);
            query.setParameter("barCode", getSaleItemBarCode());
            Product product = query.getSingleResult();
            SaleItem saleItem = new SaleItem(product, getSaleItemQuantity());
            sale.addItem(saleItem);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }
    }

    public String cancel() {
        AlterableContext ctx = (AlterableContext) beanManager.getContext(SessionScoped.class);
        Bean<?> myBean = beanManager.getBeans(SaleBean.class).iterator().next();
        ctx.destroy(myBean);
        return "menu?faces-redirect=true";
    }

    @Transactional
    public String completeSale() {
        if (sale.getSaleItems().isEmpty()) {
            return null;
        }
        sale.setCreatedAt(new Date());
        em.persist(sale);

        AlterableContext ctx = (AlterableContext) beanManager.getContext(SessionScoped.class);
        Bean<?> myBean = beanManager.getBeans(SaleBean.class).iterator().next();
        ctx.destroy(myBean);
        return "menu?faces-redirect=true";
    }

    public Sale findById(Long id) {
        return em.find(Sale.class, id);
    }

    public List<Sale> getAll() {

        CriteriaQuery<Sale> criteria = em.getCriteriaBuilder().createQuery(Sale.class);
        return em.createQuery(criteria.select(criteria.from(Sale.class))).getResultList();
    }

    public Converter getConverter() {

        final SaleBean ejbProxy = sessionContext.getBusinessObject(SaleBean.class);

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

                return String.valueOf(((Sale) value).getId());
            }
        };
    }

    public Sale getAdd() {
        return add;
    }

    public Sale getAdded() {
        Sale added = add;
        add = new Sale();
        return added;
    }
}
