//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2.3-3-
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2014.01.28 at 10:30:50 AM MEZ
//

package com.sos.scheduler.model.answers;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{}task.statistics"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre> */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "taskStatistics" })
@XmlRootElement(name = "task_subsystem.statistics")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2014-01-28T10:30:50+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
public class TaskSubsystemStatistics implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6098426926969857660L;
    @XmlElement(name = "task.statistics", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2014-01-28T10:30:50+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    protected TaskStatistics taskStatistics;

    /** Gets the value of the taskStatistics property.
     * 
     * @return possible object is {@link TaskStatistics } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2014-01-28T10:30:50+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public TaskStatistics getTaskStatistics() {
        return taskStatistics;
    }

    /** Sets the value of the taskStatistics property.
     * 
     * @param value allowed object is {@link TaskStatistics } */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2014-01-28T10:30:50+01:00", comments = "JAXB RI v2.2.3-hudson-jaxb-ri-2.2.3-3-")
    public void setTaskStatistics(TaskStatistics value) {
        this.taskStatistics = value;
    }

}
