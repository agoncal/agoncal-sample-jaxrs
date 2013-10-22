package org.agoncal.sample.jaxrs.qbe.provider.rest;

import org.agoncal.sample.jaxrs.qbe.provider.model.Book;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class BookParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType != Book.class) {
            return null;
        }

        return (ParamConverter<T>) new ParamConverter<Book>() {

            @Override
            public Book fromString(String value) throws IllegalArgumentException {
                try {
                    JAXBContext ctx = JAXBContext.newInstance(Book.class);
                    Unmarshaller m = ctx.createUnmarshaller();
                    Book book = (Book) m.unmarshal(new StringReader(value));
                    return book;
                } catch (JAXBException e) {
                    return null;
                }
            }

            @Override
            public String toString(Book bean) throws IllegalArgumentException {
                try {
                    StringWriter writer = new StringWriter();
                    JAXBContext ctx = JAXBContext.newInstance(Book.class);
                    Marshaller m = ctx.createMarshaller();
                    m.marshal(this, writer);
                    return writer.toString();
                } catch (JAXBException e) {
                    return null;
                }
            }

        };
    }
}