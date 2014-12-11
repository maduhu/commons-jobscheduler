package com.sos.JSHelper.DataElements;

/**
 * \class JSDataElement
 *
 * \brief JSDataElement - Basisklasse f�r die Datenelement-Klassen des Dataswitch
 *
 * \details
 * Basisklasse (SuperKlasse) f�r die Datenelement-Klassen des Dataswitch
 *
 * \section JSDataElement_intro_sec Introduction
 *
 * \section JSDataElement_samples Some Samples
 *
 * \code
 *   .... code goes here ...
 * \endcode
 *
 * <p style="text-align:center">
 * <br />---------------------------------------------------------------------------
 * <br /> APL/Software GmbH - Berlin
 * <br />##### generated by ClaviusXPress (http://www.sos-berlin.com) #########
 * <br />Sonntag, 7. Dezember 2008, eqbfd (eqbfd)
 * <br />---------------------------------------------------------------------------
 * </p>
 * \author eqbfd
* @version $Id$0.9
 *
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Exceptions.FormatPatternException;
import com.sos.JSHelper.io.Files.JSXMLFile;

/**
 * @author EQCPN
 *
 */
public class JSDataElement extends JSToolBox {

	private final String	conClassName					= "JSDataElement";

	public static boolean	flgXMLTagOnSingleLine			= false;
	public static boolean	flgTrimValues					= false;
	public static boolean	flgOmitEmptyXMLTags				= true;
	/**
	 * steuert, ob bei einem �berschreiten der Feldl�nge eine Exception ausgel�st werden soll
	 */
	public static boolean	flgExceptionOnFieldTruncation	= true;

	private String			strValue						= "";
	private String			strDefaultValue					= "";

	private String			strDescription					= "";

	private int				intSize							= 0;				// Size in Bytes
	public int				intPos							= 0;				// Position in einem Fix-File Record (IDoc-Segment z.B.)
	private String			strTitle						= "";
	private String			strColumnHeader					= "";

	private String			strXMLTagName					= "";
	private boolean			flgOmitXMLTag					= false;
	private String			strFormatString					= "";				// geh�rt in Integer, Double, etc Datenelement

	// EQCPN-2009-04-25: Formatpr�fung
	private String			strFormatPattern				= "";				// Muster f�r die Formatierungspr�fung
	private boolean			flgAllowEmptyValue				= true;			// Leeres Feld erlaubt

	private boolean			flgTrimValue					= false;

	/**
	 * \todo Werte(bereichs)pr�fung mit dem regul�ren Ausdruck einbauen
	 */

	@SuppressWarnings("unused")
	private final String	strRegExpr4Check				= "";
	private boolean			flgIsCData						= false;

	private boolean			flgIsDirty						= false;

	public void isDirty(final boolean pflgIsDirty) {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::isDirty";

		flgIsDirty = pflgIsDirty;

	} // public void isDirty}

	public boolean isDirty() {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::isDirty";

		return flgIsDirty;
	} // public boolean isDirty}

	public void isCData(final boolean pflgIsCData) {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::isCData";

		flgIsCData = pflgIsCData;

	} // public void isCData}

	public boolean isCData() {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::isCData";

		return flgIsCData;
	} // public boolean isCData}

	/**
	 *
	 * \brief toString
	 *
	 * \details
	 *
	 * \return
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return strDescription + ": " + strValue;
	}

	/**
	 *
	 * \brief JSDataElement
	 *
	 * \details
	 *
	 */
	public JSDataElement() {
		// TODO Auto-generated constructor stub
		doInit();
	}

	public JSDataElement(final String pstrValue) {
		Value(pstrValue);
		doInit();
	}

	// omit

	public JSDataElement(final String pstrValue, final String pstrDescription) {
		this.Value(pstrValue);
		this.Description(pstrDescription);
		doInit();
	}

	/**
	 *
	 * \brief JSDataElement
	 *
	 * \details
	 * Dieser Konstruktur wird i.d.R. verwendet vom Generator, der aus der Dokumentation der IDocs (und Segmente)
	 * die entsprechende Java-Klasse erzeugt.
	 *
	 * @param pstrValue
	 * @param pstrDescription
	 * @param pintSize
	 * @param pintPos
	 * @param pstrFormatString
	 * @param pstrColumnHeader
	 * @param pstrXMLTagName
	 */
	public JSDataElement(final String pstrValue, //
			final String pstrDescription, //
			final int pintSize, //
			final int pintPos, //
			final String pstrFormatString, //
			final String pstrColumnHeader, //
			final String pstrXMLTagName) {

		doInit();

		this.Value(pstrValue);
		this.Description(pstrDescription);

		this.Size(pintSize);
		intPos = pintPos;
		this.FormatString(pstrFormatString.trim()); // the content must not be numeric
		this.Description(pstrDescription);
		if (pstrColumnHeader.length() <= 0) {
			this.ColumnHeader(pstrDescription);
		}
		else {
			this.ColumnHeader(pstrColumnHeader);
		}
		if (pstrXMLTagName.length() <= 0) {
			this.XMLTagName(pstrDescription);
		}
		else {
			this.XMLTagName(pstrXMLTagName);
		}

	}

	/**
	 *
	 * \brief ColumnHeader
	 *
	 * \details
	 *
	 * \return JSDataElement
	 *
	 * @param pstrColumnHeader
	 * @return
	 */
	public JSDataElement ColumnHeader(final String pstrColumnHeader) {
		if (pstrColumnHeader != null) {
			strColumnHeader = pstrColumnHeader;
		}
		return this;
	}

	/**
	 *
	 * \brief ColumnHeader
	 *
	 * \details
	 *
	 * \return String
	 *
	 * @return
	 */
	public String ColumnHeader() {
		if (strColumnHeader == null) {
			strColumnHeader = "";
		}

		return strColumnHeader;
	}

	/**
	 * \brief Title - Titel des Datenelements festlegen
	 *
	 * \details
	 *
	 * @param pstrTitle
	 * @return
	 */
	public JSDataElement Title(final String pstrTitle) {
		if (pstrTitle != null) {
			strTitle = pstrTitle;
		}
		return this;
	}

	/**
	 * \brief Title - Titel des Datenelements liefern
	 *
	 * \details
	 *
	 * @param pstrTitle
	 * @return
	 */
	public String Title() {
		if (strTitle == null) {
			strTitle = "";
		}

		return strTitle;
	}

	public void Value(final JSDataElement objDE) {
		Value(objDE.Value());
	}

	/**
	 * \brief Value - Wert des Datenelements festlegen
	 *
	 * \details
	 *
	 * @param pstrValue
	 */
	public void Value(final String pstrValue) {
		if (pstrValue != null) {
			if (strValue.equals(pstrValue) == false) {
				isDirty(true);
				/**
				 * \todo �ber den regexp pr�fen, ob die Werte syntaktisch korrekt sind
				 */
				strValue = pstrValue;
				if (flgTrimValue || flgTrimValues) {
					strValue = strValue.trim();
				}
			}
		}
		else {
			strValue = "";
		}

	}

	/**
	 * \brief Value - Wert des Datenelements liefern
	 *
	 * \details
	 *
	 * @param pstrValue
	 * @return
	 */
	public String Value() {
		if (strValue == null) {
			strValue = "";
		}

		return strValue;
	}

	/**
	 *
	 * \brief DefaultValue
	 *
	 * \details
	 *
	 * \return JSDataElement
	 *
	 * @param pstrDefaultValue
	 * @return
	 */
	public JSDataElement DefaultValue(final String pstrDefaultValue) {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::DefaultValue";

		strDefaultValue = pstrDefaultValue;

		return this;
	} // public JSDataElement DefaultValue

	public String DefaultValue() {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::DefaultValue";

		return strDefaultValue;
	} // public String DefaultValue

	/**
	 * \brief Description - Beschreibung des Datenelements festlegen
	 *
	 * \details
	 *
	 * @param pstrDescription
	 * @return
	 */
	public JSDataElement Description(final String pstrDescription) {
		if (pstrDescription != null) {
			strDescription = pstrDescription;
		}
		return this;
	}

	/**
	 * \brief Description - Beschreibung des Datenelements liefern
	 *
	 * \details
	 *
	 * @param pstrDescription
	 * @return String
	 */
	public String Description() {
		if (strDescription == null) {
			strDescription = "";
		}

		return strDescription;
	}

	/**
	 * \brief Size - Gr��e des Datenelements festlegen
	 *
	 * \details
	 *
	 * @param pintSize
	 * @return
	 */
	public JSDataElement Size(final int pintSize) {
		intSize = pintSize;
		return this;
	}

	public JSDataElement Size(final Integer pintSize) {
		intSize = pintSize;
		return this;
	}

	/**
	 * \brief Size - Gr��e des Datenelements liefern
	 *
	 * \details
	 *
	 * @param pintSize
	 * @return String
	 */
	public int Size() {
		return intSize;
	}

	public Integer ISize() {
		return new Integer(intSize);
	}

	/**
	 * \brief XMLTagName - XML-TagName des Datenelements festlegen
	 *
	 * \details
	 *
	 * @param pstrXMLTagName
	 * @return
	 */
	public JSDataElement XMLTagName(final String pstrXMLTagName) {
		if (pstrXMLTagName != null) {
			strXMLTagName = pstrXMLTagName;
		}
		return this;
	}

	/**
	 * \brief XMLTagName - XML-TagName des Datenelements liefern
	 *
	 * \details
	 *
	 * @param pstrXMLTagName
	 * @return String
	 */
	public String XMLTagName() {
		if (strXMLTagName == null) {
			strXMLTagName = "";
		}

		return strXMLTagName;
	}

	/**
	 *
	 * \brief toXml
	 *
	 * \details
	 *
	 * \return String
	 *
	 * @param pstrTagName
	 * @return
	 */
	public String toXml(final String pstrTagName) {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::toXml";

		if (OmittXMLTag() == true) {
			if (this instanceof JSDataElementDouble) {
				final JSDataElementDouble objT = (JSDataElementDouble) this;
				if (objT.dblValue == 0.0) {
					return "";
				}
			}
			if (this instanceof JSDataElementDate) {
				final JSDataElementDate objD = (JSDataElementDate) this;
				if (objD.isEmpty() == true) {
					return "";
				}
			}
			if (FormattedValue().length() <= 0) {
				return "";
			}
		}
		String strT = "";
		if (this.Value().length() > 0) {
			strT = "<" + pstrTagName + ">";

			if (this.isCData() == true) {
				strT += "<![CDATA[" + FormattedValue() + "]]>";
			}
			else {
				strT += FormattedValue();
			}
			strT += "</" + pstrTagName + ">";
		}
		else {
			strT = "<" + pstrTagName + "/>";

		}

		if (flgXMLTagOnSingleLine == true) {
			strT += "\n";
		}
		return strT;
	} // public String toXml

	/**
	 * \brief toXml - XML-Tag mit dem Wert des Elements liefern
	 *
	 * \details
	 *
	 * @return String - den Wert des Elements im XML-Tag
	 */
	public String toXml() {
		return this.toXml(this.XMLTagName());
	}

	/**
	 *
	 * \brief toXml
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param pobjXMLFile
	 * @throws Exception
	 */
	public void toXml(final JSXMLFile pobjXMLFile) throws Exception {
		pobjXMLFile.WriteLine(this.toXml());
	}

	/**
	 *
	 * \brief FormatString
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param pstrFormatString
	 */
	public void FormatString(final String pstrFormatString) {
//- <remark who='EQALS' when='Donnerstag, 11. Juni 2009' id='Korrektur' >
		/**
		 * \change Donnerstag, 11. Juni 2009 EQALS Korrektur
		 * Korrektur Initialisierung FormatString
		 */
//- <oldcode>
//		if (pstrFormatString == null) {
//			strFormatString = "";
//		}
//		else {
//			strFormatString = pstrFormatString;
//		}
//- </oldcode>
//- <newcode>
		if (isEmpty(pstrFormatString) == false) {
			strFormatString = pstrFormatString;
		}
		else {
			if (isEmpty(strFormatString)) {
				strFormatString = pstrFormatString;
			}
			else {
				// Default-Initialisierung nicht �berschreiben
			}
		}
//- </newcode>
//- </remark>      <!-- id=<Korrektur>  -->

	}

	public void FormatString(final JSDateFormat pJSDateFormat) {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::FormatString";

		this.FormatString(pJSDateFormat.toPattern());

	} // public void FormatString}

	/**
	 *
	 * \brief FormatString - liefert den f�r das Datenelement definierten FormatString
	 *
	 * \details
	 *
	 * \return String
	 *
	 * @return
	 */
	public String FormatString() {
		if (strFormatString == null) {
			strFormatString = "";
		}

		return strFormatString;
	}

	public void doInit() {
		//

	}

	/**
	 *
	 * \brief FormattedValue - Liefert den Wert des Elements formatiert
	 *
	 * \details
	 * das Format (die Edit-Maske) wird �ber die Eigenschaft FormatString
	 * definiert.
	 *
	 * Wenn kein Format-String definiert ist, so wird der Wert als String
	 * zur�ckgegeben.
	 *
	 * \return String
	 *
	 * @return
	 */
	public String FormattedValue() {
		// nur �berschreibbar

		return this.Value();
	}

	/**
	 *
	 * \brief OmitXMLTag
	 *
	 * \details
	 * omit
	 * \return JSDataElement
	 *
	 * @param pflgOmitXMLTag
	 * @return
	 */
	public JSDataElement OmitXMLTag(final boolean pflgOmitXMLTag) {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::OmitXMLTag";

		flgOmitXMLTag = pflgOmitXMLTag;

		return this;
	} // public JSDataElement OmitXMLTag

	/**
	 *
	 * \brief OmittXMLTag
	 *
	 * \details
	 *
	 * \return boolean
	 *
	 * @return
	 */
	public boolean OmittXMLTag() {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::OmittXMLTag";

		boolean flgF = flgOmitXMLTag;
//		if (flgOmitXMLTag || flgOmitEmptyXMLTags) {
		if (flgOmitXMLTag) {
			flgF = true;
		}
		return flgF;

	} // public boolean OmittXMLTag}

	/**
	 *
	 * \brief IsEmpty
	 *
	 * \details
	 * Ist Value-String leer, dann liefert die Methode true
	 *
	 * \return boolean
	 *
	 * @return
	 */
	public boolean IsEmpty() {
		return this.Value().trim().length() == 0;
	}

	public boolean IsNotEmpty () {
		return !IsEmpty();
	}
//	
	/**
	 *
	 * \brief hasValue
	 *
	 * \details
	 *
	 * \return boolean
	 *
	 * @return
	 */
	public boolean hasValue() {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::hasValue";

		return this.Value().trim().length() > 0;
	} // public boolean hasValue}

	/**
	 *
	 * \brief TrimValue
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param pflgTrimValue
	 */
	public void TrimValue(final boolean pflgTrimValue) {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::TrimValue";

		flgTrimValue = pflgTrimValue;

	} // public void TrimValue

	/**
	 *
	 * \brief RecordValue - setzt den Wert aus einem festformatierten Satz (z.B. IDoc-Segement)
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param pstrRecord
	 */
	public void RecordValue(final StringBuffer pstrRecord) {

		this.Value(pstrRecord.substring(intPos, intPos + intSize).trim());
	}

	/**
	 *
	 * \brief BuildRecord
	 *
	 * \details
	 * Mit dieser Methode wird der aktuelle Wert f�r das Datenfeld in einen "festen" Record (z.B. das Segment eines IDocs)
	 * an die definierte Stelle in der definierten L�nge eingesetzt.
	 *
	 * eigentlich nehmen wir den Wert wie er ist.
	 * Ist allerdings ein format-string definiert, dann wird der
	 * mit diesem String formatierte Wert verwendet.
	 * \return void
	 *
	 * @param pstrRecord
	 * @throws Exception
	 */
	public void BuildRecord(final StringBuffer pstrRecord) throws Exception {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::BuildRecord";
		String strVal = this.Value();
		/**
		 * eigentlich nehmen wir den Wert wie er ist.
		 * Ist allerdings ein format-string definiert, dann wird der
		 * mit diesem String formatierte Wert verwendet.
		 */
		if (strVal.length() > intSize || this.FormatString().length() > 0) {
			strVal = FormattedValue();
			if (strVal.length() > intSize && flgExceptionOnFieldTruncation == true) {
				throw new Exception("Value truncated. max " + intSize + ", actual " + this.Value().length() + ". Description:" + this.Description()
						+ ". Value = '" + this.Value() + "', " + FormattedValue());
			}
		}

		doReplace(pstrRecord, intPos, intPos + intSize, strVal);

	} // public void BuildRecord

	/**
	 *
	 * \brief doReplace
	 *
	 * \details
	 *
	 * \return StringBuffer
	 *
	 * @param pstrS
	 * @param pintPos
	 * @param pintPos2
	 * @param pstrR
	 * @return
	 */
	protected StringBuffer doReplace(final StringBuffer pstrS, final int pintPos, final int pintPos2, final String pstrR) {
		final int intlSize = pintPos2 - pintPos;
		final int intLen = pstrS.length();
		if (intLen < pintPos2) {
			pstrS.append(repeatString(" ", pintPos2 - intLen + 1));
		}
		String strT = pstrR + repeatString(" ", intlSize);
		strT = strT.substring(0, intlSize);
		pstrS.replace(pintPos, pintPos2, strT);
		return pstrS;
	}

	/**
	 * \brief setFormatPattern - Muster f�r die Formatpr�fung vorgeben
	 *
	 * \details
	 * Diese Methode setzt das Muster f�r einen regul�ren Ausdruck, der in der Methode
	 * checkFormatPattern() zur formalen Pr�fung des Feldwertes verwendet wird.
	 *
	 * @param pstrFormatPattern
	 */
	public void setFormatPattern(final String pstrFormatPattern) {
		if (pstrFormatPattern != null) {
			strFormatPattern = pstrFormatPattern;
		}
	}

	/**
	 * \brief allowEmptyValue - Leere Feldwerte erlauben
	 *
	 * \details
	 * Bestimmt, ob die Formatpr�fung auch bei leeren Felder stattfindet (pflgAllowEmptyValue = false).
	 *
	 * @param pflgAllowEmptyValue
	 */
	public void allowEmptyValue(final boolean pflgAllowEmptyValue) {
		flgAllowEmptyValue = pflgAllowEmptyValue;
	}

	/**
	 * \brief Formale Pr�fung des Feldinhaltes
	 *
	 * \details
	 * Vor Aufruf dieser Methode mu� mit setFormatPattern() ein Muster zur Feldpr�fung angegeben werden.
	 * Im Allgemeinen findet dies in der Methode doInit() der abgeleiteten Klasse statt.
	 *
	 * Das Muster gibt einen regul�ren Ausdruck vor, gegen den der Feldinhalt gepr�ft wird.
	 * Entspricht der Feldinhalt nicht dem vorgegebenen Muster wird eine FormatPatternException
	 * ausgel�st.
	 *
	 * @throws FormatPatternException
	 */
	public void checkFormatPattern() throws FormatPatternException {
		if (isNotEmpty(strFormatPattern)) { // Keine Formatpr�fung ohne Pattern
			if (flgAllowEmptyValue == false || !this.Value().trim().equals("")) {
				final Pattern objP = Pattern.compile(strFormatPattern);
				final Matcher objM = objP.matcher(this.Value());
				if (objM.find() == false) {
					throw new FormatPatternException("the value '" + this.Value() + "' does not correspond with the pattern " + strFormatPattern);
				}
			}
		}
	}

	/**
	 *
	 * \brief equals
	 *
	 * \details
	 *
	 * \return boolean
	 *
	 * @param pobjO
	 * @return
	 */
	public boolean isEqual(final JSDataElement pobjO) {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::equals";

		boolean flgC = false;
		if (pobjO != null) {
			flgC = this.Value().equalsIgnoreCase(pobjO.Value());
		}
		return flgC;
	} // public boolean  equals

	public String SQLValue() {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::SQLValue";

		String strT = this.Value();
		strT = strT.replace("'", "''");

		return "'" + strT + "'";
	} // public String SQLValue

	/**
	 * 
	 * \brief equals
	 * 
	 * \details
	 * Vergleicht den Wert dieses DatenElementes mit einem String oder mit dem Wert eines
	 * anderen DatenElements.
	 * 
	 * Ist der Parameter nicht vom Typ String oder JSDataElement, so wird ein Objektvergleich
	 * durchgef�hrt.
	 * 
	 * \return true = Identit�t, false = keine Identit�t
	 *
	 * @param pobjO (String, JSDataElement, Object)
	 */
	@Override
	public boolean equals (final Object pobjO) {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::equals";

		if (pobjO instanceof String) {
			final String strT = (String) pobjO;
			return this.Value().equals(strT);
		}

		if (pobjO instanceof JSDataElement) {
			return this.Value().equals(((JSDataElement) pobjO).Value());
		}

		return pobjO.equals(this);
	}
}
