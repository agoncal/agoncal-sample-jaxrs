package org.agoncal.sample.jaxrs.jwt.repository;

import org.agoncal.sample.jaxrs.jwt.domain.Attendee;

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

    public Integer countNumberOfAttendees() {
        TypedQuery<Integer> query = em.createNamedQuery(Attendee.COUNT_ALL, Integer.class);
        Integer countResult = query.getSingleResult();
        return countResult;
    }

    public Attendee findById(String id) {
        return em.find(Attendee.class, id);
    }

    public void delete(String id) {
        em.remove(em.getReference(Attendee.class, id));
    }
}
