//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2.3-3-
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2011.01.20 at 04:00:28 PM MEZ
//

package com.sos.scheduler.model.answers;

import java.io.Serializable;
import java.math.BigInteger;

import javax.annotation.Generated;
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
 *       &lt;attribute name="delay_after_error" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="directory" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="max" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="next_state" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="regex" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="repeat" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre> */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "file_order_source")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
public class FileOrderSource implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8675159262328648427L;
    @XmlAttribute(name = "delay_after_error", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    protected BigInteger delayAfterError;
    @XmlAttribute(name = "directory", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    protected String directory;
    @XmlAttribute(name = "max", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    protected BigInteger max;
    @XmlAttribute(name = "next_state", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    protected String nextState;
    @XmlAttribute(name = "regex", required = true)
    @XmlSchemaType(name = "anySimpleType")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    protected String regex;
    @XmlAttribute(name = "repeat", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    protected BigInteger repeat;

    /** Gets the value of the delayAfterError property.
     * 
     * @return possible object is {@link BigInteger } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public BigInteger getDelayAfterError() {
        return delayAfterError;
    }

    /** Sets the value of the delayAfterError property.
     * 
     * @param value allowed object is {@link BigInteger } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public void setDelayAfterError(BigInteger value) {
        this.delayAfterError = value;
    }

    /** Gets the value of the directory property.
     * 
     * @return possible object is {@link String } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public String getDirectory() {
        return directory;
    }

    /** Sets the value of the directory property.
     * 
     * @param value allowed object is {@link String } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public void setDirectory(String value) {
        this.directory = value;
    }

    /** Gets the value of the max property.
     * 
     * @return possible object is {@link BigInteger } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public BigInteger getMax() {
        return max;
    }

    /** Sets the value of the max property.
     * 
     * @param value allowed object is {@link BigInteger } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public void setMax(BigInteger value) {
        this.max = value;
    }

    /** Gets the value of the nextState property.
     * 
     * @return possible object is {@link String } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public String getNextState() {
        return nextState;
    }

    /** Sets the value of the nextState property.
     * 
     * @param value allowed object is {@link String } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public void setNextState(String value) {
        this.nextState = value;
    }

    /** Gets the value of the regex property.
     * 
     * @return possible object is {@link String } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public String getRegex() {
        return regex;
    }

    /** Sets the value of the regex property.
     * 
     * @param value allowed object is {@link String } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public void setRegex(String value) {
        this.regex = value;
    }

    /** Gets the value of the repeat property.
     * 
     * @return possible object is {@link BigInteger } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public BigInteger getRepeat() {
        return repeat;
    }

    /** Sets the value of the repeat property.
     * 
     * @param value allowed object is {@link BigInteger } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-01-20T04:00:28+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public void setRepeat(BigInteger value) {
        this.repeat = value;
    }

}
