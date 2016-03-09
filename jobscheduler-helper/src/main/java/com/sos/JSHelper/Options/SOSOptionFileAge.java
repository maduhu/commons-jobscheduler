package com.sos.JSHelper.Options;

import com.sos.JSHelper.Exceptions.JobSchedulerException;

/** \class SOSOptionFileAge
 * 
 * \brief SOSOptionFileAge -
 * 
 * \details
 * 
 * \section SOSOptionFileAge.java_intro_sec Introduction
 *
 * \section SOSOptionFileAge.java_samples Some Samples
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
 * \author KB
 * 
 * @version $Id$28.08.2010 \see reference
 *
 *          Created on 28.08.2010 22:37:59 */

/** @author KB */
public class SOSOptionFileAge extends SOSOptionString {

    private static final String conRegExpDigits = "[\\d]+";
    /**
	 * 
	 */
    private static final long serialVersionUID = -5758606786206972123L;
    @SuppressWarnings("unused")
    private final String conClassName = "SOSOptionFileAge";

    /** \brief SOSOptionFileAge
     *
     * \details
     *
     * @param pPobjParent
     * @param pPstrKey
     * @param pPstrDescription
     * @param pPstrValue
     * @param pPstrDefaultValue
     * @param pPflgIsMandatory */
    public SOSOptionFileAge(JSOptionsClass pPobjParent, String pPstrKey, String pPstrDescription, String pPstrValue, String pPstrDefaultValue,
            boolean pPflgIsMandatory) {
        super(pPobjParent, pPstrKey, pPstrDescription, pPstrValue, pPstrDefaultValue, pPflgIsMandatory);
    }

    /** @param fileage sec or hours:min[:sec]
     * @return long value
     * @throws JobSchedulerException */
    private long calculateFileAge() {

        long age = 0;

        long lngNoOfDays = 0;
        long lngNoOfHours = 0;
        long lngNoOfMinutes = 0;
        long lngNoOfSeconds = 0;
        String strFileAge = this.strValue;
        String strM = String.format("'%1$s' is not a valid string for a file-age value", strFileAge);

        if (strFileAge == null || strFileAge.trim().length() == 0)
            return 0;

        if (strFileAge.indexOf(" ") > 0) {
            String[] strA = strFileAge.split(" ");
            long lngV = Long.parseLong(strA[0]);
            String strUoM = strA[1].toLowerCase();
            if (strUoM.startsWith("day")) {
                lngNoOfDays = lngV;
            } else {
                if (strUoM.startsWith("week")) {
                    lngNoOfDays = lngV * 7;
                } else {
                    if (strUoM.startsWith("month")) {
                        lngNoOfDays = lngV * 30;
                    } else {
                        if (strUoM.startsWith("hour")) {
                            lngNoOfHours = lngV;
                        } else {
                            if (strUoM.startsWith("minute")) {
                                lngNoOfMinutes = lngV;
                            } else {
                                if (strUoM.startsWith("second")) {
                                    lngNoOfSeconds = lngV;
                                }
                            }
                        }
                    }
                }
            }
        } else { // sec ?
            if (strFileAge.indexOf(":") == -1) {
                if (!strFileAge.matches(conRegExpDigits)) {
                    throw new JobSchedulerException(strM);
                } else {
                    lngNoOfSeconds = Long.parseLong(strFileAge);
                }
            } else { // hours:min[:sec]
                if (!strFileAge.matches("^[\\d].*[\\d]$")) {
                    throw new JobSchedulerException(strM);
                }

                String[] timeArray = strFileAge.split(":");

                if (timeArray.length > 3) {
                    throw new JobSchedulerException(strM);
                }

                for (int i = 0; i < timeArray.length; i++) {
                    if (!timeArray[i].matches(conRegExpDigits)) {
                        throw new JobSchedulerException(strM);
                    }
                }

                lngNoOfHours = Long.parseLong(timeArray[0]);
                lngNoOfMinutes = Long.parseLong(timeArray[1]);
                lngNoOfSeconds = 0;

                if (timeArray.length > 2) {
                    lngNoOfSeconds = Long.parseLong(timeArray[2]);
                }
            }
        }

        age = (lngNoOfDays * 3600 * 24) + (lngNoOfHours * 3600) + (lngNoOfMinutes * 60) + (lngNoOfSeconds);

        return age;
    }

    public long getAgeAsMS() {

        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::getAgeAsMS";

        return calculateFileAge() * 1000;

    } // private long getAgeAsMS

    public long getAgeAsSeconds() {

        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::getAgeAsMS";

        return calculateFileAge();

    } // private long getAgeAsSeconds

}
