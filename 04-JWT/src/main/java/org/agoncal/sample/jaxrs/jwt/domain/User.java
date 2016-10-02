package org.agoncal.sample.jaxrs.jwt.domain;

import org.agoncal.sample.jaxrs.jwt.util.PasswordUtils;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Entity
@Table(name = "t_user")
@NamedQueries({
        @NamedQuery(name = User.FIND_ALL, query = "SELECT u FROM User u ORDER BY u.lastName DESC"),
        @NamedQuery(name = User.FIND_BY_LOGIN_PASSWORD, query = "SELECT u FROM User u WHERE u.login = :login AND u.password = :password"),
        @NamedQuery(name = User.COUNT_ALL, query = "SELECT COUNT(u) FROM User u")
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    // ======================================
    // =             Constants              =
    // ======================================

    public static final String FIND_ALL = "User.findAll";
    public static final String COUNT_ALL = "User.countAll";
    public static final String FIND_BY_LOGIN_PASSWORD = "User.findByLoginAndPassword";

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    private String id;
    private String lastName;
    private String firstName;
    @Column(length = 10, nullable = false)
    private String login;
    @Column(length = 256, nullable = false)
    private String password;
    private String twitter;
    private String avatarUrl;
    private String company;

    // ======================================
    // =            Constructors            =
    // ======================================

    public User() {
    }

    public User(String id, String lastName) {
        this.id = id;
        this.lastName = lastName;
    }

    public User(String id, String lastName, String firstName, String login, String password) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.login = login;
        this.password = password;
    }

    public User(String id, String lastName, String firstName, String twitter, String avatarUrl, String company) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.twitter = twitter;
        this.avatarUrl = avatarUrl;
        this.company = company;
    }

    // ======================================
    // =         Lifecycle methods          =
    // ======================================

    @PrePersist
    private void setUUID() {
        id = UUID.randomUUID().toString().replace("-", "");
        password = PasswordUtils.digestPassword(password);
    }

    // ======================================
    // =          Getters & Setters         =
    // ======================================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    // ======================================
    // =   Methods hash, equals, toString   =
    // ======================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", twitter='" + twitter + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
