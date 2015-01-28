package com.sos.JSHelper.DataElements;


import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
* \class JSDataElementDateTimeTest 
* 
* \brief JSDataElementDateTimeTest - 
* 
* \details
*
* \section JSDataElementDateTimeTest.java_intro_sec Introduction
*
* \section JSDataElementDateTimeTest.java_samples Some Samples
*
* \code
*   .... code goes here ...
* \endcode
*
* <p style="text-align:center">
* <br />---------------------------------------------------------------------------
* <br /> APL/Software GmbH - Berlin
* <br />##### generated by ClaviusXPress (http://www.sos-berlin.com) #########
* <br />---------------------------------------------------------------------------
* </p>
* \author EQALS
* @version $Id$03.11.2009
* \see reference
*
* Created on 03.11.2009 14:54:22
 */

public class JSDataElementDateTimeTest {

	@SuppressWarnings("unused")
	private final String	conClassName	= "JSDataElementDateTimeTest";

	public JSDataElementDateTimeTest() {
		//
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
//	@Ignore("not relevant at this time")
	public void testISO() throws Exception {
		
		String strT;
		
		JSDataElementDateTime objDateTime = new JSDataElementDateTime("", "ValidFrom Date");
		Date objD = new Date(2011-1900, 0, 25, 15, 15);

		objDateTime.FormatString(JSDateFormat.dfDATE_SHORT);
		objDateTime.Value(objD);
		objDateTime.XMLTagName("ValidFromDate");
		strT = objDateTime.toXml();
		assertEquals("Date as XML is", "<ValidFromDate>2011-01-25</ValidFromDate>", strT);
		
	}
	
	@Test
	public void testConstructorDate () {
		Date objD = new Date(2011-1900, 0, 25, 15, 15);
		System.out.println("objD =" + objD.toLocaleString());
		JSDataElementDateTime objDT = new JSDataElementDateTime(objD);
		assertEquals("Date", "2011-01-25 15:15:00", objDT.FormattedValue());
	}
}
