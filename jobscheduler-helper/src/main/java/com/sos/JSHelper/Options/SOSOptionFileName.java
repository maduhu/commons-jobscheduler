package com.sos.JSHelper.Options;

import java.util.regex.Matcher;

import com.sos.JSHelper.io.Files.JSFile;

/** \class JSOptionFileName
 *
 * \brief JSOptionFileName -
 *
 * \details
 *
 * \section JSOptionFileName.java_intro_sec Introduction
 *
 * \section JSOptionFileName.java_samples Some Samples
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
 * @version $Id$23.01.2009 \see reference
 *
 *          Created on 23.01.2009 16:58:58 */
/** @author eqbfd */
public class SOSOptionFileName extends SOSOptionStringWVariables {

    /**
	 *
	 */
    private static final long serialVersionUID = -4059135218882474551L;
    private final String conClassName = "SOSOptionFileName";
    @SuppressWarnings("hiding")
    public final String ControlType = "file";
    protected JSFile objFile = null;

    /** \brief JSOptionFileName
     *
     * \details
     *
     * @param pPobjParent
     * @param pPstrKey
     * @param pPstrDescription
     * @param pPstrValue
     * @param pPstrDefaultValue
     * @param pPflgIsMandatory
     * @throws Exception */
    public SOSOptionFileName(final JSOptionsClass pPobjParent, final String pPstrKey, final String pPstrDescription, final String pPstrValue,
            final String pPstrDefaultValue, final boolean pPflgIsMandatory) {
        super(pPobjParent, pPstrKey, pPstrDescription, pPstrValue, pPstrDefaultValue, pPflgIsMandatory);
        this.JSFile();
    }

    public SOSOptionFileName(final String pstrFileName) {
        super(null, "", "", pstrFileName, "", false);
    }

    @Override
    public String getControlType() {
        return ControlType;
    }

    public String ValueWithFileSeparator() {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::Value";
        String strT = strValue.trim();
        if (IsNotEmpty()) { // http://www.sos-berlin.com/jira/browse/SOSFTP-198
            if (strValue.endsWith("/") || strValue.endsWith("\\")) {
            } else {
                strT = strValue + "/";
            }
        }
        return strT;
    } // private String Value

    /** \brief JSFile
     *
     * \details
     *
     * \return JSFile
     *
     * @return */
    public JSFile JSFile() {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::JSFile";
        if (objFile == null) {
            if (isNotEmpty(strValue)) {
                objFile = new JSFile(strValue);
            }
        }
        return objFile;
    } // private JSFile JSFile

    public void RelativeValue(final String pstrStringValue) {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::Value";
        strOriginalValue = pstrStringValue;
        String strT = pstrStringValue;
        if (strT.length() > 2 && (strT.startsWith("./") || strT.startsWith(".\\"))) { // relative
                                                                                      // path/file
                                                                                      // location
                                                                                      // (must
                                                                                      // be
                                                                                      // "./"
            strT = strT.replaceFirst("\\.", Matcher.quoteReplacement(getUserDir()));
        }
        super.Value(strT);
    } // public void Value

    public String getRelativeValue() {
        return strOriginalValue;
    }
}
