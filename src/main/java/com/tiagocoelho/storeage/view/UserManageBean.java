package com.tiagocoelho.storeage.view;

import com.tiagocoelho.storeage.model.User;

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
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

@Named
@Stateful
@ConversationScoped
public class UserManageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private User user;
    private String password1;
    private String password2;

    @Inject
    private Conversation conversation;

    @PersistenceContext(unitName = "storeage", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private long count;
    private List<User> pageItems;
    private User example = new User();
    @Resource
    private SessionContext sessionContext;
    private User add = new User();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    private void resetPasswords() {
        password1 = null;
        password2 = null;
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
            this.user = this.example;
        } else {
            this.user = findById(getId());
        }
    }

    public User findById(Long id) {
        return this.entityManager.find(User.class, id);
    }

    @Transactional
    public String update() {
        try {
            if (entityManager.createNamedQuery(User.FIND_BY_LOGIN, User.class).setParameter("login", user.getLogin())
                    .getResultList().size() > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "CPF " + user.getLogin() + " já cadastrado", null));
                return null;
            }

            if (password1 != null && !password1.isEmpty() && password1.equals(password2)) {
                user.setPassword(password1);
            }

            user.setCreatedAt(new Date());

            if (this.id != null) {
                this.entityManager.persist(this.user);
            } else {
                this.entityManager.merge(this.user);
            }
            resetPasswords();
            this.conversation.end();
            return "index?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        try {
            User deletableEntity = findById(getId());
            this.entityManager.remove(deletableEntity);
            this.entityManager.flush();
            this.conversation.end();
            return "index?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public void remove(User user) {
        try {
            User deletableEntity = findById(user.getId());
            this.entityManager.remove(deletableEntity);
            this.entityManager.flush();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }
    }

    public List<User> getPageItems() {
        return this.pageItems;
    }

    public long getCount() {
        return this.count;
    }

    public List<User> getAll() {

        CriteriaQuery<User> criteria = this.entityManager.getCriteriaBuilder().createQuery(User.class);
        return this.entityManager.createQuery(criteria.select(criteria.from(User.class))).getResultList();
    }

    public Converter getConverter() {

        final UserManageBean ejbProxy = this.sessionContext.getBusinessObject(UserManageBean.class);

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

                return String.valueOf(((User) value).getId());
            }
        };
    }

    public User getAdd() {
        return this.add;
    }

    public User getAdded() {
        User added = this.add;
        this.add = new User();
        return added;
    }
}
