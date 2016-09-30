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
@NamedQueries({
        @NamedQuery(name = Attendee.FIND_ALL, query = "SELECT a FROM Attendee a ORDER BY a.lastName DESC"),
        @NamedQuery(name = Attendee.COUNT_ALL, query = "SELECT COUNT(a) FROM Attendee a")
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Attendee {

    // ======================================
    // =             Constants              =
    // ======================================

    public static final String FIND_ALL = "Attendee.findAll";
    public static final String COUNT_ALL = "Attendee.countAll";

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

    public Attendee() {
    }

    public Attendee(String id, String lastName) {
        this.id = id;
        this.lastName = lastName;
    }

    public Attendee(String id, String lastName, String firstName, String login, String password) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.login = login;
        this.password = password;
    }

    public Attendee(String id, String lastName, String firstName, String twitter, String avatarUrl, String company) {
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
        Attendee attendee = (Attendee) o;
        return Objects.equals(id, attendee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Attendee{" +
                "id='" + id + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", twitter='" + twitter + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
