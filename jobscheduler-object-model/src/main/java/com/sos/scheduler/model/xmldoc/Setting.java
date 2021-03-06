//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, vJAXB 2.1.3 in JDK 1.6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2011.03.08 at 02:48:08 PM MEZ
//

package com.sos.scheduler.model.xmldoc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/** <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element ref="{http://www.sos-berlin.com/schema/scheduler_job_documentation_v1.1}note" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="default_value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="required" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="reference" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="type">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="integer"/>
 *             &lt;enumeration value="double"/>
 *             &lt;enumeration value="float"/>
 *             &lt;enumeration value="string"/>
 *             &lt;enumeration value="boolean"/>
 *             &lt;enumeration value="clob"/>
 *             &lt;enumeration value="blob"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}token" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre> */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "note" })
@XmlRootElement(name = "setting")
public class Setting {

    protected List<Note> note;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "default_value")
    protected String defaultValue;
    @XmlAttribute(required = true)
    protected boolean required;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String reference;
    @XmlAttribute
    protected String type;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String id;

    /** Gets the value of the note property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the note property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Note } */
    public List<Note> getNote() {
        if (note == null) {
            note = new ArrayList<Note>();
        }
        return this.note;
    }

    /** Gets the value of the name property.
     * 
     * @return possible object is {@link String } */
    public String getName() {
        return name;
    }

    /** Sets the value of the name property.
     * 
     * @param value allowed object is {@link String } */
    public void setName(String value) {
        this.name = value;
    }

    /** Gets the value of the defaultValue property.
     * 
     * @return possible object is {@link String } */
    public String getDefaultValue() {
        return defaultValue;
    }

    /** Sets the value of the defaultValue property.
     * 
     * @param value allowed object is {@link String } */
    public void setDefaultValue(String value) {
        this.defaultValue = value;
    }

    /** Gets the value of the required property. */
    public boolean isRequired() {
        return required;
    }

    /** Sets the value of the required property. */
    public void setRequired(boolean value) {
        this.required = value;
    }

    /** Gets the value of the reference property.
     * 
     * @return possible object is {@link String } */
    public String getReference() {
        return reference;
    }

    /** Sets the value of the reference property.
     * 
     * @param value allowed object is {@link String } */
    public void setReference(String value) {
        this.reference = value;
    }

    /** Gets the value of the type property.
     * 
     * @return possible object is {@link String } */
    public String getType() {
        return type;
    }

    /** Sets the value of the type property.
     * 
     * @param value allowed object is {@link String } */
    public void setType(String value) {
        this.type = value;
    }

    /** Gets the value of the id property.
     * 
     * @return possible object is {@link String } */
    public String getId() {
        return id;
    }

    /** Sets the value of the id property.
     * 
     * @param value allowed object is {@link String } */
    public void setId(String value) {
        this.id = value;
    }

}
