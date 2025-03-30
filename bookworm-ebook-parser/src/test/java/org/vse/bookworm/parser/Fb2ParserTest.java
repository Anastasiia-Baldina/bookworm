package org.vse.bookworm.parser;

import org.jaxb.fb2.FictionBook;

import java.io.*;

public class Fb2ParserTest {
    private final JaxbFb2Parser parser = new JaxbFb2Parser();

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(System.getProperty("user.dir"));
         File initialFile = new File("bookworm-ebook-parser/src/test/resources/valid_fb2.xml");
         InputStream targetStream =
                new DataInputStream(new FileInputStream(initialFile));
         FictionBook book = JaxbFb2Parser.getInstance().parse(targetStream);
    }
}
