//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.2 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.04.10 at 10:52:05 PM MSK 
//


package org.jaxb.fb2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;


/**
 * Book (as a book opposite a document) description
 * 
 * <p>Java class for title-infoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="title-infoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="genre" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;simpleContent&gt;
 *               &lt;extension base="&lt;http://www.gribuser.ru/xml/fictionbook/2.0/genres&gt;genreType"&gt;
 *                 &lt;attribute name="match" type="{http://www.w3.org/2001/XMLSchema}integer" default="100" /&gt;
 *               &lt;/extension&gt;
 *             &lt;/simpleContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="author" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base="{http://www.gribuser.ru/xml/fictionbook/2.0}authorType"&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="book-title" type="{http://www.gribuser.ru/xml/fictionbook/2.0}textFieldType"/&gt;
 *         &lt;element name="annotation" type="{http://www.gribuser.ru/xml/fictionbook/2.0}annotationType" minOccurs="0"/&gt;
 *         &lt;element name="keywords" type="{http://www.gribuser.ru/xml/fictionbook/2.0}textFieldType" minOccurs="0"/&gt;
 *         &lt;element name="date" type="{http://www.gribuser.ru/xml/fictionbook/2.0}dateType" minOccurs="0"/&gt;
 *         &lt;element name="coverpage" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="image" type="{http://www.gribuser.ru/xml/fictionbook/2.0}inlineImageType" maxOccurs="unbounded"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="lang" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="src-lang" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="translator" type="{http://www.gribuser.ru/xml/fictionbook/2.0}authorType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="sequence" type="{http://www.gribuser.ru/xml/fictionbook/2.0}sequenceType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "title-infoType", propOrder = {
    "genre",
    "author",
    "bookTitle",
    "annotation",
    "keywords",
    "date",
    "coverpage",
    "lang",
    "srcLang",
    "translator",
    "sequence"
})
public class TitleInfoType {

    @XmlElement(required = true)
    protected List<TitleInfoType.Genre> genre;
    @XmlElement(required = true)
    protected List<TitleInfoType.Author> author;
    @XmlElement(name = "book-title", required = true)
    protected TextFieldType bookTitle;
    protected AnnotationType annotation;
    protected TextFieldType keywords;
    protected DateType date;
    protected TitleInfoType.Coverpage coverpage;
    @XmlElement(required = true)
    protected String lang;
    @XmlElement(name = "src-lang")
    protected String srcLang;
    protected List<AuthorType> translator;
    protected List<SequenceType> sequence;

    /**
     * Gets the value of the genre property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the genre property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGenre().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TitleInfoType.Genre }
     * 
     * 
     */
    public List<TitleInfoType.Genre> getGenre() {
        if (genre == null) {
            genre = new ArrayList<TitleInfoType.Genre>();
        }
        return this.genre;
    }

    /**
     * Gets the value of the author property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the author property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TitleInfoType.Author }
     * 
     * 
     */
    public List<TitleInfoType.Author> getAuthor() {
        if (author == null) {
            author = new ArrayList<TitleInfoType.Author>();
        }
        return this.author;
    }

    /**
     * Gets the value of the bookTitle property.
     * 
     * @return
     *     possible object is
     *     {@link TextFieldType }
     *     
     */
    public TextFieldType getBookTitle() {
        return bookTitle;
    }

    /**
     * Sets the value of the bookTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextFieldType }
     *     
     */
    public void setBookTitle(TextFieldType value) {
        this.bookTitle = value;
    }

    /**
     * Gets the value of the annotation property.
     * 
     * @return
     *     possible object is
     *     {@link AnnotationType }
     *     
     */
    public AnnotationType getAnnotation() {
        return annotation;
    }

    /**
     * Sets the value of the annotation property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnnotationType }
     *     
     */
    public void setAnnotation(AnnotationType value) {
        this.annotation = value;
    }

    /**
     * Gets the value of the keywords property.
     * 
     * @return
     *     possible object is
     *     {@link TextFieldType }
     *     
     */
    public TextFieldType getKeywords() {
        return keywords;
    }

    /**
     * Sets the value of the keywords property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextFieldType }
     *     
     */
    public void setKeywords(TextFieldType value) {
        this.keywords = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link DateType }
     *     
     */
    public DateType getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateType }
     *     
     */
    public void setDate(DateType value) {
        this.date = value;
    }

    /**
     * Gets the value of the coverpage property.
     * 
     * @return
     *     possible object is
     *     {@link TitleInfoType.Coverpage }
     *     
     */
    public TitleInfoType.Coverpage getCoverpage() {
        return coverpage;
    }

    /**
     * Sets the value of the coverpage property.
     * 
     * @param value
     *     allowed object is
     *     {@link TitleInfoType.Coverpage }
     *     
     */
    public void setCoverpage(TitleInfoType.Coverpage value) {
        this.coverpage = value;
    }

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

    /**
     * Gets the value of the srcLang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrcLang() {
        return srcLang;
    }

    /**
     * Sets the value of the srcLang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrcLang(String value) {
        this.srcLang = value;
    }

    /**
     * Gets the value of the translator property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the translator property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTranslator().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AuthorType }
     * 
     * 
     */
    public List<AuthorType> getTranslator() {
        if (translator == null) {
            translator = new ArrayList<AuthorType>();
        }
        return this.translator;
    }

    /**
     * Gets the value of the sequence property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the sequence property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSequence().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SequenceType }
     * 
     * 
     */
    public List<SequenceType> getSequence() {
        if (sequence == null) {
            sequence = new ArrayList<SequenceType>();
        }
        return this.sequence;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base="{http://www.gribuser.ru/xml/fictionbook/2.0}authorType"&gt;
     *     &lt;/extension&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Author
        extends AuthorType
    {


    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="image" type="{http://www.gribuser.ru/xml/fictionbook/2.0}inlineImageType" maxOccurs="unbounded"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "image"
    })
    public static class Coverpage {

        @XmlElement(required = true)
        protected List<InlineImageType> image;

        /**
         * Gets the value of the image property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the image property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getImage().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link InlineImageType }
         * 
         * 
         */
        public List<InlineImageType> getImage() {
            if (image == null) {
                image = new ArrayList<InlineImageType>();
            }
            return this.image;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;simpleContent&gt;
     *     &lt;extension base="&lt;http://www.gribuser.ru/xml/fictionbook/2.0/genres&gt;genreType"&gt;
     *       &lt;attribute name="match" type="{http://www.w3.org/2001/XMLSchema}integer" default="100" /&gt;
     *     &lt;/extension&gt;
     *   &lt;/simpleContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Genre {

        @XmlValue
        protected GenreType value;
        @XmlAttribute(name = "match")
        protected BigInteger match;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link GenreType }
         *     
         */
        public GenreType getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link GenreType }
         *     
         */
        public void setValue(GenreType value) {
            this.value = value;
        }

        /**
         * Gets the value of the match property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getMatch() {
            if (match == null) {
                return new BigInteger("100");
            } else {
                return match;
            }
        }

        /**
         * Sets the value of the match property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setMatch(BigInteger value) {
            this.match = value;
        }

    }

}
