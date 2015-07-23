package com.sos.JSHelper.io.Files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

import sos.util.SOSGZip;

import com.sos.JSHelper.Archiver.IJSArchiver;
import com.sos.JSHelper.Archiver.JSArchiver;
import com.sos.JSHelper.Archiver.JSArchiverOptions;
import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.DataElements.JSDataElementDate;
import com.sos.JSHelper.DataElements.JSDataElementTimeStampISO;
import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.JSHelper.Listener.JSListener;
import com.sos.JSHelper.Options.SOSOptionFolderName;
import com.sos.i18n.annotation.I18NResourceBundle;
import com.sos.localization.Messages;

@I18NResourceBundle(baseName = "com_sos_JSHelper_Messages", defaultLocale = "en")
public class JSFile extends java.io.File implements JSListener, IJSArchiver {
	public static final String	conPropertySOS_JSFILE_PREFIX_4_TEMPFILE			= "sos.jsfile.prefix.4.tempfile";
	public static final String	conPropertySOS_JSFILE_EXTENSION_4_TEMPFILE		= "sos.jsfile.extension.4.tempfile";
	public static final String	conPropertySOS_JSFILE_EXTENSION_4_EXCLUSIVEFILE	= "sos.jsfile.extension.4.exclusivefile";
	private static final String	ConDefaultExtension4BackupFile					= ".bak";
	public static final String	conPropertySOS_JSFILE_EXTENSION_4_BACKUPFILE	= "sos.jsfile.extension.4.backupfile";
	private static final Logger	logger											= Logger.getLogger(JSFile.class);
	private final String		conClassName									= "JSFile";
	private static final long	serialVersionUID								= -1430552107244301112L;
	final String				JSH_E_0010										= "JSH_E_0010";													// "%1$s: File '%2$s' not exist. Append not possible";
	final String				JSH_E_0020										= "JSH_E_0020";													// "%1$s: Source file '%2$s' and file to append '%3$s' are equal. append not possible";
	final String				JSH_E_0040										= "JSH_E_0040";													// "%1$s: File '%2$s' not exist. can not append to '%3$s'";
	final String				JSH_E_0050										= "JSH_E_0050";													// "%1$s: append not succesfull for file '%2$s' with '%3$s'";
	final String				JSH_E_0060										= "JSH_E_0060";													// "%1$s: copy not succesfull for file '%2$s' to '%3$s'";
	final String				JSH_E_0070										= "JSH_E_0070";													// "Exclusive usage of file %1$s not possible. User is %$2s";
	final String				JSH_E_0090										= "JSJ_E_0090";													// delete
	final String				JSH_E_0140										= "JSH_E_0140";													// "file '%1$s' does not exist";
	final String				JSH_I_0010										= "JSH_I_0010";													// "%1$s: File '%2$s' appended to file '%3$s'";
	final String				JSH_I_0020										= "JSH_I_0020";													// "%3$s - File '%1$s' created, Size is : %2$s";
	final String				JSH_I_0030										= "JSH_I_0030";													// "%3$s - file '%1$s' closed. '%2$d' lines written.";
	final String				JSH_I_0040										= "JSH_I_0040";													// "%3$s - file '%1$s' closed. '%2$d' lines read.";
	final String				JSH_I_0060										= "JSH_I_0060";													// "File opened for write: '%1$s'";
	final String				JSH_I_0070										= "JSH_I_0070";													// "File '%1$s' opened for write with charset '%2$s'";
	final String				JSH_I_0080										= "JSH_I_0080";													// "%3$s: File '%1$s' copied to '%2$s'";
	final String				JSH_I_0090										= "JSH_I_0090";													// File
	final String				JSH_I_0100										= "JSH_I_0100";													// File
	final String				JSH_I_0110										= "JSH_I_0110";													// "create exclusive-file-marker with : %1$s";
	final String				JSH_I_0120										= "JSH_I_0120";													// "Backup for File created '%1$s'";
	final String				JSH_I_0130										= "JSH_I_0130";													// "File '%1$s' deleted before, because exist";
	String						JSH_I_0150										= "JSH_I_0150";													// "File unlocked: %1$s";
	public SOSOptionFolderName	BackupFolderName								= new SOSOptionFolderName(null, "BackupFolderName",
																						"Name of Folder for Backup of this file", "", "", false);
	protected String			strFileName;
	protected File				fleFile;
	protected FileReader		fleFileReader;
	protected BufferedReader	bufReader;
	private StringBuffer		strRecordBuffer;
	private String				strPushBackBuffer								= "";
	protected long				lngNoOfLinesRead								= 0;
	private long				lngNoOfLinesWritten								= 0;
	private long				lngNoOfCharsInBuffer							= 0;
	protected String			strCharSet4OutputFile							= null;															// spezifiziert
	protected String			strCharSet4InputFile							= null;															// spezifiziert
	private RandomAccessFile	randomFile										= null;															// Used
	private FileLock			fleLock											= null;
	private boolean				flgFileIsLocked									= false;
	private boolean				flgIsExclusive									= false;
	private JSFile				fleExclusiveFile								= null;
	private String				strExclusiveFootPrint							= "";
	private boolean				flgIsZipfile									= false;
	private Messages			Messages										= null;

	public JSFile(final String pstrFileAndPathName) {
		super(pstrFileAndPathName.replace("file:/", ""));
		strFileName = pstrFileAndPathName;
		doInit();
	}

	public JSFile(final File parent, final String child) {
		super(parent, child);
		strFileName = getAbsolutePath();
		doInit();
	}

	public JSFile(final String parent, final String child) {
		super(parent, child);
		doInit();
	}

	/**
	 *
	 * \brief JSFile
	 *
	 * \details
	 *
	 * @param pobjUri
	 * @throws MalformedURLException
	 */
	public JSFile(final URI pobjUri) throws MalformedURLException {
		super(pobjUri);
		doInit();
		strFileName = pobjUri.toURL().getFile();
	}

	/**
	 *
	 * \brief doInit - Initialisierung der Klasse
	 *
	 * \details
	 * Wird im Konstruktur explizit gerufen.
	 *
	 * \return void
	 *
	 */
	private void doInit() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::FileName";
		try {
			strFileName = getAbsolutePath();
			fleFile = this;
			int i = strFileName.indexOf("//");
			if (i > 0) {
				if (strFileName.startsWith("local:") == true) {
					strFileName = strFileName.replace("local:", "file:");
				}
				URL objURL = new URL(strFileName);
				strFileName = objURL.getFile();
				logger.info(objURL.getFile());
				logger.info(objURL.getPath());
			}
			else {
				strFileName = getAbsolutePath();
			}
			JSToolBox objT = new JSToolBox("com_sos_JSHelper_Messages");
			Messages = objT.getMessageObject();
		}
		catch (final Exception e) {
			logger.error("doInit()", e); //$NON-NLS-1$
		}
	}

	public String createZipFile(final String pstrPathName) {
		String gzipFilename = addFileSeparator(pstrPathName) + getName().concat(".gz");
		try {
			File gzipFile = new File(gzipFilename);
			SOSGZip.compressFile(this, gzipFile);
			gzipFile.setLastModified(this.lastModified());
			logger.debug(String.format("file %1$s compressed to %2$s", this.getAbsolutePath(), gzipFilename));
		}
		catch (Exception e) {
			String strT = String.format("Error during compress for file %1$s", gzipFilename);
			logger.error(strT);
			throw new JobSchedulerException(strT, e);
		}
		return gzipFilename;
	}

	public String addFileSeparator(final String str) {
		return str.endsWith("/") || str.endsWith("\\") ? str : str + "/";
	}

	public void setZipFile(final boolean pflgIsZipFile) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::setZipFile";
		flgIsZipfile = pflgIsZipFile;
	} // private void setZipFile

	/**
	 *
	 * \brief Reader - liefert eine BufferedReader-Instanz f�r die Datei
	 *
	 * \details

	 * \return BufferedReader
	 * @throws Exception
	 */
	protected BufferedReader Reader() throws IOException {
		final String conMethodName = conClassName + "::Reader";
		if (bufReader == null) {
			if (this.checkExclusiveDeny() == true) {
				throw new JobSchedulerException(Messages.getMsg(JSH_E_0070, strFileName, strExclusiveFootPrint));
			}
			if (randomFile != null) {
				fleFileReader = new FileReader(randomFile.getFD());
			}
			else {
				if (isZipFile() == true) {
					GZIPInputStream objGZ = new GZIPInputStream(new FileInputStream(this));
					if (strCharSet4InputFile != null) {
						bufReader = new BufferedReader(new InputStreamReader(objGZ, strCharSet4InputFile));
					}
					else {
						bufReader = new BufferedReader(new InputStreamReader(objGZ));
					}
				}
				else {
					fleFileReader = new FileReader(this);
					if (strCharSet4InputFile == null) {
						bufReader = new BufferedReader(fleFileReader);
					}
					else {
						bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(this), strCharSet4InputFile));
					}
				}
			}
			logger.trace(Messages.getMsg(JSH_I_0090, strFileName, strCharSet4InputFile)); // conMethodName + ": " + "File opened for read: "
		}
		return bufReader;
	}

	public boolean isZipFile() {
		boolean flgRet = false;
		if (flgIsZipfile == true || strFileName.toLowerCase().endsWith(".gz") || strFileName.toLowerCase().endsWith(".zip")) {
			flgRet = true;
		}
		return flgRet;
	}

	// - <remark who='eqbfd' when='Dienstag, 27. Oktober 2009' id='Unicode' >
	// - <oldcode>
	// - </oldcode>
	// - <newcode>
	/**
	 * \change Dienstag, 27. Oktober 2009 eqbfd Unicode
	 * Unicode lesen k�nnen
	 */
	/**
	 *
	 * \brief CharSet4InputFile
	 *
	 * \details
	 * If an IDoc-File is written using Unicode, this Option must be set to "UTF-8" to ensure, that the content of the file
	 * is properly processed, e.g. that unicode-character-sequences are recognized.
	 *
	 * If the Root-Segment is marked with EDI_DC40_U instead of EDI_DC40, the IDoc is written by using Unicode.
	 *
	 * \return void
	 *
	 * @param pstrCharSet4InputFile
	 */
	public void CharSet4InputFile(final String pstrCharSet4InputFile) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::CharSet4InputFile";
		strCharSet4InputFile = pstrCharSet4InputFile;
		// return void;
	} // public void CharSet4InputFile

	/**
	 *
	 * \brief CharSet4InputFile - Define the character set for the file
	 *
	 * \details
	 *
	 * \return String
	 *
	 * @return
	 */
	public String CharSet4InputFile() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::CharSet4InputFile";
		return strCharSet4InputFile;
	} // public String CharSet4InputFile

	// - </newcode>
	// - </remark> <!-- id=<Unicode> -->
	/**
	 * \brief CopyTimeStamp - Copy the File with a new filename extended by a time-stamp
	 *
	 * \details
	 * Kopiert die Datei mit einem um einen Zeitstempel erweiterten Dateinamen.
	 *
	 * \return String - der Dateiname mit dem Zeitstempel
	 *
	 * \see {@link #getTimeStamp()}
	 */
	public String CopyTimeStamp() throws Exception {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::CopyTimeStamp";
		try {
			String strNewFileName = null;
			final String strTimeStamp = getTimeStamp();
			final int i = strFileName.lastIndexOf(".");
			if (i > 0) {
				strNewFileName = strFileName.substring(0, i) + "-" + strTimeStamp + strFileName.substring(i);
			}
			else {
				strNewFileName = strFileName + "-" + strTimeStamp;
			}
			copy(strNewFileName);
			return strNewFileName;
		}
		catch (final Exception e) {
			throw new JobSchedulerException("CopyTimeStamp Error", e);
		}
	} // public void CopyTimeStamp ()

	/**
	 *
	 * \brief CreateBackup
	 *
	 * \details
	 *
	 * \return String
	 *
	 * @throws Exception
	 */
	public String CreateBackup() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::CreateBackup";
		// TODO Extension �ber eine Option sos.jsfile.extension.4.backupfile
		String strExtension4BackupFile = System.getProperty(conPropertySOS_JSFILE_EXTENSION_4_BACKUPFILE, ConDefaultExtension4BackupFile);
		String strR = this.doCreateBackUp(strExtension4BackupFile);
		return strR;
	} // private String CreateBackup

	/**
	 *
	 * \brief CreateBackup
	 *
	 * \details
	 *
	 * \return String
	 *
	 * @param pstrExtension4BackupFile
	 * @throws Exception
	 */
	public String CreateBackup(final String pstrExtension4BackupFile) throws Exception {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::CreateBackup";
		return this.doCreateBackUp(pstrExtension4BackupFile);
	} // private String CreateBackup

	private String doCreateBackUp(final String pstrExtension4BackupFileName) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::doCreateBackUp";
		String strNewFileName = "";
		String strBackupFolderName = BackupFolderName.Value();
		if (strBackupFolderName.length() > 0) {
			strNewFileName = strBackupFolderName + this.getName() + pstrExtension4BackupFileName;
		}
		else {
			strNewFileName = strFileName + pstrExtension4BackupFileName;
		}
		try {
			copy(strNewFileName);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			throw new JobSchedulerException("problems with createbackup", e);
		}
		logger.debug(Messages.getMsg(JSH_I_0120, strNewFileName));
		return strNewFileName;
	} // private String doCreateBackUp

	/**
	 *
	 * \brief getTimeStamp - liefert einen Zeitstempel in der Form YYYY-MM-DD-HH-II-SS
	 *
	 * \details

	 * \return String
	 *
	 * @return
	 */
	public String getTimeStamp() {
		// DateFormat dteF = DateFormat.getDateTimeInstance();
		final Date now = new Date();
		String strT = null;
		// strT = dteF.format(now).toString();
		// strT = strT.replace(":", "-");
		// strT = strT.replace(".", "-");
		// strT = strT.replace(" ", "-");
		// strT = strT.substring(6,10) + strT.substring(2,5) + '-' + strT.substring(0,2) + strT.substring(10);
		SimpleDateFormat formatter;
		final Locale currentLocale = new Locale("de", "DE");
		formatter = new SimpleDateFormat("yyyy-MM-dd-H-mm-ss", currentLocale);
		strT = formatter.format(now);
		return strT;
	}

	public boolean isOlderThan(final long plngCompareTo) {
		boolean flgR = false;
		long interval = System.currentTimeMillis() - lastModified();
		if (interval > plngCompareTo) {
			flgR = true;
		}
		return flgR;
	}

	@Override
	public boolean delete() {
		boolean flgR = true;
		try {
			if (exists()) {
				if (canWrite()) {
					flgR = super.delete();
					if (!flgR) {
						logger.error(String.format("file NOT '%1$s' deleted, due to false returncode", getAbsolutePath()));
					}
					else {
						logger.debug(String.format("file '%1$s' deleted", getAbsolutePath()));
					}
				}
				else {
					logger.error(String.format("file NOT '%1$s' deleted, because its not writable", getAbsolutePath()));
				}
			}
			else {
				logger.debug(String.format("file '%1$s' does not exists. deletion failed.", getAbsolutePath()));
//				throw new JobSchedulerException("delete failed");
			}
		}
		catch (Exception e) {
			String strT = String.format("error on delete file '%1$s'", getAbsolutePath());
			logger.error(strT);
			throw new JobSchedulerException(strT, e);
		}
		return flgR;
	}

	/**
	 * \brief move - move/rename actual file to new file
	 *
	 * \details
	 */
	public JSFile move(final String pstrNewFileName) throws Exception {
		final JSFile RetVal = null;
		try {
			final File fleNewFile = new File(pstrNewFileName);
			if (fleNewFile.exists()) {
				// \TODO: SignalError if no overwrite is specified and the new file already exist
				logger.debug(Messages.getMsg(JSH_I_0130, fleNewFile.getAbsoluteFile()));
				fleNewFile.delete();
			}
			copy(pstrNewFileName);
			try {
				delete();
				logger.info(Messages.getMsg(JSH_I_0090, getAbsoluteFile()));
			}
			catch (final Exception e) {
				// TODO kopierte Datei wieder l�schen, weil sonst doppelt
				logger.error(Messages.getMsg(JSH_E_0090, getName()));
				throw e;
			}
		}
		catch (final Exception e) {
			throw e;
		}
		// message (String.format("%1$s: File '%2$s' moved to '%3$s'.", conMethodName, this.getAbsoluteFile(), pstrNewFileName));
		return RetVal;
	} // public JSFile move ()

	/**
	 *
	 * \brief RenameTimeStamp
	 *
	 * \details
	 *
	 * \return void
	 *
	 */
	public void RenameTimeStamp() {
		// \todo coding erstellen
	}

	/**
	 *
	 * \brief MustExist - l�st eine Exception aus, wenn die Datei nicht existiert.
	 *
	 * \details

	 * \return void
	 *
	 * @throws Exception
	 */
	public void MustExist() throws Exception {
		final String conMethodName = conClassName + "::MustExist";
		if (!exists()) {
			final String s = Messages.getMsg(JSH_E_0140, strFileName);
			logger.debug(s);
			throw new Exception(s);
		}
	}
	private FileWriter			fleFileWriter	= null;
	protected BufferedWriter	bufWriter		= null;
	protected boolean			flgIsAppendMode	= false;

	public boolean getAppendMode() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getAppendMode";
		return flgIsAppendMode;
		//	return boolean;
	} // private boolean getAppendMode

	public JSFile setAppendMode(final boolean pflgIsAppendMode) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::setAppendMode";
		flgIsAppendMode = pflgIsAppendMode;
		return this;
	} // private JSFile setAppendMode

	/**
	 *
	 * \brief Writer - liefert eine BufferedWriter-Instanz f�r die Datei
	 *
	 * \details

	 * \return BufferedWriter
	 *
	 * @return
	 * @throws IOException
	 */
	protected BufferedWriter Writer() throws IOException {
		final String conMethodName = conClassName + "::Writer";
		if (bufWriter == null) {
			if (this.checkExclusiveDeny() == true) {
				throw new IOException(String.format(JSH_E_0070, strFileName, strExclusiveFootPrint));
			}
			fleFileWriter = new FileWriter(this, flgIsAppendMode);
			// - <remark who='eqbfd' when='Dienstag, 27. Oktober 2009' id='Unicode' >
			// - <oldcode>
			// bufWriter = new BufferedWriter(fleFileWriter);
			// - </oldcode>
			// - <newcode>
			if (strCharSet4OutputFile == null) {
				if (isZipFile() == true) {
					GZIPOutputStream objGZ = new GZIPOutputStream(new FileOutputStream(this));
					if (strCharSet4OutputFile != null) {
						bufWriter = new BufferedWriter(new OutputStreamWriter(objGZ, strCharSet4OutputFile));
					}
					else {
						bufWriter = new BufferedWriter(new OutputStreamWriter(objGZ));
					}
				}
				else {
					bufWriter = new BufferedWriter(fleFileWriter);
					logger.trace(Messages.getMsg(JSH_I_0060, strFileName));
				}
			}
			else {
				bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this), strCharSet4OutputFile));
				logger.trace(Messages.getMsg(JSH_I_0070, strFileName, strCharSet4InputFile));
			}
			// - </newcode>
			// - </remark> <!-- id=<Unicode> -->
			/**
			 * \change Dienstag, 27. Oktober 2009 eqbfd Unicode
			 * Unicode lesen k�nnen
			 */
		}
		return bufWriter;
	}

	public boolean isExclusive() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::isExclusive";
		return flgIsExclusive;
	} // private boolean isExclusive

	/**
	 *
	 * \brief NewLine - LineBuffer flush
	 *
	 * \details

	 * \return JSFile
	 *
	 * @return
	 * @throws Exception
	 */
	public JSFile NewLine() throws Exception {
		WriteLine("");
		lngNoOfCharsInBuffer = 0;
		return this;
	}

	/**
	 *
	 * \brief NoOfCharsInBuffer - Size of LineBuffer
	 *
	 * \details

	 * \return long - Number of Chars in Line-Buffer
	 *
	 */
	public long NoOfCharsInBuffer() {
		return lngNoOfCharsInBuffer;
	}

	/**
	 *
	 * \brief OutChar - put out a single character to the file
	 *
	 * \details
	 * Without line end, use it as a stream-output buffer.
	 *
	 * \return JSFile
	 *
	 * @param pchrC
	 * @return
	 * @throws IOException
	 */
	public JSFile OutChar(final char pchrC) throws IOException {
		bufWriter.write(pchrC);
		lngNoOfCharsInBuffer++;
		return this;
	}

	/**
	 *
	 * \brief OutString
	 *
	 * \details

	 * \return void
	 *
	 * @param pstrLine
	 * @throws Exception
	 */
	public void OutString(final String pstrLine) throws Exception {
		if (bufWriter == null) {
			Writer();
		}
		bufWriter.write(pstrLine);
		lngNoOfCharsInBuffer += pstrLine.length();
	}

	/**
	 *
	 * \brief Write - see WriteLine
	 *
	 * \details
	 * Wrapper for the WriteLine Method
	 * \return void
	 *
	 * @param pstrLine
	 * @throws Exception
	 */
	public void Write(final String pstrLine) throws Exception {
		WriteLine(pstrLine);
	}

	/**
	 *
	 * \brief WriteLine
	 *
	 * \details
	 * \code
	private void TestWriteLine() throws IOException {
		JSTextFile objF = new JSTextFile("C:/temp/HalloWelt.txt");
		for (int i = 1; i <= 10; i++) {
			objF.WriteLine("Hallo, Welt ...");
		}
		objF.close();

	}
	 * \endcode
	 * \return void
	 *
	 * @param pstrLine
	 * @throws IOException
	 */
	public JSFile WriteLine(final String pstrLine) throws IOException {
		if (bufWriter == null) {
			Writer();
		}
		String strBuff = pstrLine + System.getProperty("line.separator");
		if (randomFile == null) {
			bufWriter.write(strBuff);
			bufWriter.flush();
		}
		else {
			// randomFile.seek(randomFile.length());
			// randomFile.writeUTF(pstrLine + System.getProperty("line.separator"));
			randomFile.writeBytes(strBuff);
		}
		lngNoOfLinesWritten++;
		return this;
	}

	/**
	 *
	 * \brief WirteLine
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @throws IOException
	 * @throws Exception
	 */
	// - <remark who='EQALS' when='Donnerstag, 17. September 2009' id='Fehlerkorrektur' >
	/**
	 * \change Donnerstag, 17. September 2009 EQALS Fehlerkorrektur
	 * Fehlerkorrektur
	 */
	// - <oldcode>
	// public void WriteLine() throws IOException {
	// - </oldcode>
	// - <newcode>
	public void WriteLine() throws IOException, Exception {
		// - </newcode>
		// - </remark> <!-- id=<Fehlerkorrektur> -->
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::WriteLine";
		WriteLine("");
	} // public void WriteLine

	/**
	 *
	 * \brief GetLine - get a single line from a file
	 *
	 * \details
	 * This Method is using the PushBackBuffer if needed.
	 *
	 * \return StringBuffer - the content of the line
	 *
	 * @return
	 */
	public StringBuffer GetLine() {
		String strT;
		StringBuffer strSB = new StringBuffer("");
		try {
			if (strPushBackBuffer.length() > 0) {
				strSB.append(strPushBackBuffer);
				strPushBackBuffer = "";
				strRecordBuffer = strSB;
			}
			else {
				// System.out.println ("Try to read a record...");
				if (bufReader == null) {
					Reader();
				}
				strT = bufReader.readLine();
				if (strT != null) {
					lngNoOfLinesRead++;
					strSB = new StringBuffer(strT);
					strRecordBuffer = strSB;
					// System.out.println(strT);
				}
				else {
					bufReader.close();
					bufReader = null;
					strSB = null;
				}
			}
		}
		catch (final Exception e) {
			logger.error("GetLine()", e); //$NON-NLS-1$
			strSB = null;
		}
		return strSB;
	}

	public void PushBack() {
		strPushBackBuffer = strRecordBuffer.toString();
	}

	public void copy(final JSFile pobjTargetFile) {
		try {
			this.copy(pobjTargetFile.getAbsolutePath());
		}
		catch (JobSchedulerException e) {
			throw e;
		}
		catch (Exception e) {
			throw new JobSchedulerException(e);
		}
	}

	/**
	 *
	 * \brief copy - FileCopy actual to new file
	 *
	 * \details

	 * \return void
	 *
	 * @param pstrTargetFileName
	 * @throws Exception
	 */
	public void copy(final String pstrTargetFileName) throws Exception {
		final String conMethodName = conClassName + "::copy";
		final File infile = new File(strFileName);
		if (!infile.getAbsoluteFile().exists()) {
			SignalError(String.format("%1$s: File '%2$s' not exist. copy not possible", conMethodName, strFileName));
			return;
		}
		final File outfile = new File(pstrTargetFileName);
		if (infile.getCanonicalPath().equals(outfile.getCanonicalPath())) {
			message(String.format("%3$s: File '%1$s' is equal to '%2$s'. copy is useless", strFileName, pstrTargetFileName, conMethodName));
			return;
		}
		try {
			final FileOutputStream out = new FileOutputStream(outfile);
			try {
				final FileInputStream in = new FileInputStream(infile);
				ExecuteCopy(in, out);
			}
			catch (final FileNotFoundException e) {
				SignalError(String.format("%1$s: File '%2$s' not exist. can not copy to '%3$s'", conMethodName, strFileName, pstrTargetFileName));
			}
		}
		catch (final Exception e) {
			e.printStackTrace(System.err);
			SignalError(Messages.getMsg(JSH_E_0060, conMethodName, getName(), pstrTargetFileName));
		}
		finally {
			//
		}
		logger.debug(Messages.getMsg(JSH_I_0080, strFileName, pstrTargetFileName, conMethodName));
	}// public void copy(String pstrTargetFileName)

	/**
	 *
	 * \brief ReplaceWith - Textersetzungen in der Datei durchf�hren
	 *
	 * \details

	\code
	private void replaceText () throws Exception {

		String strFileName = "data/sample4write.csv";
		JSCsvFile objCsvFile = new JSCsvFile(strFileName);
		objCsvFile.dumpAscii(System.out);

		objCsvFile.ReplaceWith("dadis.schering.de", "dadis.eu.schering.net");
		objCsvFile.dumpAscii(System.out);
	}
	\endcode
	 * \return void
	 *
	 * @param strReplaceWhatAsRegEx
	 * @param strReplaceWith
	 * @throws Exception
	 */
	public void ReplaceWith(final String strReplaceWhatAsRegEx, final String strReplaceWith) throws Exception {
		final String conMethodName = conClassName + "::ReplaceWith";
		final File infile = fleFile;
		if (!infile.exists()) {
			SignalError(String.format("%1$s: File '%2$s' not exist. nothing to do", conMethodName, strFileName));
			return;
		}
		final File fleTempfile = java.io.File.createTempFile("JSFile", ".tmp");
		try {
			FileInputStream in = new FileInputStream(infile);
			FileOutputStream out = new FileOutputStream(fleTempfile);
			ExecuteReplace(in, out, strReplaceWhatAsRegEx, strReplaceWith);
			// \todo Datei erst umbenennen (mit Tilde) und erst nach dem Kopieren umbenennen
			fleFile.delete();
			in = new FileInputStream(fleTempfile);
			out = new FileOutputStream(infile);
			ExecuteCopy(in, out);
			fleTempfile.delete();
		}
		catch (final FileNotFoundException e) {
			SignalError(String.format("%1$s: File '%2$s' not exist. nothing to do", conMethodName, strFileName));
		}
		catch (final Exception e) {
			SignalError(String.format("%1$s: replace not succesfull for file '%2$s' ", conMethodName, getName()));
		}
		message(String.format("%1$s: Replacing in File '%2$s' done ", conMethodName, strFileName));
	} // public void ReplaceWith(String strReplaceWhatAsRegEx, String strReplaceWith)

	private void ExecuteReplace(final FileInputStream in, final FileOutputStream out, final String strReplaceWhatAsRegEx, final String strReplaceWith)
			throws IOException {
		final int intBuffsize = 4096;
		synchronized (in) {
			synchronized (out) {
				byte[] buffer = new byte[intBuffsize];
				while (true) {
					final int bytesRead = in.read(buffer);
					if (bytesRead == -1) {
						break;
					}
					if (bytesRead < intBuffsize) {
						byte[] tmpB = new byte[bytesRead];
						tmpB = buffer;
						buffer = new byte[bytesRead];
						for (int i = 0; i < bytesRead; i++) {
							buffer[i] = tmpB[i];
						}
					}
					String strLine = new String(buffer);
					strLine = strLine.replaceAll(strReplaceWhatAsRegEx, strReplaceWith);
					byte[] outB = new byte[intBuffsize + strReplaceWith.length()];
					outB = strLine.getBytes();
					out.write(outB, 0, strLine.length());
				} // while (true)
			}
		}
		in.close();
		out.close();
	}

	private long in2out(final FileInputStream in, final FileOutputStream out) throws IOException {
		long lngBytesCopies = 0;
		synchronized (in) {
			synchronized (out) {
				final byte[] buffer = new byte[256];
				while (true) {
					final int bytesRead = in.read(buffer);
					if (bytesRead == -1) {
						break;
					}
					out.write(buffer, 0, bytesRead);
					lngBytesCopies += bytesRead;
				}
			}
		}
		return lngBytesCopies;
	}

	private long ExecuteCopyWOClose(final FileInputStream in, final FileOutputStream out) throws IOException {
		return in2out(in, out);
	}

	private long ExecuteCopy(final FileInputStream in, final FileOutputStream out) throws IOException {
		final long lngBytesCopied = in2out(in, out);
		in.close();
		out.close();
		return lngBytesCopied;
	}

	/**
	 *
	 * \brief ExecuteCopy
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	private void ExecuteCopy(final FileInputStream in, final PrintStream out) throws IOException {
		synchronized (in) {
			synchronized (out) {
				final byte[] buffer = new byte[1024];
				while (true) {
					final int bytesRead = in.read(buffer);
					if (bytesRead == -1) {
						break;
					}
					out.write(buffer, 0, bytesRead);
				}
			}
		}
		in.close();
		// out.close();
	}

	/**
	 *
	 * \brief File2String
	 *
	 * \details
	 *
	 * \return String
	 *
	 * @return
	 */
	public String File2String() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::File2String";
		final String strT = getContent();
		return strT;
	} // public String File2String}

	/**
	 *
	 * \brief getContent
	 *
	 * \details
	 *
	 * \return String
	 *
	 * @return
	 */
	public String getContent() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getContent ";
		String strB = "";
		BufferedReader fin;
		try {
			fin = this.Reader();
		}
		catch (Exception e1) {
			throw new JobSchedulerException("Could not get content from file " + this.getAbsolutePath(), e1);
		}
		// FileInputStream fin = null;
		int lngFileSize = 0;
		try {
			// if (randomFile == null) {
			// fin = new FileInputStream(strFileName);
			// }
			// else {
			// fin = new FileInputStream(randomFile.getFD());
			// }
			// TODO Buffsize as a parameter
			final char[] buffer = new char[4096];
			while (true) {
				final int bytesRead = fin.read(buffer);
				if (bytesRead == -1) {
					break;
				}
				lngFileSize += bytesRead;
				strB = strB + new String(buffer);
			}
		}
		catch (final IOException e) {
			logger.error("getContent() - " + e, e); //$NON-NLS-1$
			throw new JobSchedulerException("Error in JSFile", e);
		}
		finally {
			try {
				if (fin != null) {
					fin.close();
					fin = null;
				}
			}
			catch (final IOException e) {
				fin = null;
			}
		}
		// final int i = strB.indexOf('\0');
		// if (i > 0) {
		final String strT = strB.substring(0, lngFileSize);
		// strB = strT;
		// }
		return strT;
	} // public String getContent

	/**
	 *
	 * \brief dumpAscii - Dateiinhalt in ascii (Text) auf PrintStream kopieren
	 *
	 * \details

	 * \return void
	 *
	 * @param out
	 */
	public void dumpAscii(final PrintStream out) {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(strFileName);
			ExecuteCopy(fin, out);
		}
		catch (final IOException e) {
			logger.error("dumpAscii(PrintStream) - " + e, e); //$NON-NLS-1$
		}
		finally {
			try {
				if (fin != null) {
					fin.close();
				}
			}
			catch (final IOException e) {
			}
		}
	}

	/**
	 *
	 * \brief dumpAscii - Dateiinhalt in ascii (Text) auf FileOutputStream kopieren
	 *
	 * \details

	 * \return void
	 *
	 * @param out
	 */
	public void dumpAscii(final FileOutputStream out) {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(strFileName);
			ExecuteCopy(fin, out);
		}
		catch (final IOException e) {
			logger.error("dumpAscii(FileOutputStream) - " + e, e); //$NON-NLS-1$
		}
		finally {
			try {
				if (fin != null) {
					fin.close();
				}
			}
			catch (final IOException e) {
			}
		}
	}
	private final char[]	charArray	= { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 *
	 * \brief byte2String - bytes in String umsetzen
	 *
	 * \details

	 * \return String
	 *
	 * @param content
	 * @return
	 */
	public String byte2String(final byte[] content) {
		final StringBuilder buffer = new StringBuilder(content.length * 2);
		for (final byte element : content) {
			buffer.append(charArray[(0xF0 & element) >> 4]);
			buffer.append(charArray[0xF & element]);
		}
		return buffer.toString();
	}

	/**
	 *
	 * \brief dumpDecimal
	 *
	 * \details

	 * \return void
	 *
	 * @param filename
	 */
	public void dumpDecimal(final String filename) {
		FileInputStream fin = null;
		final byte[] buffer = new byte[16];
		boolean end = false;
		int bytesRead;
		try {
			fin = new FileInputStream(filename);
			while (!end) {
				bytesRead = 0;
				while (bytesRead < buffer.length) {
					final int r = fin.read(buffer, bytesRead, buffer.length - bytesRead);
					if (r == -1) {
						end = true;
						break;
					}
					bytesRead += r;
				}
				for (int i = 0; i < bytesRead; i++) {
					int dec = buffer[i];
					if (dec < 0) {
						dec = 256 + dec;
					}
					if (dec < 10) {
						logger.debug("dumpDecimal(String) - 00" + dec + " "); //$NON-NLS-1$ //$NON-NLS-2$
					}
					else
						if (dec < 100) {
							logger.debug("dumpDecimal(String) - 0" + dec + " "); //$NON-NLS-1$ //$NON-NLS-2$
						}
						else {
							logger.debug("dumpDecimal(String) - " + dec + " "); //$NON-NLS-1$ //$NON-NLS-2$
						}
				}
				logger.debug("dumpDecimal(String)"); //$NON-NLS-1$
			}
		}
		catch (final IOException e) {
			logger.error("dumpDecimal(String) - " + e, e); //$NON-NLS-1$
		}
		finally {
			try {
				if (fin != null) {
					fin.close();
				}
			}
			catch (final IOException e) {
			}
		}
	}

	/**
	 *
	 * \brief dumpHex - Dateiinhalt in hexadezial auf PrintStream ausgeben
	 *
	 * \details

	 * \return void
	 *
	 * @param out
	 */
	public void dumpHex(final PrintStream out) {
		BufferedReader fin = null;
		final char[] buffer = new char[24];
		boolean end = false;
		int bytesRead;
		try {
			fin = Reader();
			while (!end) {
				bytesRead = 0;
				while (bytesRead < buffer.length) {
					final int r = fin.read(buffer, bytesRead, buffer.length - bytesRead);
					if (r == -1) {
						end = true;
						break;
					}
					bytesRead += r;
				}
				for (int i = 0; i < bytesRead; i++) {
					int hex = buffer[i];
					if (hex < 0) {
						hex = 256 + hex;
					}
					if (hex >= 16) {
						out.print(Integer.toHexString(hex) + " ");
					}
					else {
						out.print("0" + Integer.toHexString(hex) + " ");
					}
				}
				out.println();
			}
		}
		catch (final IOException e) {
			logger.error("dumpHex(PrintStream) - " + e, e); //$NON-NLS-1$
		}
		finally {
			try {
				if (fin != null) {
					fin.close();
					fin = null;
				}
			}
			catch (final IOException e) {
			}
		}
	}

	/**
	 *
	 * \brief message
	 *
	 * \details
	 *
	 * \return
	 *
	 * @param pstrMsg
	 */
	@Override
	public void message(final String pstrMsg) {
		if (JSListener != null) {
			JSListener.message(pstrMsg);
		}
		else {
			logger.debug("message(String) - " + pstrMsg); //$NON-NLS-1$
		}
	}
	private JSListener	JSListener;

	/**
	 *
	 * \brief registerMessageListener
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param l
	 */
	@Deprecated
	public void registerMessageListener(final JSListener l) {
		JSListener = l;
	}

	/**
	 *
	 * \brief SignalAbort
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param strS
	 * @throws Exception
	 */
	public void SignalAbort(final String strS) {
		String strT = " ###ProgramAbort### ";
		strT = strT + strS + strS;
		logger.fatal(strT);
		throw new JobSchedulerException(strT);
	}

	/**
	 *
	 * \brief SignalInfo
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param strS
	 * @throws Exception
	 */
	public void SignalInfo(final String strS) {
		logger.info(strS);
	}

	/**
	 *
	 * \brief SignalError
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param strS
	 * @throws Exception
	 */
	public void SignalError(final String strS) {
		String strT = " ### Error ### ";
		strT = strT + strS + strS;
		logger.error(strT);
		throw new JobSchedulerException(strT);
	}
	private JSArchiver	objArchiver	= null;

	/**
	 *
	 * \brief getArchiver - liefert eine aktuelle Instanz der Archiver-Klasse
	 *
	 * \details
	 * liefert eine aktuelle Instanz der Archiver-Klasse.
	 * Falls keine existiert wird eine gestartet.
	 *
	 * \return
	 *
	 * @throws Exception
	 */
	@Override
	public JSArchiver getArchiver() throws Exception {
		if (objArchiver == null) {
			objArchiver = new JSArchiver();
			objArchiver.registerMessageListener(this);
			objArchiver.Options().FileName(super.getAbsoluteFile().toString());
		}
		return objArchiver;
	}

	/**
	 *
	 * \brief getArchiver - liefert eine aktuelle Instanz der Archiver-Klasse
	 *
	 * \details
	 * liefert eine aktuelle Instanz der Archiver-Klasse.
	 * Falls keine existiert wird eine gestartet.
	 *
	 * \return
	 *
	 * @throws Exception
	 */
	public JSArchiver getArchiver(final JSArchiverOptions pobjArchiverOptions) throws Exception {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getArchiver";
		if (objArchiver == null) {
			objArchiver = new JSArchiver();
			objArchiver.registerMessageListener(this);
			pobjArchiverOptions.FileName(super.getAbsoluteFile().toString());
			objArchiver.Options(pobjArchiverOptions);
			// objArchiver.Options().FileName(super.getAbsoluteFile().toString());
		}
		return objArchiver;
	} // public JSArchiver getArchiver}

	/**
	 *
	 * \brief close
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @throws IOException
	 */
	public void close() throws IOException {
		final String conMethodName = conClassName + "::close";
		if (bufWriter != null) {
			bufWriter.flush();
			bufWriter.close();
			bufWriter = null;
			final NumberFormat fmt = NumberFormat.getInstance();
			fmt.setGroupingUsed(true);
			final String strT = Messages.getMsg(JSH_I_0020, fleFile.getAbsoluteFile(), fmt.format(fleFile.length()), conMethodName);
			logger.debug(strT);
			logger.debug(Messages.getMsg(JSH_I_0030, strFileName, lngNoOfLinesWritten, conMethodName));
		}
		else {
			if (bufReader != null) {
				bufReader.close();
				bufReader = null;
				logger.debug(Messages.getMsg(JSH_I_0040, strFileName, lngNoOfLinesRead, conMethodName));
			}
		}
		this.doUnlock();
		this.doReleaseExclusive();
	}

	/**
	 * \brief createTempFileName
	 *
	 * \details
	 *
	 * \return String
	 *
	 */
	public static String createTempFileName() {
		String strTempFileNameExtension = System.getProperty(conPropertySOS_JSFILE_EXTENSION_4_TEMPFILE, ".tmp");
		String strTempFileNamePrefix = System.getProperty(conPropertySOS_JSFILE_PREFIX_4_TEMPFILE, "SOS_");
		String strTempFileName = null;
		try {
			File objF = File.createTempFile(strTempFileNamePrefix, strTempFileNameExtension);
			objF.deleteOnExit();
			strTempFileName = objF.getAbsoluteFile().getAbsolutePath();
			strTempFileName = strTempFileName.replaceAll("\\\\", "/");
		}
		catch (IOException e) {
			//			e.printStackTrace();
		}
		return strTempFileName;
	}

	/**
	 * \brief createTempFile
	 *
	 * \details
	 *
	 * \return JSFile
	 *
	 */
	public static JSFile createTempFile() {
		String strTempFileName = createTempFileName();
		JSFile tempFile = new JSFile(strTempFileName);
		tempFile.deleteOnExit();
		return tempFile;
	}

	/**
	 *
	 * \brief AppendFile - H�ngt den Inhalt einer anzugebenden Datei an die aktuelle Datei
	 *
	 * \return long
	 *
	 * @param pstrFileName - Name der Datei, deren Inhalt angeh�ngt werden soll
	 *
	 * @throws Exception
	 */
	public long AppendFile(final String pstrFileName) throws Exception {
		final String conMethodName = conClassName + "::AppendFile";
		long lngBytesWritten = 0;
		FileInputStream in = null;
		FileOutputStream out = null;
		final String strFileN = getAbsolutePath();
		final File objFile2Append = new File(strFileN);
		if (!objFile2Append.getAbsoluteFile().exists()) {
			SignalError(Messages.getMsg(JSH_E_0010, conMethodName, strFileN));
			return -1;
		}
		JSFile tempFile = createTempFile(); // Ausgabe
		try {
			out = new FileOutputStream(tempFile);
			try {
				final File AFile = new File(pstrFileName); // the content of this file has to be appended
				if (!AFile.getAbsoluteFile().exists()) {
					SignalError(Messages.getMsg(JSH_E_0010, conMethodName, pstrFileName));
					tempFile.delete();
					return -1;
				}
				if (AFile.getAbsoluteFile().getAbsolutePath().equalsIgnoreCase(strFileN) == true) {
					SignalError(Messages.getMsg(JSH_E_0020, conMethodName, strFileN, pstrFileName));
					tempFile.delete();
					return -1;
				}
				in = new FileInputStream(objFile2Append); //
				lngBytesWritten += ExecuteCopyWOClose(in, out);
				in.close();
				in = new FileInputStream(AFile);
				lngBytesWritten += ExecuteCopyWOClose(in, out);
				in.close();
				out.close();
				message(Messages.getMsg(JSH_I_0010, conMethodName, pstrFileName, strFileN));
				objFile2Append.delete(); // otherwise the rename leads into an error
				tempFile.renameTo(objFile2Append);
				// tempFile.move(objFile2Append.getAbsolutePath());
				tempFile = null;
				return lngBytesWritten;
			}
			catch (final FileNotFoundException e) {
				SignalError(Messages.getMsg(JSH_E_0040, conMethodName, pstrFileName, strFileN));
			}
			catch (final Exception e) {
				if (in != null) {
					in.close();
				}
				// if (out != null)
				out.close();
				throw e;
			}
		}
		catch (final Exception e) {
			logger.error("AppendFile(String)", e); //$NON-NLS-1$
			SignalError(Messages.getMsg(JSH_E_0050, conMethodName, getName(), pstrFileName));
		}
		finally {
			if (tempFile != null) {
				tempFile.delete();
				tempFile = null;
			}
			//
		}
		return lngBytesWritten;
		// try {
		// String strFileN = this.getAbsolutePath();
		// File tempFile = new File(strFileN + "~");
		// writer = new BufferedWriter(new FileWriter(tempFile));
		//
		// File objFile2Append = new File(strFileN);
		// reader = new BufferedReader(new FileReader(objFile2Append));
		//
		// while ((line = reader.readLine()) != null) {
		// writer.write (line);
		// writer.newLine();
		// }
		// reader.close();
		//
		// File AFile = new File(pstrFileName); // the content of this file has to be appended
		// reader = new BufferedReader(new FileReader(AFile));
		//
		// while ((line = reader.readLine()) != null) {
		// writer.write(line);
		// writer.newLine();
		// }
		//
		// reader.close();
		//
		// writer.close();
		//
		// objFile2Append.delete(); // otherwise the rename leads into an error
		// tempFile.renameTo(objFile2Append);
		// }
		// catch (Exception e) {
		// throw e;
		// //
		// }
		// finally {
		// if (writer != null) {
		// writer.close();
		// }
		// if (reader != null) {
		// reader.close();
		// }
		// }
		//
		// return lngBytesWritten;
	}

	/**
	 *
	 * \brief Send2FtpServer
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param settings
	 * @throws Exception
	 */
	public JSFile Send2FtpServer(final HashMap<String, String> settings) throws Exception {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Send2FtpServer";
		// final JSFtp objFtpClient = new JSFtp();
		// final JSFtpOptions objFtpOptions = objFtpClient.Options(settings);
		// objFtpOptions.setAllOptions(settings);
		//
		// objFtpOptions.FileMask("^" + strFileName + "$");
		// objFtpClient.EstablishConnectionToServer();
		// objFtpClient.putFiles();
		return this;
	} // public void Send2FtpServer

	/**
	 *
	 * \brief Send2FtpServer
	 *
	 * \details
	 *
	 * \return JSFile
	 *
	 * @param pobjFtpOptions
	 * @return
	 * @throws Exception
	 */
	// public JSFile Send2FtpServer(final JSFtpOptions pobjFtpOptions) throws Exception {
	//
	// @SuppressWarnings("unused")
	// final String conMethodName = conClassName + "::Send2FtpServer";
	//
	// // final JSFtp objFtpClient = new JSFtp();
	// // objFtpClient.Options(pobjFtpOptions);
	// //
	// // pobjFtpOptions.FileMask("^" + strFileName + "$");
	// // objFtpClient.EstablishConnectionToServer();
	// // objFtpClient.putFiles();
	//
	// return this;
	// } // public this Send2FtpServer}
	/**
	 *
	 * \brief match
	 *
	 * \details
	 * Vergleicht den Inhalt der Datei mit der im Parameter angegebenen Datei.
	 * Beide Dateien werden Zeichen f�r Zeichen verglichen
	 *
	 * \return boolean
	 *
	 * @param fleTest
	 * @return
	 * @throws Exception
	 */
	public boolean compare(final JSFile pfleFile) throws Exception {
		final BufferedReader objReader1 = Reader();
		final BufferedReader objReader2 = pfleFile.Reader();
		int char1, char2;
		if (pfleFile.length() != length()) {
			return false;
		}
		while (true) {
			char1 = objReader1.read();
			char2 = objReader2.read();
			if (char1 != char2) {
				return false;
			}
			if (char1 == -1 && char2 == -1) {
				return true;
			}
			if (char1 == -1 || char2 == -1) {
				return false;
			}
		}
	}

	/**
	 *
	 * \brief compare
	 *
	 * \details
	 *
	 * \return boolean
	 *
	 * @param pstrFile2Compare
	 * @return
	 * @throws IOException
	 */
	public boolean compare(final String pstrFile2Compare) throws IOException, Exception {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::compare";
		return this.compare(new JSFile(pstrFile2Compare));
	} // public boolean compare

	public void Write(final StringBuffer pstrLine) throws Exception {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Write";
		this.Write(pstrLine.toString());
	} // public void Write

	/**
	 *
	 * \brief doLock - lock file on java-level against concurrent access
	 *
	 * \details
	 * This Methods locks the file on java-level against concurrent access.
	 *
	 * \return boolean - true, if the lock was sucessfull
	 *
	 * \see doUnlock
	 *
	 * \param strAccessMode
	 * The mode argument specifies the access mode in which the file is to be opened.
	 * The permitted values and their meanings are:
	 *
	 * @throws Exception
	 */
	public boolean doLock(final String pstrAccessMode) throws Exception {
		final String conMethodName = conClassName + "::doLock";
		// randomFile = new RandomAccessFile(this.strFileName, pstrAccessMode);
		randomFile = new RandomAccessFile(this, pstrAccessMode);
		FileChannel channel = randomFile.getChannel();
		FileLock fleLock = null;
		try {
			fleLock = channel.tryLock();
		}
		catch (OverlappingFileLockException e) {
			flgFileIsLocked = true;
		}
		catch (Exception e) {
			logger.error("doLock(String)", e); //$NON-NLS-1$
			throw new JobSchedulerException("doLock(String)", e);
		}
		if (fleLock == null) { // we couldnt acquire lock as it is already locked by another program instance)
			flgFileIsLocked = false;
			randomFile = null;
		}
		else {
			flgFileIsLocked = true;
		}
		if (flgFileIsLocked == true) {
			String JSH_I_0160 = "JSH_I_0160"; // "File locked: %1$s";
			message(String.format(JSH_I_0160, strFileName));
		}
		return flgFileIsLocked;
	} // private void doLock

	/**
	 *
	 * \brief doLock
	 *
	 * \details
	 *
	 * \return boolean
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean doLock() throws Exception {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::doLock";
		return this.doLock("rw");
	} // private boolean doLock

	public boolean doUnlock() throws IOException {
		final String conMethodName = conClassName + "::doUnlock";
		if (randomFile != null) {
			try {
				if (fleLock != null) {
					fleLock.release();
					fleLock = null;
				}
				randomFile.getChannel().close();
			}
			catch (Exception e) {
			}
			randomFile = null;
			flgFileIsLocked = false;
			message(String.format(JSH_I_0150, strFileName));
		}
		return true;
	} // private boolean doUnlock

	/**
	 *
	 * \brief isLocked
	 *
	 * \details
	 *
	 * \return boolean
	 *
	 * @return
	 */
	public boolean isLocked() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::isLocked";
		boolean flgIsLocked = false;
		if (randomFile != null) {
			flgIsLocked = flgFileIsLocked;
		}
		return flgIsLocked;
	} // private boolean isLocked

	public boolean setExclusive(final boolean pflgIsExclusive) throws IOException {
		final String conMethodName = conClassName + "::setExclusive";
		if (flgIsExclusive == true && pflgIsExclusive == false) {
			//
			this.doReleaseExclusive();
			return flgIsExclusive;
		}
		flgIsExclusive = pflgIsExclusive;
		if (flgIsExclusive == true) {
			if (this.checkExclusiveDeny() == false) {
				// String localHost = InetAddress.getLocalHost().getHostName();
				InetAddress ia = InetAddress.getLocalHost();
				String strUserName = System.getProperty("user.name");
				JSDataElementTimeStampISO tstIso = new JSDataElementTimeStampISO(new JSDataElementDate(new Date()));
				String strValues = strFileName + ";" + strUserName + ";" + tstIso.FormattedValue() + ";" + ia.getHostAddress();
				fleExclusiveFile.WriteLine(strValues);
				logger.debug(Messages.getMsg(JSH_I_0110, strValues));
				fleExclusiveFile.close();
			}
		}
		return flgIsExclusive;
	} // private boolean setExclusive

	/**
	 *
	 * \brief checkExclusiveDeny
	 *
	 * \details
	 *
	 * \return boolean
	 *
	 * @return
	 * @throws IOException
	 */
	private boolean checkExclusiveDeny() throws IOException {
		final String conMethodName = conClassName + "::checkExclusiveDeny";
		boolean flgExclusiveDeny = false;
		String strExclusiveFileNameExtension = System.getProperty(conPropertySOS_JSFILE_EXTENSION_4_EXCLUSIVEFILE, "~");
		File _fleExclusiveFile = new JSFile(strFileName + strExclusiveFileNameExtension);
		if (_fleExclusiveFile != null && _fleExclusiveFile.exists()) {
			/**
			 * exclusive-file exists. check, who is the owner.
			 * If the current user is not the owner, we must deny the request.
			 *
			 */
			strExclusiveFootPrint = fleExclusiveFile.File2String();
			if (strExclusiveFootPrint.length() > 0) {
				message(String.format("%1$s - Footprint is %2$s", conMethodName, strExclusiveFootPrint));
				String[] strP = strExclusiveFootPrint.split(";");
				if (strP[1].equalsIgnoreCase(System.getProperty("user.name")) == false) {
					fleExclusiveFile.close();
					fleExclusiveFile = null;
					flgExclusiveDeny = true;
				}
			}
		}
		return flgExclusiveDeny;
	} // private boolean checkExclusiveDeny

	private void doReleaseExclusive() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::doReleaseExclusive";
		if (flgIsExclusive == true && fleExclusiveFile != null) {
			fleExclusiveFile.delete();
			fleExclusiveFile = null;
			flgIsExclusive = false;
		}
	} // private void doReleaseExclusive

	public String toXml() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::toXml";
		String strXml = String.format("<file name='%1$s' size='%2$d' modificationdate='%3$s' />", this.getAbsolutePath(), fleFile.length(),
				new Date(fleFile.lastModified()));
		return strXml;
	} // private String toXml

	/**
	 * 
	*
	* \brief getTempdir
	*
	* \details
	* 
	* \return String
	*
	 */
	public static final String getTempdir() {
		return System.getProperty("java.io.tmpdir");
	}

	/**
	 * 
	*
	* \brief getUniqueFileName
	*
	* \details
	* 
	* \return String
	*
	 */
	public String getUniqueFileName() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getUniqueFileName";
		String strUniqueFileName = strFileName;
		String strE = getFileExtensionName();
		String strF = getName();
		if (this.exists() == true) {
			for (int i = 2;; i++) {
				String strN = "(" + String.valueOf(i) + ")";
				if (strE.length() <= 0) {
					strUniqueFileName = strFileName + strN;
				}
				else {
					strUniqueFileName = strFileName.replaceFirst(strE, strN + strE);
				}
				if (new File(strUniqueFileName).exists() == false) {
					break;
				}
			}
		}
		return strUniqueFileName;
	} // private String getUniqueFileName

	/**
	 * 
	*
	* \brief removeExtension
	*
	* \details
	* 
	* \return String
	* \see http://stackoverflow.com/questions/941272/how-do-i-trim-a-file-extension-from-a-string-in-java
	 */
	public String removeFileNameExtension() {
		//	    String separator = System.getProperty("file.separator");
		String filename = strFileName;
		// Remove the path upto the filename.
		int lastSeparatorIndex = strFileName.lastIndexOf(separator);
		if (lastSeparatorIndex != -1) {
			filename = strFileName.substring(lastSeparatorIndex + 1);
		}
		// Remove the extension.
		int extensionIndex = filename.lastIndexOf(".");
		if (extensionIndex != -1) {
			filename = filename.substring(0, extensionIndex);
		}
		return filename;
	}

	/**
	 * 
	*
	* \brief getFileExtensionName
	*
	* \details
	* 
	* \return String
	*
	 */
	public String getFileExtensionName() {
		String strRet = "";
		int intIdx = strFileName.lastIndexOf(".");
		if (intIdx == -1) {
			return "";
		}
		else {
			int intFL = strFileName.length();
			int intExtensionLength = strFileName.length() - intIdx;
			strRet = strFileName.substring(intFL - intExtensionLength, intFL);
		}
		return strRet;
	}

	/**
	 * \brief getstrCharSet4OutputFile
	 *
	 * \details
	 * getter
	 *
	 * @return the strCharSet4OutputFile
	 */
	public String getCharSet4OutputFile() {
		return strCharSet4OutputFile;
	}

	/**
	 * \brief setstrCharSet4OutputFile -
	 *
	 * \details
	 * setter
	 *
	 * @param strCharSet4OutputFile the value for strCharSet4OutputFile to set
	 */
	public void setCharSet4OutputFile(final String strCharSet4OutputFile) {
		this.strCharSet4OutputFile = strCharSet4OutputFile;
	}

	/**
	 * \brief getstrCharSet4InputFile
	 *
	 * \details
	 * getter
	 *
	 * @return the strCharSet4InputFile
	 */
	public String getCharSet4InputFile() {
		return strCharSet4InputFile;
	}

	/**
	 * \brief setstrCharSet4InputFile -
	 *
	 * \details
	 * setter
	 *
	 * @param strCharSet4InputFile the value for strCharSet4InputFile to set
	 */
	public void setCharSet4InputFile(final String strCharSet4InputFile) {
		this.strCharSet4InputFile = strCharSet4InputFile;
	}
}
