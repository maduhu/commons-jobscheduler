//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, vJAXB 2.1.3 in JDK 1.6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2011.03.29 at 11:06:03 AM MESZ
//

package sos.scheduler.InstallationService.batchInstallationModel.installationFile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *       &lt;sequence>
 *         &lt;element ref="{}userInput"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="home"/>
 *             &lt;enumeration value="network"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre> */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "userInput" })
@XmlRootElement(name = "com.izforge.izpack.panels.UserInputPanel")
public class ComIzforgeIzpackPanelsUserInputPanel {

    @XmlElement(required = true)
    protected UserInput userInput;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String id;

    /** Gets the value of the userInput property.
     * 
     * @return possible object is {@link UserInput } */
    public UserInput getUserInput() {
        return userInput;
    }

    /** Sets the value of the userInput property.
     * 
     * @param value allowed object is {@link UserInput } */
    public void setUserInput(UserInput value) {
        this.userInput = value;
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
