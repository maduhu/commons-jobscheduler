package com.sos.JSHelper.Options;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/** \class SOSOptionPortNumberTest
 * 
 * \brief SOSOptionPortNumberTest -
 * 
 * \details
 *
 * \section SOSOptionPortNumberTest.java_intro_sec Introduction
 *
 * \section SOSOptionPortNumberTest.java_samples Some Samples
 *
 * \code .... code goes here ... \endcode
 *
 * <p style="text-align:center">
 * <br />
 * --------------------------------------------------------------------------- <br />
 * APL/Software GmbH - Berlin <br />
 * ##### generated by ClaviusXPress (http://www.sos-berlin.com) ######### <br />
 * ---------------------------------------------------------------------------
 * </p>
 * \author KB \version 11.02.2011 \see reference
 *
 * Created on 11.02.2011 16:07:46 */

public class SOSOptionPortNumberTest {

    @SuppressWarnings("unused")
    private final String conClassName = "SOSOptionPortNumberTest";
    private static final Logger logger = Logger.getLogger(SOSOptionPortNumberTest.class);

    public SOSOptionPortNumber objPortNumber = new SOSOptionPortNumber( // ...
    null, // ....
    conClassName + ".variablename", // ...
    "OptionDescription", // ...
    "4444", // ...
    "4444", // ...
    true);

    public SOSOptionPortNumberTest() {
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
    public final void testValueString() {
        objPortNumber.Value("4711");
        assertEquals("port is 4711", 4711, objPortNumber.value());
    }

    @Test(expected = com.sos.JSHelper.Exceptions.JobSchedulerException.class)
    public final void testValueString2() {
        objPortNumber.Value("471111");
        assertEquals("port is 471111", 4711, objPortNumber.value());
    }

    @Test
    public final void testGetStandardSFTPPort() {
        int intSFTPPort = SOSOptionPortNumber.getStandardSFTPPort();
        assertEquals("sFTP port is 22", 22, intSFTPPort);
    }

    @Test
    public final void testGetStandardFTPPort() {
        int intFTPPort = SOSOptionPortNumber.getStandardFTPPort();
        assertEquals("FTP port is 21", 21, intFTPPort);
    }
}
