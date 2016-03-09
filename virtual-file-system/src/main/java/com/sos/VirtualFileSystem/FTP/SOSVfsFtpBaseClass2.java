package com.sos.VirtualFileSystem.FTP;

import java.io.IOException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.JSHelper.interfaces.ISOSConnectionOptions;
import com.sos.JSHelper.interfaces.ISOSDataProviderOptions;
import com.sos.VirtualFileSystem.DataElements.SOSFileList;
import com.sos.VirtualFileSystem.DataElements.SOSFileListEntry;
import com.sos.VirtualFileSystem.Interfaces.ISOSAuthenticationOptions;
import com.sos.VirtualFileSystem.Interfaces.ISOSConnection;
import com.sos.VirtualFileSystem.Interfaces.ISOSVfsFileTransfer2;
import com.sos.VirtualFileSystem.Options.SOSConnection2OptionsAlternate;
import com.sos.VirtualFileSystem.Options.SOSConnection2OptionsSuperClass;
import com.sos.i18n.annotation.I18NResourceBundle;

/** \class SOSVfsFtpBaseClass
 *
 * \brief SOSVfsFtpBaseClass -
 *
 * \details
 *
 * \section SOSVfsFtpBaseClass.java_intro_sec Introduction
 *
 * \section SOSVfsFtpBaseClass.java_samples Some Samples
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
 * \author KB \version $Id$ 21.04.2011 \see reference
 *
 * Created on 21.04.2011 19:31:35 */
/** @author KB */
@I18NResourceBundle(baseName = "SOSVirtualFileSystem", defaultLocale = "en")
public class SOSVfsFtpBaseClass2 extends SOSVfsFtpBaseClass implements ISOSVfsFileTransfer2 {

    private final String conClassName = this.getClass().getSimpleName();
    @SuppressWarnings("unused")
    private static final String conSVNVersion = "$Id$";
    private final Logger logger = Logger.getLogger(this.getClass());

    protected Vector<SOSFileListEntry> objFileListEntries = null;

    @SuppressWarnings("unused")
    private final ISOSAuthenticationOptions objAO = null;

    public SOSVfsFtpBaseClass2() {
        super();
        //
    }

    @Override
    public void clearFileListEntries() {
        objFileListEntries = new Vector<SOSFileListEntry>();
    }

    /*
     * @param host the remote ftp server
     * @param port the port number of the remote server
     * @throws java.net.SocketException
     * @throws java.io.IOException
     * @throws java.net.UnknownHostException
     * @see org.apache.commons.net.SocketClient#connect(java.lang.String, int)
     */
    @Override
    public void connect(final String phost, final int pport) {
        final String conMethodName = conClassName + "::connect";

        try {
            host = phost;
            port = pport;
            String strM = SOSVfs_D_0101.params(host, port);
            logger.debug(strM);
            if (isConnected() == false) {
                Client().connect(host, port);
                logger.info(SOSVfs_D_0102.params(host, port));
                LogReply();
                // String strT;
                // try {
                // strT = Client().getSystemType();
                // logger.info(String.format("System-Type = %1$s", strT));
                // }
                // catch (IOException e) {
                // e.printStackTrace();
                // }
            } else {
                logger.warn(SOSVfs_D_0103.params(host, port));

            }
        } catch (Exception e) {
            RaiseException(e, HostID(SOSVfs_E_0105.params(conMethodName)));
        }
    }

    @Override
    public final ISOSConnection Connect() {
        final String conMethodName = conClassName + "::Connect";

        String strH = host = objConnectionOptions.getHost().Value();
        int intP = port = objConnectionOptions.getPort().value();

        logger.debug(SOSVfs_D_0101.params(strH, intP));
        try {
            this.connect(strH, intP);
            logger.info(SOSVfs_D_0102.params(strH, intP));
        } catch (RuntimeException e) {
            logger.info(SOSVfs_E_0107.params(host, port) + e.getMessage());
            String strAltHost = host = objConnectionOptions.getalternative_host().Value();
            int intAltPort = port = objConnectionOptions.getalternative_port().value();

            if (isNotEmpty(strAltHost) && intAltPort > 0) {
                logger.debug(SOSVfs_D_0101.params(strAltHost, intAltPort));
                this.connect(strAltHost, intAltPort);
                logger.info(SOSVfs_D_0102.params(strAltHost, intAltPort));
            } else {
                logger.info(SOSVfs_E_0107.params(host, port, e.getMessage()));
                RaiseException(e, HostID(SOSVfs_E_0105.params(conMethodName)));
            }
        }
        return this;
    }

    @Deprecated
    @Override
    public ISOSConnection Connect(final ISOSConnectionOptions pobjConnectionOptions) throws Exception {
        final String conMethodName = conClassName + "::Connect";

        objConnectionOptions = pobjConnectionOptions;
        try {
            host = objConnectionOptions.getHost().Value();
            port = objConnectionOptions.getPort().value();
            // TODO try alternate host, if this connection is not possible
            this.connect(host, port);
            // TODO find the "Microsoft FTP Server" String from the reply and
            // set the HostType accordingly
        } catch (Exception e) {
            RaiseException(e, HostID(SOSVfs_E_0105.params(conMethodName)));
        }
        return this;
    }

    /** \brief Connect
     *
     * \details
     *
     * \return
     *
     * @param pobjConnectionOptions
     * @return */
    @Override
    public final ISOSConnection Connect(final SOSConnection2OptionsAlternate pobjConnectionOptions) {
        final String conMethodName = conClassName + "::Connect";
        objConnection2Options = pobjConnectionOptions;
        try {
            objHost = objConnection2Options.getHost();
            objPort = objConnection2Options.getport();
            this.connect(objHost.Value(), objPort.value());
            if (Client().isConnected() == false) {
                SOSConnection2OptionsSuperClass objAlternate = objConnection2Options.Alternatives();
                objHost = objAlternate.host;
                objPort = objAlternate.port;
                logger.info(SOSVfs_I_0121.params(host));
                this.connect(objHost.Value(), objPort.value());
                if (Client().isConnected() == false) {
                    objHost = null;
                    objPort = null;
                    host = "";
                    port = -1;
                    RaiseException(SOSVfs_E_204.get());
                }
            }
            // TODO find the "Microsoft FTP Server" String from the reply and
            // set the HostType accordingly
            // TODO respect Proxy-Server. implement handling of
        } catch (Exception e) {
            RaiseException(e, HostID(SOSVfs_E_0105.params(conMethodName)));
        }
        return this;
    }

    @Override
    public ISOSConnection Connect(final ISOSDataProviderOptions pobjConnectionOptions) throws Exception {
        return null;
    }

    @Override
    public ISOSConnection Connect(final String pstrHostName, final int pintPortNumber) throws Exception {
        this.connect(pstrHostName, pintPortNumber);
        if (objConnectionOptions != null) {
            objConnectionOptions.getHost().Value(pstrHostName);
            objConnectionOptions.getPort().value(pintPortNumber);
        }
        return this;
    }

    @Override
    public Vector<SOSFileListEntry> getFileListEntries() {
        if (objFileListEntries == null) {
            objFileListEntries = new Vector<SOSFileListEntry>();
        }
        return objFileListEntries;
    }

    @Override
    public SOSFileList getFileListEntries(final SOSFileList pobjSOSFileList, final String folder, final String regexp,
            final boolean flgRecurseSubFolder) {
        getFilenames(folder, flgRecurseSubFolder, regexp);
        for (SOSFileListEntry objEntry : objFileListEntries) {
            pobjSOSFileList.add(objEntry);
        }
        return pobjSOSFileList;
    }

    /** return a listing of the contents of a directory in short format on the
     * remote machine (without subdirectory)
     *
     * @param pathname on remote machine
     * @return a listing of the contents of a directory on the remote machine
     * @throws IOException
     *
     * @exception Exception
     * @see #dir() */
    protected Vector<String> getFilenames(final String pstrPathName, final boolean flgRecurseSubFolders, final String regexp) {
        String strCurrentDirectory = null;
        @SuppressWarnings("hiding")
        Vector<String> vecDirectoryListing = new Vector<String>();
        String lstrPathName = pstrPathName.trim();
        strCurrentDirectory = lstrPathName;
        if (lstrPathName.length() <= 0) {
            lstrPathName = ".";
        }
        if (lstrPathName.equals(".")) {
            lstrPathName = DoPWD();
            strCurrentDirectory = lstrPathName;
        }
        FTPFile[] objFTPFileList = null;
        getFileListEntries();

        try {
            logger.debug(String.format("start directory scan for '%1$s'", lstrPathName));
            Client().setListHiddenFiles(false);
            objFTPFileList = Client().listFiles(lstrPathName);
        } catch (IOException e1) {
            throw new JobSchedulerException("listfiles failed", e1);
        }

        if (objFTPFileList == null || objFTPFileList.length <= 0) {
            return vecDirectoryListing;
        }

        Pattern pattern = null;
        if (isNotEmpty(regexp)) {
            pattern = Pattern.compile(regexp, 0);
        }

        strCurrentDirectory = addFileSeparator(strCurrentDirectory);
        for (FTPFile objFTPFile : objFTPFileList) {
            String strCurrentFile = objFTPFile.getName();
            if (isNotHiddenFile(strCurrentFile) && strCurrentFile.trim().length() > 0) {
                boolean flgIsDirectory = objFTPFile.isDirectory();
                if (flgIsDirectory == false) {
                    /** the file_spec has to be compared to the filename only ...
                     * excluding the path */
                    boolean flgSelected = true;
                    if (pattern != null) {
                        Matcher matcher = pattern.matcher(strCurrentFile);
                        flgSelected = matcher.find();
                    }
                    if (flgSelected) {
                        if (strCurrentFile.startsWith(strCurrentDirectory) == false) {
                            strCurrentFile = strCurrentDirectory + strCurrentFile;
                        }
                        vecDirectoryListing.add(strCurrentFile);
                        objFTPFile.setName(strCurrentFile); // The name comes
                                                            // from the server
                                                            // without a
                                                            // path-name
                        SOSFileListEntry objF = new SOSFileListEntry(objFTPFile);
                        objF.VfsHandler(this);
                        objF.Options(objOptions);
                        objFileListEntries.add(objF);
                    }
                } else {
                    if (flgIsDirectory && flgRecurseSubFolders == true) {
                        if (flgRecurseSubFolders) {
                            // TODO die Directories evtl. in jeweils einem
                            // separaten Thread parsen? Bringt das was? Option
                            // w�re gut.
                            // TODO special regexp for the folder names?
                            Vector<String> vecNames = getFilenames(strCurrentDirectory + strCurrentFile, flgRecurseSubFolders, regexp);
                            if (vecNames != null & vecNames.size() > 0) {
                                vecDirectoryListing.addAll(vecNames);
                            }
                        }
                    }
                }
            }
        }

        return vecDirectoryListing;
    } // getFilenames

    /** return a listing of the contents of a directory in short format on the
     * remote machine
     * 
     * @param pathname on remote machine
     * @return a listing of the contents of a directory on the remote machine
     *
     * @exception Exception
     * @see #dir() */
    @Override
    public Vector<String> nList(final String pathname, final boolean flgRecurseSubFolder) {
        try {
            return getFilenames(pathname, flgRecurseSubFolder, null);
        } catch (Exception e) {
            throw new JobSchedulerException(SOSVfs_E_128.params("getfilenames", "nLixt"), e);
        }
    } // nList

    /** return the size of remote-file on the remote machine on success,
     * otherwise -1
     * 
     * @param remoteFile the file on remote machine
     * @return the size of remote-file on remote machine */
    @Override
    public long size(final String pstrRemoteFileName) throws Exception {
        long lngSize = -1L;
        getFileListEntries();
        for (SOSFileListEntry objFileEntry : objFileListEntries) {
            if (objFileEntry.getSourceFilename().equalsIgnoreCase(pstrRemoteFileName)) {
                lngSize = objFileEntry.getFileSize();
                break;
            }
        }
        if (lngSize == -1L) {
            FTPFile objF = getFTPFile(pstrRemoteFileName);
            if (objF != null) {
                lngSize = objF.getSize();
            }
        }
        return lngSize;
    }
}
