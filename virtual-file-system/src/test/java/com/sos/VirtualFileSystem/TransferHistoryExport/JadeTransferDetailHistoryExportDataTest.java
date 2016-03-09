package com.sos.VirtualFileSystem.TransferHistoryExport;

import java.util.Date;

import org.junit.Ignore;

import com.sos.VirtualFileSystem.Interfaces.IJadeTransferDetailHistoryData;

/** \class JadeTransferDetailHistoryExportDataTest
 * 
 * \brief JadeTransferDetailHistoryExportDataTest -
 * 
 * \details
 *
 * \section JadeTransferDetailHistoryExportDataTest.java_intro_sec Introduction
 *
 * \section JadeTransferDetailHistoryExportDataTest.java_samples Some Samples
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
 * \author Uwe Risse \version 20.10.2011 \see reference
 *
 * Created on 20.10.2011 12:02:46 */

// oh 18.04.14 No runnable methods [SP]
@Ignore("Test set to Ignore for later examination")
public class JadeTransferDetailHistoryExportDataTest implements IJadeTransferDetailHistoryData {

    @SuppressWarnings("unused")
    private final String conClassName = "JadeTransferDetailHistoryExportDataTest";

    public JadeTransferDetailHistoryExportDataTest() {
        //
    }

    @Override
    public Integer getTransferDetailsId() {
        return 1;
    }

    @Override
    public Integer getTransferId() {
        return 2;
    }

    @Override
    public Long getFileSize() {
        return new Long(3);
    }

    @Override
    public Integer getCommandType() {
        return 4;
    }

    @Override
    public String getCommand() {
        return "command";
    }

    @Override
    public String getPid() {
        return "1";
    }

    @Override
    public String getLastErrorMessage() {
        return "lastErrorMessage";
    }

    @Override
    public String getTargetFilename() {
        return "targetFileName";
    }

    @Override
    public String getSourceFilename() {
        return "sourceFileName";
    }

    @Override
    public String getMd5() {
        return "md5";
    }

    @Override
    public Integer getStatus() {
        return 4;
    }

    @Override
    public Date getStartTime() {
        return new Date();
    }

    @Override
    public Date getEndTime() {
        return new Date();
    }

    @Override
    public String getModifiedBy() {
        return "modifiedBy";
    }

    @Override
    public String getCreatedBy() {
        return "createdBy";
    }

    @Override
    public Date getCreated() {
        return new Date();
    }

    @Override
    public Date getModified() {
        return new Date();
    }

    @Override
    public String getStatusText() {
        return "statusText";
    }

    @Override
    public String getSizeValue() {
        return "sizeValue";
    }
}
