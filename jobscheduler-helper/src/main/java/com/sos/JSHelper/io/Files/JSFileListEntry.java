package com.sos.JSHelper.io.Files;

import com.sos.JSHelper.Listener.JSListenerClass;

/*
 * ---------------------------------------------------------------------------
 * APL/Software GmbH - Berlin ##### generated by ClaviusXPress
 * (http://www.sos-berlin.com) ######### Montag, 15. Oktober 2007,
 * Klaus.Buettner@sos-berlin.com (KB)
 * --------------------------------------------
 * ----------------------------------- <docu type="smcw" version="1.0">
 * <project>com.sos.IDocs</project> <name>JSFileListEntry.java</name>
 * <title>Datenstruktur f�r Dateiverarbeitung </title> <description> <para>
 * Diese Klasse rep�sentiert eine Datenstruktur f�r die Dateiverarbeitung von
 * lokalen und remote Dateien. </para> </description> <params> </params>
 * <keywords> <keyword>Datenstruktur</keyword> <keyword>Dateiliste</keyword>
 * </keywords> <categories> <category>Datenstruktur</category> </categories>
 * <date>Montag, 15. Oktober 2007</date> <copyright>� 2000, 2001 by SOS GmbH
 * Berlin</copyright> <author>Klaus.Buettner@sos-berlin.com</author> <changes>
 * <change who='KB' when='Montag, 15. Oktober 2007' id='created'> <what> <para>
 * created </para> </what> </change> </changes> </docu>
 * ----------------------------------------------------------------------------
 */
/** <p style="text-align:center">
 * <br />
 * ---------------------------------------------------------------------------
 * APL/Software GmbH - Berlin ##### generated by ClaviusXPress
 * (http://www.sos-berlin.com) ######### Montag, 15. Oktober 2007,
 * Klaus.Buettner@sos-berlin.com (KB) <br />
 * ---------------------------------------------------------------------------
 * </p>
 * JSFileListEntry - Datenstruktur f�r Dateiverarbeitung
 * <p>
 * Diese Klasse rep�sentiert eine Datenstruktur f�r die Dateiverarbeitung von
 * lokalen und remote Dateien.
 * </p>
 * <p>
 * Verwendung findet diese Struktur beim download von Dateien, deren Name �ber
 * einen (regul�ren) Ausdruck definiert ist und dabei dann mehr als eine Datei
 * relevant ist.
 * </p>
 * 
 * @author Klaus.Buettner@sos-berlin.com
 * @version 0.9
 * @see reference
 * @exception classname description */

public class JSFileListEntry extends JSListenerClass {

    private String strRemoteFileName = null;
    private String strLocalFileName = null;
    private long lngNoOfBytesTransferred = 0;

    public JSFileListEntry() {

    }

    public JSFileListEntry(String pstrLocalFileName) {
        this("", pstrLocalFileName, 0);
    }

    public JSFileListEntry(String pstrRemoteFileName, String pstrLocalFileName, long plngNoOfBytesTransferred) {
        strRemoteFileName = pstrRemoteFileName;
        strLocalFileName = pstrLocalFileName;
        lngNoOfBytesTransferred = plngNoOfBytesTransferred;
    }

    public String toString() {
        String strT = String.format("RemoteFile = %1$s, LocalFile = %2$s, BytesTransferred = %3$s", this.RemoteFileName(), this.LocalFileName(), this.NoOfBytesTransferred());
        return strT;
    }

    public String RemoteFileName() {
        return strRemoteFileName;
    }

    public void setRemoteFileName(String pstrRemoteFileName) {
        this.strRemoteFileName = pstrRemoteFileName;
    }

    public String LocalFileName() {
        return strLocalFileName;
    }

    public void setLocalFileName(String pstrLocalFileName) {
        this.strLocalFileName = pstrLocalFileName;
    }

    public long NoOfBytesTransferred() {
        return lngNoOfBytesTransferred;
    }

    public void setNoOfBytesTransferred(long plngNoOfBytesTransferred) {
        this.lngNoOfBytesTransferred = plngNoOfBytesTransferred;
    }
}
