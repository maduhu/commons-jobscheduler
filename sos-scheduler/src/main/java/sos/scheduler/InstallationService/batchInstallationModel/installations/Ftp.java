//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, vJAXB 2.1.3 in JDK 1.6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2011.03.29 at 11:12:34 AM MESZ
//

package sos.scheduler.InstallationService.batchInstallationModel.installations;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element ref="{}user"/>
 *         &lt;element ref="{}password"/>
 *         &lt;element ref="{}localDir"/>
 *         &lt;element ref="{}remoteDir"/>
 *         &lt;element ref="{}port"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre> */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "user", "password", "localDir", "remoteDir", "port" })
@XmlRootElement(name = "ftp")
public class Ftp {

    @XmlElement(required = true)
    protected String user;
    @XmlElement(required = true)
    protected String password;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String localDir;
    @XmlElement(required = true)
    protected String remoteDir;
    protected short port;

    /** Gets the value of the user property.
     * 
     * @return possible object is {@link String } */
    public String getUser() {
        return user;
    }

    /** Sets the value of the user property.
     * 
     * @param value allowed object is {@link String } */
    public void setUser(String value) {
        this.user = value;
    }

    /** Gets the value of the password property.
     * 
     * @return possible object is {@link String } */
    public String getPassword() {
        return password;
    }

    /** Sets the value of the password property.
     * 
     * @param value allowed object is {@link String } */
    public void setPassword(String value) {
        this.password = value;
    }

    /** Gets the value of the localDir property.
     * 
     * @return possible object is {@link String } */
    public String getLocalDir() {
        return localDir;
    }

    /** Sets the value of the localDir property.
     * 
     * @param value allowed object is {@link String } */
    public void setLocalDir(String value) {
        this.localDir = value;
    }

    /** Gets the value of the remoteDir property.
     * 
     * @return possible object is {@link String } */
    public String getRemoteDir() {
        return remoteDir;
    }

    /** Sets the value of the remoteDir property.
     * 
     * @param value allowed object is {@link String } */
    public void setRemoteDir(String value) {
        this.remoteDir = value;
    }

    /** Gets the value of the port property. */
    public short getPort() {
        return port;
    }

    /** Sets the value of the port property. */
    public void setPort(short value) {
        this.port = value;
    }

}
