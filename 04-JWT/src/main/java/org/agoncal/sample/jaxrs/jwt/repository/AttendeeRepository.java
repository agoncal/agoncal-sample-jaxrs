package org.agoncal.sample.jaxrs.jwt.repository;

import org.agoncal.sample.jaxrs.jwt.domain.Attendee;
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
public class AttendeeRepository {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @PersistenceContext
    private EntityManager em;

    // ======================================
    // =          Business methods          =
    // ======================================

    public Attendee create(Attendee attendee) {
        em.persist(attendee);
        return attendee;
    }

    public List<Attendee> findAllAttendees() {
        TypedQuery<Attendee> query = em.createNamedQuery(Attendee.FIND_ALL, Attendee.class);
        return query.getResultList();
    }

    public Long countNumberOfAttendees() {
        TypedQuery<Long> query = em.createNamedQuery(Attendee.COUNT_ALL, Long.class);
        Long countResult = query.getSingleResult();
        return countResult;
    }

    public Attendee findById(String id) {
        return em.find(Attendee.class, id);
    }

    public Attendee findByLoginPassword(String login, String password) {
        TypedQuery<Attendee> query = em.createNamedQuery(Attendee.FIND_BY_LOGIN_PASSWORD, Attendee.class);
        query.setParameter("login", login);
        query.setParameter("password", PasswordUtils.digestPassword(password));
        return query.getSingleResult();
    }

    public void delete(String id) {
        em.remove(em.getReference(Attendee.class, id));
    }
}
