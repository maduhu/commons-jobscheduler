package com.sos.JSHelper.Options;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.sos.JSHelper.DataElements.JSDataElementDate;
import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.i18n.annotation.I18NResourceBundle;

/**
* \class SOSOptionRegExp
*
* \brief SOSOptionRegExp -
*
* \details
*
* most popular and mostly asked regular expressions (from http://www.regextester.com/:
*
* Regular expression examples for decimals input
*
* Positive Integers --- ^\d+$
* Negative Integers --- ^-\d+$
* Integer --- ^-{0,1}\d+$
* Positive Number --- ^\d*\.{0,1}\d+$
* Negative Number --- ^-\d*\.{0,1}\d+$
* Positive Number or Negative Number - ^-{0,1}\d*\.{0,1}\d+$
* Phone number --- ^\+?[\d\s]{3,}$
* Phone with code --- ^\+?[\d\s]+\(?[\d\s]{10,}$
* Year 1900-2099 --- ^(19|20)[\d]{2,2}$
* Date (dd mm yyyy, d/m/yyyy, etc.) --- ^([1-9]|0[1-9]|[12][0-9]|3[01])\D([1-9]|0[1-9]|1[012])\D(19[0-9][0-9]|20[0-9][0-9])$
* IP v4 --- ^(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]){3}$
*
* Regular expression examples for Alphabetic input
*
* Personal Name --- ^[\w\.\']{2,}([\s][\w\.\']{2,})+$
* Username --- ^[\w\d\_\.]{4,}$
* Password at least 6 symbols --- ^.{6,}$
* Password or empty input --- ^.{6,}$|^$
* email --- ^[\_]*([a-z0-9]+(\.|\_*)?)+@([a-z][a-z0-9\-]+(\.|\-*\.))+[a-z]{2,6}$
* domain --- ^([a-z][a-z0-9\-]+(\.|\-*\.))+[a-z]{2,6}$
*
* Other regular expressions
*
* Match no input --- ^$
* Match blank input --- ^\s[\t]*$
* Match New line --- [\r\n]|$
*
* More examples on http://www.regular-expressions.info/examples.html
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
* \author KB
* @version $Id$17.05.2010
* \see reference
*
* Created on 17.05.2010 14:49:32
 */

/**
 * @author KB
 *
 */
@I18NResourceBundle(baseName = "com_sos_JSHelper_Messages", defaultLocale = "en")
public class SOSOptionRegExp extends SOSOptionStringWVariables {

	private static final long	serialVersionUID	= 8393808803161272343L;
	private final String		conClassName		= "SOSOptionRegExp";

	private Pattern				objCurrentPattern	= null;
	private int					intRegExpFlags		= Pattern.CASE_INSENSITIVE;

	private Matcher				matcher				= null;
	private String				strMatchValue		= "";

	/**
	 * \brief SOSOptionRegExp
	 *
	 * \details
	 *
	 * @param pPobjParent
	 * @param pPstrKey
	 * @param pPstrDescription
	 * @param pPstrValue
	 * @param pPstrDefaultValue
	 * @param pPflgIsMandatory
	 */
	public SOSOptionRegExp(final JSOptionsClass pPobjParent, final String pPstrKey, final String pPstrDescription, final String pPstrValue, final String pPstrDefaultValue,
			final boolean pPflgIsMandatory) {
		super(pPobjParent, pPstrKey, pPstrDescription, pPstrValue, pPstrDefaultValue, pPflgIsMandatory);
	}

	public SOSOptionRegExp(final String pstrRegExp) {
		super(null, "", "", pstrRegExp, "", false);
		
	}
	public int getRegExpFlags() {
		return intRegExpFlags;
	}

	public void setRegExpFlags(final int pintRegExpFlags) {
		intRegExpFlags = pintRegExpFlags;
	}

	public String getRegExpFlagsText() {

		String msg = "";
		if (has(intRegExpFlags, Pattern.CANON_EQ))
			msg += "CANON_EQ ";
		if (has(intRegExpFlags, Pattern.CASE_INSENSITIVE))
			msg += "CASE_INSENSITIVE ";
		if (has(intRegExpFlags, Pattern.COMMENTS))
			msg += "COMMENTS ";
		if (has(intRegExpFlags, Pattern.DOTALL))
			msg += "DOTALL ";
		if (has(intRegExpFlags, Pattern.MULTILINE))
			msg += "MULTILINE ";
		if (has(intRegExpFlags, Pattern.UNICODE_CASE))
			msg += "UNICODE_CASE ";
		if (has(intRegExpFlags, Pattern.UNIX_LINES))
			msg += "UNIX_LINES";

		return msg;
	}

	/**
	 * Tests if a flag is set or not
	 */
	private boolean has(final int flags, final int f) {
		return (flags & f) > 0;
	}

	/**
	 *
	 * \brief doReplace
	 *
	 * \details
	 *
	 * \return String
	 *
	 * @param pstrSourceString
	 * @param pstrReplacementPattern
	 * @return
	 * @throws Exception
	 */
	public String doReplace(final String pstrSourceString, final String pstrReplacementPattern) throws Exception {

		final String conMethodName = conClassName + "::doReplace";
		String strTargetString = pstrSourceString;

		try {
			strTargetString = replaceGroups(strTargetString, pstrReplacementPattern /* .split(";") */);
			strTargetString = substituteAllFilename(strTargetString, pstrSourceString);
			strTargetString = substituteAllDate(strTargetString);
			// TODO allow timestamp: as an alternative date-pattern
			strTargetString = substituteTimeStamp(strTargetString);
			// TODO implement uuid: as an additional pattern for substitution.
			strTargetString = substituteUUID(strTargetString);
			// TODO implement sqltimestamp: as an additional pattern for substitution
			strTargetString = substituteSQLTimeStamp(strTargetString);
			
			strValue = substituteTempFile(strValue);
			strValue = substituteSQLTimeStamp(strValue);
			strValue = substituteEnvironmenVariable(strValue);
			strValue = substituteFileContent(strValue);
			strValue = substituteShell(strValue);

			// // should any opening and closing brackets be found in the file name, then this is an error
//			Matcher m = Pattern.compile("\\[[^\\]]*\\]").matcher(strTargetString);
//			if (m.find()) {
//				throw new JobSchedulerException(String.format("unsupported variable found: ' %1$s'", m.group()));
//			}

			return strTargetString;
		}
		catch (Exception e) {
			throw new JobSchedulerException(conMethodName + ": " + e.getMessage(), e);
		}
	}

	/**
	 *
	 * \brief replaceGroups
	 *
	 * \details
	 *
	 * \return String
	 *
	 * @param pstrSourceString
	 * @param replacements
	 * @return
	 * @throws Exception
	 */
	public String replaceGroups(final String pstrSourceString, final String replacement) throws Exception {

		String result = "";

		if (replacement == null /* || replacements.length == 0 */) {
			throw new JobSchedulerException("replacements missing: 0 replacements defined");
		}

		Pattern p = Pattern.compile(strValue, intRegExpFlags); // the regular expression
		Matcher m = p.matcher(pstrSourceString); // check matching
		if (m.find() == false) { // then nothing matched
			return pstrSourceString;
		}

		String[] replacements = replacement.split(";");

		int intGroupCount = m.groupCount();
		// kb 2012-06-14 check disabled
		// if (replacements.length < intGroupCount)
		// throw new JobSchedulerException("replacements missing: " + replacements.length + " replacement(s) defined but " + intGroupCount +
		// " groups found");

		// no groups, exchange the whole string
		if (intGroupCount == 0) {
			result = pstrSourceString.substring(0, m.start()) + replacements[0] + pstrSourceString.substring(m.end());
		}
		else {
			int index = 0;
			// m.matches();

			for (int i = 1; i <= intGroupCount; i++) {
				int intStart = m.start(i);
				// kb 2012-06-14 check bounds added
				if (intStart >= 0 && i <= replacements.length) {
					String strRepl = replacements[i - 1].trim();
					if (strRepl.length() > 0) {
						if (strRepl.contains("\\") == true) {
							strRepl = strRepl.replaceAll("\\\\-", "");
							for (int j = 1; j <= intGroupCount; j++) {
								strRepl = strRepl.replaceAll("\\\\" + j, m.group(j));
							}
						}
						result += pstrSourceString.substring(index, intStart) + strRepl;
					}
				}
				index = m.end(i);
			}
			result += pstrSourceString.substring(index);
		}

		return result;
	}

	public String doRegExpReplace(final String pstrSourceString, final String pstrReplacementPattern) throws Exception {

		final String conMethodName = conClassName + "::doRegExpReplace";
		String strTargetString = pstrSourceString;

		try {

			Pattern pattern = Pattern.compile(strValue);
			Matcher matcher1 = pattern.matcher(pstrSourceString);
			strTargetString = matcher1.replaceAll(pstrReplacementPattern);

			 strTargetString = substituteAllDate(strTargetString);
			 strTargetString = substituteAllFilename(strTargetString, pstrSourceString);

			return strTargetString;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			throw new JobSchedulerException(conMethodName + ": " + e.getMessage());
		}
	}


	@Override
	public String substituteAllDate(String targetFilename) throws Exception {

		String temp = substituteFirstDate(targetFilename);

		while (!targetFilename.equals(temp)) {
			targetFilename = temp;
			temp = substituteFirstDate(targetFilename);
		}

		return temp;
	}

	private String substituteFirstDate(String targetFilename) throws Exception {

		final String conVarName = "[date:";
		try {
			// check for a date format string given in the file mask
			if (targetFilename.matches("(.*)(\\" + conVarName + ")([^\\]]*)(\\])(.*)")) {
				int posBegin = targetFilename.indexOf(conVarName);
				if (posBegin > -1) {
					int posEnd = targetFilename.indexOf("]", posBegin + 6);
					if (posEnd > -1) {
						String strDateMask = targetFilename.substring(posBegin + 6, posEnd);
						String strDateTime = JSDataElementDate.getCurrentTimeAsString(strDateMask);
						String strT = (posBegin > 0 ? targetFilename.substring(0, posBegin) : "") + strDateTime;
						if (targetFilename.length() > posEnd) {
							strT += targetFilename.substring(posEnd + 1);
						}
						targetFilename = strT;
					}
				}
			}

			return targetFilename;

		}
		catch (Exception e) {
			throw new JobSchedulerException("error substituting [date:]: " + e.getMessage(), e);
		}
	}

	@Override
	public String substituteAllFilename(String targetFilename, final String original) throws Exception {

		// original ist das replacement; es ist der urspruengliche Dateiname inklusive Endung
		String temp = substituteFirstFilename(targetFilename, original);

		while (!targetFilename.equals(temp)) {
			targetFilename = temp;
			temp = substituteFirstFilename(targetFilename, original);
		}

		return temp;
	}

	private String substituteSQLTimeStamp(String strValue) throws Exception {

		// check for [filename:...]
		Matcher matcher1 = Pattern.compile("\\[sqltimestamp:([^\\]]*)\\]", intRegExpFlags).matcher(strValue);

		if (matcher1.find()) {
			if (matcher1.group(1).equals("")) {
				strValue = strValue.replaceFirst("\\[sqltimestamp:\\]", new Timestamp(new Date().getTime()).toString() );
			}
		}

		return strValue;
	}

	public static String getSqlTimeStamp () {
		return new Timestamp(new Date().getTime()).toString() ;
	}

	private String substituteFirstFilename(String targetFilename, final String original) throws Exception {

		// check for [filename:...]
		Matcher matcher1 = Pattern.compile("\\[filename:([^\\]]*)\\]", intRegExpFlags).matcher(targetFilename);

		if (matcher1.find()) {
			if (matcher1.group(1).equals("")) {
				targetFilename = targetFilename.replaceFirst("\\[filename:\\]", original);
			}
			else
				if (matcher1.group(1).equals("lowercase")) {
					targetFilename = targetFilename.replaceFirst("\\[filename:lowercase\\]", original.toLowerCase());
				}
				else
					if (matcher1.group(1).equals("uppercase")) {
						targetFilename = targetFilename.replaceFirst("\\[filename:uppercase\\]", original.toUpperCase());
					}
		}

		return targetFilename;
	}

	public Pattern getPattern(final String pstrValue) {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getPattern";

		strValue = pstrValue;

		return this.getPattern();
	} // private Pattern getPattern

	public Pattern getPattern() {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getPattern";

		Pattern p = null;
		try {
			p = Pattern.compile(strValue, intRegExpFlags);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		objCurrentPattern = p;

		return p;
	} // private Pattern getPattern

	// TODO kann in die basisklasse
	private Vector <String> lstMatchValues = null;
	public void addValue(final String pstrValue4Matching) {
		if (lstMatchValues == null) {
			lstMatchValues = new Vector();
		}
		lstMatchValues.add(pstrValue4Matching);
	}

	public boolean match(final String pstrValue4Matching) {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::match";

		strMatchValue = "";
		if (objCurrentPattern == null) {
			this.getPattern();
		}
		boolean flgFound = false;
		matcher = objCurrentPattern.matcher(pstrValue4Matching);
		if (matcher.find()) {
			flgFound = true;
			strMatchValue = pstrValue4Matching;
		} // ^(.*/)2.*\.txt$

		if (flgFound == false && lstMatchValues != null) {
			for (String strValue4Matching : lstMatchValues) {
				Pattern p = Pattern.compile(strValue4Matching, intRegExpFlags);
				matcher = objCurrentPattern.matcher(pstrValue4Matching);
				if (matcher.find()) {
					flgFound = true;
					strMatchValue = pstrValue4Matching;
					break;
				}
			}
		}
		return flgFound;
	} // private boolean match

	public String getGroup(final int pintGroupNo) {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getGroup";
		String strRetVal = null;
		this.match(strMatchValue);
		if (matcher != null) {
			strRetVal = matcher.group(pintGroupNo);
		}

		return strRetVal;
	} // private String getGroup

	@Override
	public void Value (final String pstrValue) {
		super.Value(pstrValue);  // make first some text replacements
		String strV = super.Value();
		if (isNotEmpty(strV)) {
//		String strV = pstrValue;
	        try {
	            Pattern.compile(strV);
	        } catch (PatternSyntaxException exception) {
	        	String strT = String.format("The RegExp '%1$s' is invalid", strV);
	        	SOSValidationError objVE = new SOSValidationError(strT);
	        	objVE.setException(new JobSchedulerException(strT, exception));
//	            System.err.println(exception.getDescr  iption());
	        }
		}
	}
}
