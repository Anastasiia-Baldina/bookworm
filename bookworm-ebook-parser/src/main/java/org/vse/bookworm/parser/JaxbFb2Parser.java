package org.vse.bookworm.parser;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.jaxb.fb2.FictionBook;
import org.vse.bookworm.parser.exception.IllegalFb2FormatException;

import java.io.InputStream;
import java.io.StringReader;

public class JaxbFb2Parser {
    private static final Unmarshaller UNMARSHALLER;
    private static final JaxbFb2Parser INSTANCE = new JaxbFb2Parser();

    static {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance("org.jaxb.fb2");
        } catch (JAXBException e) {
            throw new IllegalStateException("JAXB context not initialized.", e);
        }
        try {
            UNMARSHALLER = context.createUnmarshaller();
        } catch (JAXBException e) {
            throw new IllegalStateException("JAXB unmarshaller not initialized.", e);
        }
    }

    public FictionBook parse(String entry) {
        try {
            Object res = UNMARSHALLER.unmarshal(new StringReader(entry));
            if(res instanceof FictionBook fictionBook) {
                return fictionBook;
            }
            throw new IllegalFb2FormatException();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public FictionBook parse(InputStream is) {
        try {
            Object res =  UNMARSHALLER.unmarshal(is);
            if(res instanceof FictionBook fictionBook) {
                return fictionBook;
            }
            throw new IllegalFb2FormatException();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static JaxbFb2Parser getInstance() {
        return INSTANCE;
    }
}
