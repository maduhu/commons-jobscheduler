package com.sos.scheduler.model.commands;

import java.math.BigInteger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.answers.Calendar;

 
public class JSCmdShowCalendar extends ShowCalendar {

	private final String		conClassName	= "JSCmdShowCalendar";
	@SuppressWarnings("unused")
	private static final Logger	logger			= Logger.getLogger(JSCmdShowCalendar.class);
	private DatatypeFactory objDatatypeFactory 	= null;

	public JSCmdShowCalendar(SchedulerObjectFactory schedulerObjectFactory) {
		super();
		objFactory = schedulerObjectFactory;
	}

	public void setLimit(int i) {
		super.setLimit(BigInteger.valueOf(i));
	}

//	Doesn't work with IBM Java
//	
//	public void setFrom(String strDateAndTime) {
//		XMLGregorianCalendar objGC = XMLGregorianCalendarImpl.parse(strDateAndTime);
//		super.setFrom(objGC);
//	}
//	
//	public void setBefore(String strDateAndTime) {
//		XMLGregorianCalendar objGC = XMLGregorianCalendarImpl.parse(strDateAndTime);
//		super.setBefore(objGC);
//	}
	
	private DatatypeFactory datatypeFactoryInstance() {
		final String conMethodName = conClassName + "::datatypeFactoryInstance";
		if(objDatatypeFactory == null) {
			try {
				objDatatypeFactory = DatatypeFactory.newInstance();
			}
			catch (DatatypeConfigurationException e) {
				e.printStackTrace();
				throw new JobSchedulerException(String.format("%1$s: Can't get instantiate DatatypeFactory", conMethodName), e);
			}
		}
		return objDatatypeFactory;
	}
	
	public void setFrom(String strDateAndTime) {
		XMLGregorianCalendar objGC = datatypeFactoryInstance().newXMLGregorianCalendar(strDateAndTime);
		super.setFrom(objGC);
	}
	
	public void setBefore(String strDateAndTime) {
		XMLGregorianCalendar objGC = datatypeFactoryInstance().newXMLGregorianCalendar(strDateAndTime);
		super.setBefore(objGC);
	}
	
	public Calendar getCalendar() {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getCalendar";
		Calendar objCalendar = null;
		objCalendar = this.getAnswer().getCalendar();
//		objCalendar.setParent(objFactory);
		return objCalendar;
	} // public Calendar getCalendar


}
