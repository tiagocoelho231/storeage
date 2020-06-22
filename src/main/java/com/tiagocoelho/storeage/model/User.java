package com.tiagocoelho.storeage.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author Tiago Rodrigues Coelho
 */
@Entity
@Table(name = "T_USER")
@NamedQueries({
    @NamedQuery(name = User.FIND_BY_LOGIN, query = "SELECT u FROM User u WHERE u.login = :login"),
    @NamedQuery(name = User.FIND_BY_TOKEN, query = "SELECT u FROM User u WHERE u.token = :token"),
    @NamedQuery(name = User.FIND_BY_LOGIN_PASSWORD, query = "SELECT u FROM User u WHERE u.login = :login AND u.password = :password"),
    @NamedQuery(name = User.FIND_ALL, query = "SELECT u FROM User u")})
public class User {

    // ======================================
    // = Constants =
    // ======================================
    public static final String FIND_BY_LOGIN = "User.findByLogin";
    public static final String FIND_BY_TOKEN = "User.findByUUID";
    public static final String FIND_BY_LOGIN_PASSWORD = "User.findByLoginAndPassword";
    public static final String FIND_ALL = "User.findAll";

    // ======================================
    // = Attributes =
    // ======================================
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "login", length = 11, nullable = false)
    @NotNull
    @Size(min = 11, max = 11)
    private String login;

    @Column(length = 256, nullable = false)
    @NotNull
    @Size(min = 1, max = 256)
    private String password;

    @Column(length = 100, nullable = false)
    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @Column(length = 256)
    @Size(min = 1, max = 256)
    private String token;

    @Column(name = "IS_ADMIN")
    private Boolean admin;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    @Past
    private Date createdAt;

    // ======================================
    // = Getters and Setters =
    // ======================================
    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean isAdmin() {
        return admin;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
