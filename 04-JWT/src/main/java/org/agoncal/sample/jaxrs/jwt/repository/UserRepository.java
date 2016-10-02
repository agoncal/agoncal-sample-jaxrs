package org.agoncal.sample.jaxrs.jwt.repository;

import org.agoncal.sample.jaxrs.jwt.domain.User;
import org.agoncal.sample.jaxrs.jwt.util.PasswordUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Transactional
public class UserRepository {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @PersistenceContext
    private EntityManager em;

    // ======================================
    // =          Business methods          =
    // ======================================

    public User create(User user) {
        em.persist(user);
        return user;
    }

    public List<User> findAllUsers() {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_ALL, User.class);
        return query.getResultList();
    }

    public Long countNumberOfUsers() {
        TypedQuery<Long> query = em.createNamedQuery(User.COUNT_ALL, Long.class);
        Long countResult = query.getSingleResult();
        return countResult;
    }

    public User findById(String id) {
        return em.find(User.class, id);
    }

    public User findByLoginPassword(String login, String password) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_LOGIN_PASSWORD, User.class);
        query.setParameter("login", login);
        query.setParameter("password", PasswordUtils.digestPassword(password));
        return query.getSingleResult();
    }

    public void delete(String id) {
        em.remove(em.getReference(User.class, id));
    }
}
