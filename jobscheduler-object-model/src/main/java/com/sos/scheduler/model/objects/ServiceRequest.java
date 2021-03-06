//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2.3-3-
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2011.01.17 at 03:00:56 PM MEZ
//

package com.sos.scheduler.model.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *       &lt;sequence>
 *         &lt;element name="web_service" type="{}web_service" minOccurs="0"/>
 *         &lt;element name="content">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;any/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="url" type="{}String" />
 *       &lt;attribute name="url_path" type="{}String" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre> */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "webService", "content" })
@XmlRootElement(name = "service_request")
public class ServiceRequest extends JSObjBase {

    @XmlElement(name = "web_service")
    protected WebService webService;
    @XmlElement(required = true)
    protected ServiceRequest.Content content;
    @XmlAttribute(name = "url")
    protected String url;
    @XmlAttribute(name = "url_path")
    protected String urlPath;

    /** Gets the value of the webService property.
     * 
     * @return possible object is {@link WebService } */
    public WebService getWebService() {
        return webService;
    }

    /** Sets the value of the webService property.
     * 
     * @param value allowed object is {@link WebService } */
    public void setWebService(WebService value) {
        this.webService = value;
    }

    /** Gets the value of the content property.
     * 
     * @return possible object is {@link ServiceRequest.Content } */
    public ServiceRequest.Content getContent() {
        return content;
    }

    /** Sets the value of the content property.
     * 
     * @param value allowed object is {@link ServiceRequest.Content } */
    public void setContent(ServiceRequest.Content value) {
        this.content = value;
    }

    /** Gets the value of the url property.
     * 
     * @return possible object is {@link String } */
    public String getUrl() {
        return url;
    }

    /** Sets the value of the url property.
     * 
     * @param value allowed object is {@link String } */
    public void setUrl(String value) {
        this.url = value;
    }

    /** Gets the value of the urlPath property.
     * 
     * @return possible object is {@link String } */
    public String getUrlPath() {
        return urlPath;
    }

    /** Sets the value of the urlPath property.
     * 
     * @param value allowed object is {@link String } */
    public void setUrlPath(String value) {
        this.urlPath = value;
    }

    /** <p>
     * Java class for anonymous complex type.
     * 
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;any/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre> */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "any" })
    public static class Content {

        @XmlAnyElement(lax = true)
        protected Object any;

        /** Gets the value of the any property.
         * 
         * @return possible object is {@link Object } */
        public Object getAny() {
            return any;
        }

        /** Sets the value of the any property.
         * 
         * @param value allowed object is {@link Object } */
        public void setAny(Object value) {
            this.any = value;
        }

    }

}
