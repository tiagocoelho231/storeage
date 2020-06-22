package com.tiagocoelho.storeage.view;

import com.tiagocoelho.storeage.model.User;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.AlterableContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.io.Serializable;

@Named
@SessionScoped
@Transactional
public class UserBean implements Serializable {

    // ======================================
    // = Injection Points =
    // ======================================
    @Inject
    private BeanManager beanManager;

    @Inject
    private FacesContext facesContext;

    @Inject
    private EntityManager em;

    // ======================================
    // = Attributes =
    // ======================================
    // Logged user
    private User user = new User();
    private boolean loggedIn;
    private boolean admin;

    // ======================================
    // = Business methods =
    // ======================================
    public String doSignin() {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_LOGIN_PASSWORD, User.class);
        query.setParameter("login", user.getLogin());
        query.setParameter("password", user.getPassword());
        try {
            user = query.getSingleResult();

            if (user.isAdmin()) {
                admin = true;
            }

            loggedIn = true;

            if (admin) {
                return "admin/menu?faces-redirect=true";
            } else {
                return "vendor/menu?faces-redirect=true";
            }
        } catch (NoResultException e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "CPF/Senha incorreto", null));
            return null;
        }
    }

    public String doLogout() {
        AlterableContext ctx = (AlterableContext) beanManager.getContext(SessionScoped.class);
        Bean<?> myBean = beanManager.getBeans(UserBean.class).iterator().next();
        ctx.destroy(myBean);
        return "/signin";
    }

    // ======================================
    // = Getters and Setters =
    // ======================================
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isAdmin() {
        return admin;
    }
}
