package com.sos.graphviz.main;

import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.VirtualFileSystem.Factory.VFSFactory;
import com.sos.VirtualFileSystem.Interfaces.ISOSVFSHandler;
import com.sos.VirtualFileSystem.Interfaces.ISOSVfsFileTransfer;
import com.sos.VirtualFileSystem.Interfaces.ISOSVirtualFile;
import com.sos.graphviz.enums.FileType;
import com.sos.graphviz.jobchain.diagram.JobChainDiagramCreator;
import com.sos.scheduler.model.SchedulerHotFolder;
import com.sos.scheduler.model.SchedulerHotFolderFileList;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.objects.*;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.sos.scheduler.model.messages.JSMessages.*;

public class JSObjects2Graphviz extends JSJobUtilitiesClass<JSObjects2GraphvizOptions> {

    private static final String CLASSNAME = "JSObjects2Graphviz";
    private static final Logger LOGGER = LoggerFactory.getLogger(JSObjects2Graphviz.class);
    private ISOSVFSHandler vfs = null;
    private ISOSVfsFileTransfer fileSystemHandler = null;
    private SchedulerObjectFactory schedulerObjectFactory = null;
    private SchedulerHotFolderFileList schedulerHotFolderFileList = null;
    private String outputFolderName = "";

    public JSObjects2Graphviz() {
        super(new JSObjects2GraphvizOptions());
    }

    public JSObjects2Graphviz initialize() throws Exception {
        getOptions().checkMandatory();
        LOGGER.debug(getOptions().dirtyString());
        String liveFolderName = objOptions.liveFolderName.getValue();
        vfs = VFSFactory.getHandler("local");
        fileSystemHandler = (ISOSVfsFileTransfer) vfs;
        schedulerObjectFactory = new SchedulerObjectFactory();
        schedulerObjectFactory.initMarshaller(Spooler.class);
        ISOSVirtualFile hotFolder = fileSystemHandler.getFileHandle(liveFolderName);
        SchedulerHotFolder schedulerHotFolder = schedulerObjectFactory.createSchedulerHotFolder(hotFolder);
        LOGGER.info(String.format("... load %1$s", liveFolderName));
        schedulerHotFolderFileList = schedulerHotFolder.loadRecursive();
        outputFolderName = objOptions.outputFolderName.getValue();
        return this;
    }

    public JSObjects2Graphviz execute() throws Exception {
        final String methodName = CLASSNAME + "::execute";
        JOM_I_110.toLog(methodName);
        try {
            initialize();
            LOGGER.debug(String.format("%s job chains found", schedulerHotFolderFileList.getSortedFileList().size()));
            for (JSObjBase obj : schedulerHotFolderFileList.getSortedFileList()) {
                if (obj instanceof JSObjJobChain) {
                    JSObjJobChain jsObjJobChain = (JSObjJobChain) obj;
                    JobChainDiagramCreator jobChainDiagramCreator = new JobChainDiagramCreator(jsObjJobChain, new File(objOptions.liveFolderName.getValue()));
                    jobChainDiagramCreator.setGraphVizImageType(FileType.pdf);
                    jobChainDiagramCreator.setDotOutputPath(outputFolderName);
                    LOGGER.info(String.format("... call generator %1$s", outputFolderName));
                    jobChainDiagramCreator.createGraphVizFile(true);
                }
            }
        } catch (Exception e) {
            throw new JobSchedulerException(JOM_F_107.get(methodName) + ":" + e.getMessage(), e);
        }
        JOM_I_111.toLog(methodName);
        return this;
    }

}