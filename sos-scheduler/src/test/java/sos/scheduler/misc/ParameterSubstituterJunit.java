package sos.scheduler.misc;

import static org.junit.Assert.*;

import org.junit.Test;

/** \class ParameterSubstituterJunit
 * 
 * \brief ParameterSubstituterJunit -
 * 
 * \details
 *
 * \section ParameterSubstituterJunit.java_intro_sec Introduction
 *
 * \section ParameterSubstituterJunit.java_samples Some Samples
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
 * \author Uwe Risse \version 14.10.2011 \see reference
 *
 * Created on 05.04.2013 11:24:40 */

public class ParameterSubstituterJunit {

    @SuppressWarnings("unused")
    private final String conClassName = "SortNumberComparatorTest";

    public ParameterSubstituterJunit() {
        //
    }

    @Test
    public void testReplace() {
        ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor();
        parameterSubstitutor.addKey("par1", "value of par1");
        parameterSubstitutor.addKey("par2", "value of par2");
        String source = "The value of par1 is ${par1} and the value of par2 is ${par2} ${par3} is not set";
        String erg = parameterSubstitutor.replace(source);
        assertEquals("testReplace failed: ", "The value of par1 is value of par1 and the value of par2 is value of par2 ${par3} is not set", erg);
    }

    @Test
    public void replaceSystemProperties() {
        ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor();
        String source = "The value of user.name is ${user.name}";
        String erg = parameterSubstitutor.replaceSystemProperties(source);
        assertEquals("testReplace failed: ", "The value of user.name is " + System.getProperty("user.name"), erg);
    }

    @Test
    public void replaceEnvVars() {
        ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor();
        String source = "The value of OS is ${OS}";
        String erg = parameterSubstitutor.replaceEnvVars(source);
        assertEquals("testReplace failed: ", "The value of OS is " + System.getenv("OS"), erg);
    }

}
