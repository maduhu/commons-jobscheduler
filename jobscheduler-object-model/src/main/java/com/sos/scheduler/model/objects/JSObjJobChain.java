package com.sos.scheduler.model.objects;

import com.sos.JSHelper.io.Files.JSTextFile;
import com.sos.VirtualFileSystem.Interfaces.ISOSVirtualFile;
import com.sos.VirtualFileSystem.shell.cmdShell;
import com.sos.scheduler.converter.graphviz.Dot;
import com.sos.scheduler.model.SchedulerObjectFactory;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/** \class JSObjJobChain
 *
 * \brief JSObjJobChain -
 *
 * \details
 *
 * \section JSObjJobChain.java_intro_sec Introduction
 *
 * \section JSObjJobChain.java_samples Some Samples
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
 * \author oh
 * 
 * @version $Id$ \see reference
 *
 *          Created on 09.02.2011 14:37:58 */
public class JSObjJobChain extends JobChain {

    @SuppressWarnings("unused")
    private final String conClassName = this.getClass().getSimpleName();
    @SuppressWarnings("unused")
    private static final String conSVNVersion = "$Id$";
    @SuppressWarnings("unused")
    private final Logger logger = Logger.getLogger(this.getClass());
    public final static String fileNameExtension = ".job_chain.xml";
    public static final String conFileNameExtension4NodeParameterFile = ".config.xml";

    public JSObjJobChain(final SchedulerObjectFactory schedulerObjectFactory) {
        super();
        objFactory = schedulerObjectFactory;
        super.strFileNameExtension = fileNameExtension;
    }

    public JSObjJobChain(final SchedulerObjectFactory schedulerObjectFactory, final JobChain origOrder) {
        this(schedulerObjectFactory);
        setObjectFieldsFrom(origOrder);
    }

    public JSObjJobChain(final SchedulerObjectFactory schedulerObjectFactory, final ISOSVirtualFile pobjVirtualFile) {
        this(schedulerObjectFactory);
        super.objVirtualFile = pobjVirtualFile;
        loadObject();
    }

    public void loadObject() {
        JobChain objJobChain = (JobChain) unMarshal(super.objVirtualFile);
        setObjectFieldsFrom(objJobChain);
        setHotFolderSrc(super.objVirtualFile);
    }

    public void setNameIfNotEmpty(String value) {
        if (!isEmpty(value)) {
            this.setName(value);
        }
    }

    public void setDistributedNotEmpty(String value) {
        if (!isEmpty(value)) {
            this.setDistributed(value);
        }
    }

    public void setMaxOrdersIfNotEmpty(String value) {
        if (!isEmpty(value)) {
            this.setMaxorders(value);
        }
    }

    public void setOrdersRecoverableIfNotEmpty(String value) {
        if (!isEmpty(value)) {
            this.setOrdersRecoverable(value);
        }
    }

    public void setVisibleIfNotEmpty(String value) {
        if (!isEmpty(value)) {
            this.setVisible(value);
        }
    }

    public void setTitleIfNotEmpty(String value) {
        if (!isEmpty(value)) {
            this.setTitle(value);
        }
    }

    public boolean isNestedJobChain() {
        boolean flgR = false;
        if (jobChainNodeJobChain != null) {
            flgR = true;
        }
        // for (Object obj :
        // this.getJobChainNodeOrFileOrderSinkOrJobChainNodeEnd()) {
        // if (obj instanceof JobChainNodeJobChain) {
        // flgR = true;
        // break;
        // }
        // }
        return flgR;
    }

    /** \brief setOrdersRecoverable
     *
     * \details
     *
     * \return void
     *
     * @param pflgIsOrdersRecoverable */
    public void setOrdersRecoverable(final boolean pflgIsOrdersRecoverable) {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::setOrdersRecoverable";
        if (canUpdate() && pflgIsOrdersRecoverable != getYesOrNo(this.getOrdersRecoverable())) {
            if (pflgIsOrdersRecoverable == true) {
                this.setOrdersRecoverable(conYES);
            } else {
                this.setOrdersRecoverable(conNO);
            }
            setDirty();
        }
    } // public void setOrdersRecoverable

    /** \brief setVisible
     *
     * \details
     *
     * \return void
     *
     * @param pflgIsVisible */
    public void setVisible(final boolean pflgIsVisible) {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::setVisible";
        if (canUpdate()) {
            if (pflgIsVisible == true) {
                this.setVisible(conYES);
            } else {
                this.setVisible(conNO);
            }
            setDirty();
        }
    } // public void setVisible

    /** \brief setDistributed
     *
     * \details
     *
     * \return void
     *
     * @param pflgIsDistributed */
    public void setDistributed(final boolean pflgIsDistributed) {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::setDistributed";
        if (canUpdate() && getYesOrNo(super.getDistributed()) != pflgIsDistributed) {
            if (pflgIsDistributed == true) {
                this.setDistributed(conYES);
            } else {
                this.setDistributed(conNO);
            }
            setDirty();
        }
    } // public void setDistributed

    public String createFileName(final String pstrPathName) {
        String strT = "";
        strT = pstrPathName + "/" + this.getName() + JSObjJobChain.fileNameExtension;
        return strT;
    }

    @Override
    public String getObjectName() {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::getObjectName";
        if (getHotFolderSrc() == null) {
            return "";
        }
        String name = this.getHotFolderSrc().getName();
        if (name == null) {
            name = "???";
        } else {
            name = name.substring(0, name.indexOf(JSObjJobChain.fileNameExtension));
            name = new File(name).getName();
        }
        return name;
    } // private String getJobName

    @Override
    public String getObjectNameAndTitle() {
        String strT = this.getObjectName();
        String strV = this.getTitle();
        if (strV != null && strV.isEmpty() == false) {
            strT += " - " + this.getTitle();
        }
        return strT;
    }

    @Override
    public void setName(final String pstrName) {
        if (canUpdate() == false) {
            return;
        }
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::setName";
        // TODO Check for valid name
        String strOldName = getObjectName();
        if (strOldName.equals(pstrName) == false) {
            changeSourceName(pstrName);
            super.setName(pstrName);
            setDirty();
            // TODO rename filename ?
            // getHotFolderSrc().rename(strNewName);
        }
    } // private void setName

    @Override
    public String getTitle() {
        String strT = "";
        if (title == null) {
            strT = getObjectName();
        } else {
            strT = title;
        }
        return strT;
    }

    @Override
    public void setTitle(final String pstrTitle) {
        if (canUpdate() == false) {
            return;
        }
        String strOldTitle = getTitle();
        if (strOldTitle.equals(pstrTitle) == false) {
            super.setTitle(pstrTitle);
            setDirty();
        }
    }

    public void setMaxorders(final String strMaxOrders) {
        if (canUpdate() == false) {
            return;
        }
        int maxOrders;
        try {
            maxOrders = Integer.parseInt(strMaxOrders);
            setMaxorders(maxOrders);
        } catch (NumberFormatException e) {
            maxOrders = 0;
        }
    }

    public void setMaxorders(final int maxOrders) {
        super.maxOrders = maxOrders;
        setDirty();
    }

    public boolean isDistributed() {
        return getYesOrNo(distributed);
    }

    public boolean isRecoverable() {
        return getYesOrNo(getOrdersRecoverable());
    }

    public boolean isVisible() {
        return getYesOrNo(getVisible());
    }

    public String getMaxOrders() {
        Integer intM = getmaxOrders();
        String strM = "";
        if (intM != null) {
            strM = String.valueOf(intM);
        }
        return strM;
    }

    public String createDOTFile() throws Exception {
        boolean flgCreateCluster = false;
        boolean flgCreateErrorNodes = false;
        JSObjJobChain objChain = this;
        String strName = objChain.getName();
        if (strName == null) {
            strName = objChain.getObjectName();
        }
        // TODO an Option is needed for the diagram work folder
        String strFileName = "c:/temp/dottest/" + strName;
        // TODO implement class JSDOTFile
        JSTextFile objDotFile = new JSTextFile(strFileName + ".dot");
        objDotFile.deleteOnExit();
        // Get list of orders related to this JobChain
        /** Hashtable<String, JSObjOrder> tblOrders = new Hashtable<String,
         * JSObjOrder>(); for (JSObjBase objO :
         * objSchedulerHotFolderFileList.getSortedFileList()) { if (objO
         * instanceof JSObjOrder) { JSObjOrder objOrder = (JSObjOrder) objO;
         * String strOrderName = objOrder.getJobChainName(); if
         * (strName.equalsIgnoreCase(strOrderName)) {
         * tblOrders.put(strOrderName, objOrder); } } } */
        objDotFile.WriteLine("digraph " + getQuoted(strName) + " {");
        objDotFile.WriteLine("rankdir = TB;");
        objDotFile.WriteLine("graph [");
        objDotFile.WriteLine("label = " + getQuoted(objChain.getTitle()));
        objDotFile.WriteLine("fontsize = 8");
        objDotFile.WriteLine("];");
        objDotFile.WriteLine("node [");
        objDotFile.WriteLine("fontsize = 8");
        objDotFile.WriteLine("shape = " + getQuoted("box"));
        objDotFile.WriteLine("style = " + getQuoted("rounded,filled"));
        objDotFile.WriteLine("fillcolor = " + getQuoted("#CCFF99"));
        // objDotFile.WriteLine("style = " + getQuoted("rounded"));
        // objDotFile.WriteLine("shape = " + getQuoted("plaintext"));
        // objDotFile.WriteLine("style = " + getQuoted("filled"));
        // objDotFile.WriteLine("fillcolor = " + getQuoted("#EEEEEE"));
        // objDotFile.WriteLine("color = " + getQuoted("#EEEEEE"));
        objDotFile.WriteLine("fontname = " + getQuoted("Arial"));
        objDotFile.WriteLine("];");
        objDotFile.WriteLine("edge [") //
        .WriteLine("color = " + getQuoted("#31CEF0")).WriteLine("arrowsize = " + getQuoted("0.5")).WriteLine("];");
        Hashtable<String, JobChainNode> tblNodes = new Hashtable<String, JobChainNode>();
        // objDotFile.WriteLine(getQuoted("start") + " [label = " +
        // getQuoted("start" + ": " + strName) + ", shape = " + getQuoted("box")
        // + ", style = "
        // + getQuoted("solid") + "];");
        // objDotFile.WriteLine(getQuoted("end") + " [label = " +
        // getQuoted("end" + ": " + strName) + ", shape = " + getQuoted("box") +
        // ", style = "
        // + getQuoted("solid") + "];");
        /** for (JSObjOrder objOrder : tblOrders.values()) {
         * objDotFile.WriteLine(getQuoted(objOrder.getObjectName()) +
         * " [label = " + getQuoted("Order - " + objOrder.getObjectName()) +
         * "];"); } */
        int intFileOrderSourceCnt = 0;
        String strFirstState = "";
        for (Object objO : objChain.getFileOrderSourceList()) {
            if (objO instanceof FileOrderSource) {
                FileOrderSource objFOS = (FileOrderSource) objO;
                String strDir = objFOS.getDirectory();
                String strRegExp = objFOS.getRegex();
                String strNextState = objFOS.getNextState();
                intFileOrderSourceCnt++;
                String strH = "";
                strH = "<b>" + "Folder: " + strDir + " </b>" + conHtmlBR;
                strH += "<i><b><font color=\"blue\" >" + escapeHTML("RegExp: " + strRegExp) + "</font></b></i>" + conHtmlBR;
                objDotFile.WriteLine(getQuoted("FileOrderSource" + intFileOrderSourceCnt) + " [label = <" + strH + ">];");
            }
        }
        for (Object objO : objChain.getJobChainNodeOrFileOrderSinkOrJobChainNodeEnd()) {
            if (objO instanceof JobChainNode) {
                JobChainNode objNode = (JobChainNode) objO;
                String strState = objNode.getState();
                if (strFirstState.length() <= 0) {
                    strFirstState = strState;
                }
                if (tblNodes.get(strState) == null) {
                    tblNodes.put(strState, objNode);
                    String strJobName = objNode.getJob();
                    if (strJobName == null) {
                        strJobName = "endNode";
                    }
                    String strT = strState;
                    if (strState.startsWith("!") && flgCreateErrorNodes == false) {
                        if (strState.equalsIgnoreCase(strJobName) == false) {
                            strT = strState + ": " + strJobName;
                        }
                        objDotFile.WriteLine(getQuoted(strState) + " [label = " + getQuoted(strT) + "];");
                    }
                    if (flgCreateErrorNodes == true) {
                        String strErrorState = objNode.getErrorState();
                        if (strErrorState != null && tblNodes.get(strErrorState) == null) {
                            tblNodes.put(strErrorState, objNode);
                            objDotFile.WriteLine(getQuoted(strErrorState) + " [label = " + getQuoted(strErrorState)
                                    + ", color=\"red\", fillcolor=\"yellow\", style=\"filled\", fontcolor=\"blue\"];");
                        }
                    }
                }
            }
        }
        // TODO implement Method: getNodes()
        // TODO implement Method: getAllNodeNames()
        /** for (JSObjOrder objOrder : tblOrders.values()) {
         * objDotFile.WriteLine(getQuoted(objOrder.getObjectName()) + " -> " +
         * getQuoted("start")); } */
        boolean flgStart = true;
        if (flgStart == true) {
            flgStart = false;
            // objDotFile.WriteLine(getQuoted("start") + " -> " + strState);
            if (flgCreateCluster) {
                objDotFile.WriteLine("subgraph cluster_0 {");
                objDotFile.WriteLine("    style=filled;");
                objDotFile.WriteLine("    color=lightgrey;");
                objDotFile.WriteLine("    node [style=filled,color=white];");
            }
        }
        String strState = null;
        String strNextState = null;
        String strLastNextState = null;
        intFileOrderSourceCnt = 0;
        for (Object objO : objChain.getFileOrderSourceList()) {
            if (objO instanceof FileOrderSource) {
                FileOrderSource objFOS = (FileOrderSource) objO;
                intFileOrderSourceCnt++;
                strNextState = objFOS.getNextState();
                if (strNextState.trim().length() <= 0) {
                    strNextState = strFirstState;
                }
                objDotFile.WriteLine(getQuoted("FileOrderSource" + intFileOrderSourceCnt) + " -> " + getQuoted(strNextState) + " [constraint=true]");
            }
        }
        for (Object objO : objChain.getJobChainNodeOrFileOrderSinkOrJobChainNodeEnd()) {
            if (objO instanceof JobChainNode) {
                JobChainNode objNode = (JobChainNode) objO;
                strState = objNode.getState();
                int i = strState.indexOf(":");
                if (i > 0) {
                    String strFrom = strState.substring(0, i);
                    objDotFile.WriteLine(getQuoted(strFrom) + " -> " + getQuoted(strState) + " [constraint=true]");
                }
                if (strState.startsWith("split") == false | i > 0) {
                    strNextState = objNode.getNextState();
                    // TODO Eigenschaft in JobChainNode
                    if (strNextState != null && strNextState.length() > 0) {
                        objDotFile.WriteLine(getQuoted(strState) + " -> " + getQuoted(strNextState));
                        strLastNextState = strNextState;
                    }
                }
                // TODO file_sink
                // else {
                // objDotFile.WriteLine(getQuoted(strState) + " -> " +
                // getQuoted("end"));
                // }
            }
        }
        // objDotFile.WriteLine(getQuoted(strLastNextState) + " -> " + "end");
        if (flgCreateCluster) {
            objDotFile.WriteLine("label = \"Process\";");
            objDotFile.WriteLine("}");
        }
        /** create the links to the error-states. */
        if (flgCreateErrorNodes == true) {
            Hashtable<String, JobChainNode> tblErrNodes = new Hashtable<String, JobChainNode>();
            for (Object objO : objChain.getJobChainNodeOrFileOrderSinkOrJobChainNodeEnd()) {
                if (objO instanceof JobChainNode) {
                    JobChainNode objNode = (JobChainNode) objO;
                    strState = objNode.getState();
                    String strErrorState = objNode.getErrorState();
                    if (strErrorState != null) {
                        objDotFile.WriteLine(getQuoted(strState) + " -> " + getQuoted(strErrorState) + " [style=\"dotted\", constraint=false]");
                        tblErrNodes.put(strErrorState, objNode);
                    } else {
                    }
                }
            }
            if (flgCreateCluster) {
                objDotFile.WriteLine("subgraph cluster_1 {");
                objDotFile.WriteLine("    style=filled;");
                objDotFile.WriteLine("color=lightgrey;");
                objDotFile.WriteLine("node [style=filled,color=white];");
            }
            String strLastErrNode = "";
            for (JobChainNode objErrNode : tblErrNodes.values()) {
                String strErrNodeName = objErrNode.getErrorState();
                if (flgCreateCluster) {
                    if (strLastErrNode.length() <= 0) {
                        strLastErrNode = strErrNodeName;
                    } else {
                        objDotFile.WriteLine(getQuoted(strLastErrNode) + " -> " + getQuoted(strErrNodeName) + " [style=invis]");
                        strLastErrNode = strErrNodeName;
                    }
                }
                objDotFile.WriteLine(getQuoted(strErrNodeName) + " -> " + getQuoted("end"));
            }
            if (flgCreateCluster) {
                objDotFile.WriteLine("label = \"Error\";");
                objDotFile.WriteLine("}");
            }
        }
        objDotFile.WriteLine("}");
        objDotFile.close();
        cmdShell objShell = new cmdShell();
        // TODO create Method in objDotFile
        String strCommandString = String.format(Dot.Command + " -x -T%1$s %2$s.dot > %2$s.%1$s", "jpg", strFileName);
        objShell.setCommand(strCommandString);
        objShell.run();
        return strFileName + ".jpg";
    }

    @Override
    public Integer getmaxOrders() {
        if (maxOrders == null) {
            return 0;
        }
        return maxOrders;
    }

    public List<FileOrderSink> getFileOrderSinkList() {
        List<FileOrderSink> objList = new ArrayList<FileOrderSink>();
        for (Object objO : this.getJobChainNodeOrFileOrderSinkOrJobChainNodeEnd()) {
            if (objO instanceof FileOrderSink) {
                objList.add((FileOrderSink) objO);
            }
        }
        return objList;
    }

    public List<FileOrderSource> getFileOrderSourceList() {
        List<FileOrderSource> objList = new ArrayList<FileOrderSource>();
        for (Object objO : this.getFileOrderSource()) {
            if (objO instanceof FileOrderSource) {
                objList.add((FileOrderSource) objO);
            }
        }
        return objList;
    }

    public List<JobChainNode> getJobChainNodeList() {
        List<JobChainNode> objList = new ArrayList<JobChainNode>();
        for (Object objO : this.getJobChainNodeOrFileOrderSinkOrJobChainNodeEnd()) {
            if (objO instanceof JobChainNode) {
                objList.add((JobChainNode) objO);
            }
        }
        return objList;
    }

    public boolean isStateDefined(final String state) {
        for (String _state : getAllStates()) {
            if (_state.equals(state)) {
                return true;
            }
        }
        return false;
    }

    public String[] getAllStates() {
        return getStates();
    }

    public String[] getStates() {
        List<String> strStatesList = new ArrayList<String>();
        for (JobChainNode objNode : getJobChainNodeList()) {
            addToList(strStatesList, objNode.errorState);
            addToList(strStatesList, objNode.nextState);
            addToList(strStatesList, objNode.state);
        }
        return arrayListToStringArray(strStatesList);
    }

    public String[] getErrorStates() {
        List<String> strStatesList = new ArrayList<String>();
        for (JobChainNode objNode : getJobChainNodeList()) {
            addToList(strStatesList, objNode.errorState);
        }
        return arrayListToStringArray(strStatesList);
    }

    public String[] getNextStates() {
        List<String> strStatesList = new ArrayList<String>();
        for (JobChainNode objNode : getJobChainNodeList()) {
            addToList(strStatesList, objNode.nextState);
        }
        return arrayListToStringArray(strStatesList);
    }

    private void addToList(List<String> pobjL, final String pstrS) {
        if (pstrS != null && pobjL.contains(pstrS) == false) {
            pobjL.add(pstrS);
        }
    }

    // private String[] arrayListToStringArray (final List <String> pobjArray) {
    // String[] strA = new String[pobjArray.size()];
    // strA = pobjArray.toArray(strA);
    // return strA;
    // }
    //
    public List<JobChainNodeEnd> getJobChainNodeEndList() {
        List<JobChainNodeEnd> objList = new ArrayList<JobChainNodeEnd>();
        for (Object objO : this.getJobChainNodeOrFileOrderSinkOrJobChainNodeEnd()) {
            if (objO instanceof JobChainNodeEnd) {
                objList.add((JobChainNodeEnd) objO);
            }
        }
        return objList;
    }
}
