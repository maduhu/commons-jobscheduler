package com.sos.JSHelper.Annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Target;

/** \class JSOptionClass
 *
 * \brief JSOptionClass -
 *
 * \details
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
 * \author eqbfd
 * 
 * @version $Id$12.05.2009 \see reference
 *
 *          Created on 12.05.2009 09:24:12 */

@Target({ TYPE, FIELD })
public @interface JSOptionClass {

    String name(); // der Name des Segments

    String description();

    String version() default "1.0";

    String prefix() default "";
}
