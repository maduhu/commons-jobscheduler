package com.sos.VirtualFileSystem.SSH;

import java.io.InputStream;
import org.apache.log4j.Logger;

import sos.spooler.Variable_set;

import com.sos.VirtualFileSystem.common.SOSVfsBaseClass;
import com.sos.i18n.Msg;
import com.sos.i18n.Msg.BundleBaseName;
import com.sos.i18n.annotation.I18NResourceBundle;

/** \class SOSSSH2SuperClass
 * 
 * \brief SOSSSH2SuperClass -
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
 * \author UR
 * 
 * @version $Id: SOSSSH2BaseImpl.java 27533 2014-10-10 08:40:28Z ur $16.05.2010
 *          \see reference
 *
 *          Created on 16.05.2010 19:17:53 */

@I18NResourceBundle(baseName = "SOSVirtualFileSystem", defaultLocale = "en")
public class SOSSSH2BaseImpl extends SOSVfsBaseClass {

    private final String conClassName = "SOSSSH2BaseImpl";

    protected final Logger logger = Logger.getLogger(SOSSSH2BaseImpl.class);
    protected Msg objMsg = new Msg(new BundleBaseName(this.getClass().getAnnotation(I18NResourceBundle.class).baseName()));
    boolean isAuthenticated = false;
    boolean isConnected = false;

    /** Line currently being displayed on the shell **/
    protected String strCurrentLine = "";

    /** Inputstreams for stdout and stderr **/
    protected InputStream ipsStdOut;
    protected InputStream ipsStdErr;

    /** Output from stdout and stderr **/
    protected StringBuffer strbStdoutOutput;
    protected StringBuffer strbStderrOutput;

    /** timestamp of the last text from stdin or stderr **/
    protected long lngLastTime = 0;

    private Variable_set params = null;

    public SOSSSH2BaseImpl() {
        //
    }

    /** \brief setJSParam
     * 
     * \details
     *
     * \return SOSSSH2SuperClass
     *
     * @param pstrKey
     * @param pstrValue
     * @return */
    public SOSSSH2BaseImpl setJSParam(final String pstrKey, final String pstrValue) {

        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::setJSParam";

        if (params != null) {
            params.set_var(pstrKey, pstrValue);
        }

        return this;
    } // private SOSSSH2SuperClass setJSParam

    public SOSSSH2BaseImpl setParameters(final Variable_set pVariableSet) {

        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::setParameters";

        params = pVariableSet;

        return this;
    } // private void setParameters

    /** \brief replaceSchedulerVars
     * 
     * \details
     *
     * \return void
     *
     * @param isWindows */
    protected String replaceSchedulerVars(final boolean isWindows, final String pstrString2Modify) {
        String strTemp = pstrString2Modify;
        if (params != null) {
            logger.debug(SOSVfs_D_255.get());
            String[] paramNames = params.names().split(";");
            for (String name : paramNames) {
                SignalDebug(SOSVfs_D_256.params(name));
                String regex = "(?i)";
                /** \todo TODO os-abhängigkeit besser herstellen als hier gemacht
                 * 
                 * TODO Es ist doch eigentlich viel besser, wenn die Variablen
                 * (zusätzlich) als Environment-variablen gesetzt werden. */
                if (isWindows) {
                    regex += "%SCHEDULER_PARAM_" + name + "%";
                } else {
                    regex += "\\$\\{?SCHEDULER_PARAM_" + name + "\\}?";
                }
                strTemp = myReplaceAll(strTemp, regex, params.value(name));
            }
        }
        return strTemp;
    }

    /** \brief myReplaceAll
     * 
     * \details
     *
     * \return String
     *
     * @param source
     * @param what
     * @param replacement
     * @return */
    public String myReplaceAll(final String source, final String what, final String replacement) {

        String newReplacement = replacement.replaceAll("\\$", "\\\\\\$");
        return source.replaceAll("(?m)" + what, newReplacement);
    }

}
