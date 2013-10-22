package org.agoncal.sample.jaxrs.qbe.rest;

import org.agoncal.sample.jaxrs.qbe.model.Book;
import org.agoncal.sample.jaxrs.qbe.model.Books;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Transactional
@Path("/books")
public class BookEndpoint {

    // ======================================
    // =             Attributes             =
    // ======================================

    @PersistenceContext(unitName = "sampleJaxRsQBEPU")
    private EntityManager em;

    // ======================================
    // =          Business Methods          =
    // ======================================

    @POST
    @Consumes("application/xml")
    public Response create(Book entity) {
        em.persist(entity);
        return Response.created(UriBuilder.fromResource(BookEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") Long id) {
        Book deletableEntity = em.find(Book.class, id);
        if (deletableEntity == null) {
            return Response.status(Status.NO_CONTENT).build();
        }
        em.remove(deletableEntity);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/xml")
    public Response findById(@PathParam("id") Long id) {
        TypedQuery<Book> query = em.createQuery("SELECT DISTINCT b FROM Book b WHERE b.id = :entityId ORDER BY b.id", Book.class);
        query.setParameter("entityId", id);
        Book entity = query.getSingleResult();
        if (entity == null) {
            return Response.status(Status.NO_CONTENT).build();
        }
        return Response.ok(entity).build();
    }

    @GET
    @Produces("application/xml")
    public Response findAll() {
        TypedQuery<Book> query = em.createQuery("SELECT DISTINCT b FROM Book b ORDER BY b.id", Book.class);
        List<Book> entities = query.getResultList();
        if (entities == null) {
            return Response.status(Status.NO_CONTENT).build();
        }
        Books books = new Books(entities);
        return Response.ok(books).build();

    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/xml")
    public Response update(Book entity) {
        em.merge(entity);
        return Response.noContent().build();
    }

    @GET
    @Path("/query")
    @Produces("application/xml")
    public Response findByQBE(@QueryParam("example") Book example) {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        TypedQuery<Book> query = em.createQuery(criteria.select(root).where(getSearchPredicates(root, example)));
        List<Book> entities = query.getResultList();
        if (entities == null) {
            return Response.status(Status.NO_CONTENT).build();
        }
        Books books = new Books(entities);
        return Response.ok(books).build();
    }

    private Predicate[] getSearchPredicates(Root<Book> root, Book example) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        String isbn = example.getIsbn();
        if (isbn != null && !"".equals(isbn)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("isbn")), '%' + isbn.toLowerCase() + '%'));
        }
        String title = example.getTitle();
        if (title != null && !"".equals(title)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("title")), '%' + title.toLowerCase() + '%'));
        }
        String description = example.getDescription();
        if (description != null && !"".equals(description)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("description")), '%' + description.toLowerCase() + '%'));
        }
        String publisher = example.getPublisher();
        if (publisher != null && !"".equals(publisher)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("publisher")), '%' + publisher.toLowerCase() + '%'));
        }
        Integer nbOfPages = example.getNbOfPages();
        if (nbOfPages != null && nbOfPages != 0) {
            predicatesList.add(builder.equal(root.get("nbOfPages"), nbOfPages));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

}