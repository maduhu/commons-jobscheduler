package com.sos.scheduler.model.objects;

import com.sos.VirtualFileSystem.Factory.VFSFactory;
import com.sos.VirtualFileSystem.Interfaces.ISOSVFSHandler;
import com.sos.VirtualFileSystem.Interfaces.ISOSVfsFileTransfer;
import com.sos.VirtualFileSystem.Interfaces.ISOSVirtualFile;
import com.sos.graphviz.Edge;
import com.sos.graphviz.Graph;
import com.sos.graphviz.GraphIO;
import com.sos.graphviz.Node;
import com.sos.graphviz.SingleNodeProperties;
import com.sos.graphviz.enums.FileType;
import com.sos.graphviz.enums.RankDir;
import com.sos.graphviz.enums.SVGColor;
import com.sos.graphviz.enums.Shape;
import com.sos.graphviz.enums.Style;
import com.sos.scheduler.model.SchedulerHotFolder;
import com.sos.scheduler.model.SchedulerHotFolderFileList;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.objects.JobChain.JobChainNode.OnReturnCodes.OnReturnCode;
import com.sos.scheduler.model.objects.JobChain.JobChainNode.OnReturnCodes.OnReturnCode.ToState;

import org.apache.log4j.Logger;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class JSObjJobChain extends JobChain {
	@SuppressWarnings("unused") private final String		conClassName							= this.getClass().getSimpleName();
	@SuppressWarnings("unused") private static final String	conSVNVersion							= "$Id$";
	@SuppressWarnings("unused") private final Logger		logger									= Logger.getLogger(this.getClass());
	public final static String								fileNameExtension						= ".job_chain.xml";
	public static final String								conFileNameExtension4NodeParameterFile	= ".config.xml";
    private Hashtable<String, JobChainNode> jobChainNodes;
    private Hashtable<String, FileOrderSink> jobChainFileSinks;
	private Graph graph;
	private FileType graphVizImageType=FileType.png;
	private String dotOutputPath="./";

	public void setDotOutputPath(String dotOutputPath) {
        this.dotOutputPath = dotOutputPath;
    }

    public void setGraphVizImageType(FileType graphVizImageType) {
        this.graphVizImageType = graphVizImageType;
    }

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
        setHotFolderSrc(super.objVirtualFile);
		loadObject();
	}
    
 
	
	public void loadObject() {
		JobChain objJobChain = (JobChain) unMarshal(super.objVirtualFile);
		setObjectFieldsFrom(objJobChain);
		setHotFolderSrc(super.objVirtualFile);
	}

	public void loadObject(File file) {
        JobChain jobChain = (JobChain) unMarshal(file);
        setObjectFieldsFrom(jobChain);
        
        if (super.objVirtualFile == null){
            try {
                ISOSVFSHandler sosVFSHandler = VFSFactory.getHandler("local");
                ISOSVfsFileTransfer sosVFSFileTransfer = (ISOSVfsFileTransfer) sosVFSHandler;            
                ISOSVirtualFile virtualFile = sosVFSFileTransfer.getFileHandle(file.getAbsolutePath());
                super.objVirtualFile = virtualFile;
            } catch (Exception e) {
                e.printStackTrace();
            }
         }else{
         }
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
		//    	for (Object obj : this.getJobChainNodeOrFileOrderSinkOrJobChainNodeEnd()) {
		//			if (obj instanceof JobChainNodeJobChain) {
		//				flgR = true;
		//				break;
		//			}
		//		}
		return flgR;
	}

	/**
	 *
	 * \brief setOrdersRecoverable
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param pflgIsOrdersRecoverable
	 */
	public void setOrdersRecoverable(final boolean pflgIsOrdersRecoverable) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::setOrdersRecoverable";
		if (canUpdate() && pflgIsOrdersRecoverable != getYesOrNo(this.getOrdersRecoverable())) {
			if (pflgIsOrdersRecoverable == true) {
				this.setOrdersRecoverable(conYES);
			}
			else {
				this.setOrdersRecoverable(conNO);
			}
			setDirty();
		}
	} // public void setOrdersRecoverable

	/**
	 *
	 * \brief setVisible
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param pflgIsVisible
	 */
	public void setVisible(final boolean pflgIsVisible) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::setVisible";
		if (canUpdate()) {
			if (pflgIsVisible == true) {
				this.setVisible(conYES);
			}
			else {
				this.setVisible(conNO);
			}
			setDirty();
		}
	} // public void setVisible

	/**
	 *
	 * \brief setDistributed
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param pflgIsDistributed
	 */
	public void setDistributed(final boolean pflgIsDistributed) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::setDistributed";
		if (canUpdate() && getYesOrNo(super.getDistributed()) != pflgIsDistributed) {
			if (pflgIsDistributed == true) {
				this.setDistributed(conYES);
			}
			else {
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

	@Override public String getObjectName() {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::getObjectName";
		if (getHotFolderSrc() == null) {
			return "";
		}
		String name = this.getHotFolderSrc().getName();
		if (name == null) {
			name = "???";
		}
		else {
			name = name.substring(0, name.indexOf(JSObjJobChain.fileNameExtension));
			name = new File(name).getName();
		}
		return name;
	} // private String getJobName

	@Override public String getObjectNameAndTitle() {
		String strT = this.getObjectName();
		String strV = this.getTitle();
		if (strV != null && strV.isEmpty() == false) {
			strT += " - " + this.getTitle();
		}
		return strT;
	}

	@Override public void setName(final String pstrName) {
		if (canUpdate() == false) {
			return;
		}
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::setName";
		// TODO Check for valid name
		String strOldName = getObjectName();
		if (strOldName.equals(pstrName) == false) {
			changeSourceName(pstrName);
			super.setName(pstrName);
			setDirty();
			// TODO rename filename ?
			//			getHotFolderSrc().rename(strNewName);
		}
	} // private void setName

	@Override public String getTitle() {
		String strT = "";
		if (title == null) {
			strT = getObjectName();
		}
		else {
			strT = title;
		}
		return strT;
	}

	@Override public void setTitle(final String pstrTitle) {
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
		BigInteger maxOrders;
		try {
			maxOrders = new BigInteger(strMaxOrders);
			setMaxorders(maxOrders);
		}
		catch (NumberFormatException e) {
			maxOrders = new BigInteger("0");
		}
	}

	public void setMaxorders(final BigInteger maxOrders) {
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
		BigInteger intM = getmaxOrders();
		String strM = "";
		if (intM != null) {
			strM = String.valueOf(intM);
		}
		return strM;
	}
	
	
	private void newErrorNode(String state){
    	    if (jobChainNodes.get(state) == null){
                Node nNotExist = graph.getNodeOrNull(state);
                nNotExist.getSingleNodeProperties().setFillcolor(SVGColor.orangered);
                nNotExist.getSingleNodeProperties().setColor(SVGColor.blue);
                String strH = "<b>" + escapeHTML(state) + "</b>" + conHtmlBR;
                strH += "<i><font point-size=\"8\" color=\"yellow\" >missing</font></i>" + conHtmlBR;
                nNotExist.getSingleNodeProperties().setLabel(strH);
            }else{
                Node nErrorState = graph.getNodeOrNull(state);
                if (nErrorState != null){
                    nErrorState.getSingleNodeProperties().setFillcolor(SVGColor.orange);
                    nErrorState.getSingleNodeProperties().setColor(SVGColor.blue);
                    String strH = "<b>" + escapeHTML(state) + "</b>";
                    nErrorState.getSingleNodeProperties().setLabel(strH);
                }
            }
	}

    private Edge newEdge(String from, String to){
        Edge e = graph.newEdge(from, to);
        e.getEdgeProperties().setColor(SVGColor.cadetblue);
        e.getEdgeProperties().setArrowSize(0.5);
        e.getEdgeProperties().setFontSize(8);
        e.getEdgeProperties().setFontName("Arial");        

        //Handling File_Order_Sinks
        if (jobChainFileSinks.get(to) != null){
           Node nFileSink = graph.getNodeOrNull(to);
           String strH = "<b>" + escapeHTML(to) + "</b>" + conHtmlBR;
           strH += "<i><font point-size=\"8\" color=\"blue\" >File Sink</font></i>" + conHtmlBR;
           nFileSink.getSingleNodeProperties().setLabel(strH);
           nFileSink.getSingleNodeProperties().setFillcolor(SVGColor.beige); 
        }else{ 
            JobChainNode jobChainToNode = jobChainNodes.get(to);
            if (jobChainToNode == null){
               Node nNotExist = graph.getNodeOrNull(to);
               String strH = "<b>" + escapeHTML(to) + "</b>" + conHtmlBR;
               strH += "<i><font point-size=\"8\" color=\"yellow\" >missing</font></i>" + conHtmlBR;
               nNotExist.getSingleNodeProperties().setLabel(strH);
               nNotExist.getSingleNodeProperties().setFillcolor(SVGColor.lightgray); 
            }else{
                Node nExist = graph.getNodeOrNull(to);
               
                String strH = "<b>" + escapeHTML(to) + "</b>" + conHtmlBR;
                nExist.getSingleNodeProperties().setLabel(strH);
            }
        }
        return e;
    }
    
    private String getOrderNodeId(String n){
        return "order:" + n;
        
    }
    
    private void creatingOrders(File liveFolder, String firstState) throws Exception{
      //Creating orders
      // Get list of orders related to this JobChain
       
      String liveFolderName = liveFolder.getAbsolutePath();
      ISOSVFSHandler objVFS = VFSFactory.getHandler("local");
      ISOSVfsFileTransfer objFileSystemHandler = (ISOSVfsFileTransfer) objVFS;
      ISOSVirtualFile objHotFolder = objFileSystemHandler.getFileHandle(liveFolderName);
      SchedulerHotFolder objSchedulerHotFolder = objFactory.createSchedulerHotFolder(objHotFolder);
   
      SchedulerHotFolderFileList objSchedulerHotFolderFileList = objSchedulerHotFolder.load();
      for (JSObjBase hotFolderItem : objSchedulerHotFolderFileList.getOrderList()) {
          if (hotFolderItem instanceof JSObjOrder) {
              JSObjOrder order = (JSObjOrder) hotFolderItem;
              String jobchainName = order.getJobChainName();
              if (jobchainName.equalsIgnoreCase(this.getName())){
                  String from = order.getId();
                  String to = firstState;
                  logger.debug("createGraphVizImageFile.order found:" +  from);

                  if (order.getState() != null){
                      to = order.getState();
                  }
                  
                
                  Node  node = graph.newNode(getOrderNodeId(from));
                  SingleNodeProperties singleNodeProperties = node.getSingleNodeProperties();
                  singleNodeProperties.setShape(Shape.ellipse);
                  singleNodeProperties.setFillcolor(SVGColor.chartreuse);
                  
                        
                  String label = "Order:" + order.getId() + conHtmlBR;
                  if (order.getTitle() != null){
                     label += "<i><font point-size=\"8\" color=\"blue\" >" + escapeHTML(order.getTitle()) + "</font></i>" + conHtmlBR;
                  }
                 
                  singleNodeProperties.setLabel(label);
                  Edge eOrderEdge = newEdge(getOrderNodeId(from), to);
                        
                  eOrderEdge.getEdgeProperties().setConstraint(true);
                  eOrderEdge.getEdgeProperties().setStyle(Style.dashed);
                  eOrderEdge.getEdgeProperties().setColor(SVGColor.black);
               }
          }
      }    
   }
    
   
      
	public String createGraphVizImageFile(File imageOutputFolder,boolean showErrorNodes) throws Exception {
	    File liveFolder = new File(new File(this.getHotFolderSrc().getName()).getParent());
		JSObjJobChain jobchain = this;
 	    graph = new Graph();
		graph.getGraphProperties().setDirection(RankDir.TB);
        graph.getGraphProperties().setLabel(jobchain.getTitle());
        graph.getGraphProperties().setFontSize("8");

        graph.getGlobalNodeProperties().setFontsize("8");
        graph.getGlobalNodeProperties().setShape(Shape.box);
        graph.getGlobalNodeProperties().setStyle(Style.rounded + "," + Style.filled);
        graph.getGlobalNodeProperties().setFillcolor(SVGColor.azure);
        graph.getGlobalNodeProperties().setFontname("Arial");
        
 		 
	    graph.getGraphProperties().setRatio("auto");
	 
        logger.debug("createGraphVizImageFile.Parameter file:" +liveFolder.getAbsolutePath());
        logger.debug("createGraphVizImageFile.job chain.title:" +jobchain.getTitle());
	    
	 
		//Creating nodes for file order source.
		int fileOrderSourceCnt = 0;
		for (Object fileOrderSourceItem : jobchain.getFileOrderSourceList()) {
			if (fileOrderSourceItem instanceof FileOrderSource) {
 
				FileOrderSource fileOrderSource = (FileOrderSource) fileOrderSourceItem;
				String dir = fileOrderSource.getDirectory();
                logger.debug("createGraphVizImageFile.file_order_source found:" +  dir);
				
                String regExp = fileOrderSource.getRegex();
				fileOrderSourceCnt++;

				Node  node = graph.newNode("FileOrderSource" + fileOrderSourceCnt);
		        SingleNodeProperties singleNodeProperties = node.getSingleNodeProperties();
		        singleNodeProperties.setFillcolor(SVGColor.beige);
		        
		        String strH = "";
                strH = "<b>" + "Folder: " + dir + " </b>" + conHtmlBR;
                strH += "<i><b>" + escapeHTML("RegExp: " + regExp) + "</b></i>" + conHtmlBR;
                singleNodeProperties.setColor(SVGColor.blue);
		        singleNodeProperties.setLabel (strH);        
			}
		}
		
        //reading states in Hashmap for exist checking
        jobChainNodes = new Hashtable<String, JobChainNode>();
        jobChainFileSinks = new Hashtable<String, FileOrderSink>();
        
        String firstState = "";
		for (Object jobChainNodeItem : jobchain.getJobChainNodeOrFileOrderSinkOrJobChainNodeEnd()) {
            if (jobChainNodeItem instanceof FileOrderSink ) {

                FileOrderSink fileOrderSink = (FileOrderSink) jobChainNodeItem;
                 
                String state = fileOrderSink.getState();
                if (firstState.length() <= 0) {
                    firstState = state;
                }
                if (jobChainFileSinks.get(state) == null) {
                    jobChainFileSinks.put(state, fileOrderSink);
                }
            }

		    
		    if (jobChainNodeItem instanceof JobChainNode ) {
                JobChainNode jobChainNode = (JobChainNode) jobChainNodeItem;
                 
                String state = jobChainNode.getState();
                if (firstState.length() <= 0) {
                    firstState = state;
                }
                if (jobChainNodes.get(state) == null) {
                    jobChainNodes.put(state, jobChainNode);
                }
			}
		}
	 
	    creatingOrders(liveFolder,  firstState);

		//crating edges for file order sources
		String state = null;
		String nextState = null;
		fileOrderSourceCnt = 0;
 		for (Object fileOrderSourceItem : jobchain.getFileOrderSourceList()) {
			if (fileOrderSourceItem instanceof FileOrderSource) {
				FileOrderSource fileOrderSource = (FileOrderSource) fileOrderSourceItem;
				fileOrderSourceCnt++;
				nextState = fileOrderSource.getNextState();
				if (nextState.trim().length() <= 0) {
					nextState = firstState;
				}       
				
				Edge e = newEdge("FileOrderSource" + fileOrderSourceCnt, nextState);
		        e.getEdgeProperties().setConstraint(true);
			}
		}
 		
 		 
 		
		//Creating nodes and edges of the job chain 
		for (Object jobChainNodeItem : jobchain.getJobChainNodeOrFileOrderSinkOrJobChainNodeEnd()) {
			if (jobChainNodeItem instanceof JobChainNode) {
				JobChainNode jobChainNode = (JobChainNode) jobChainNodeItem;
				
				// to have the option to add edges by naming convention (from:state).
				state = jobChainNode.getState();
				int i = state.indexOf(":");
                logger.debug("createGraphVizImageFile.job_chain_node found:" +  state);

				
				if (i > 0 && jobChainNodes.get(state.substring(0, i)) != null) {
                    String from = state.substring(0, i);
 					Edge e = newEdge(from, state);
			        e.getEdgeProperties().setConstraint(true);
				}
				
			 
				nextState = jobChainNode.getNextState();
				if (nextState != null && nextState.length() > 0) {
				    
				    String strH = "";
				    
                   //Get the error handling
                    String suspend = "";
                    boolean isSuspend=false;
                    if (jobChainNode.getOnError() != null && jobChainNode.getOnError().equals("suspend")){
                        suspend = "on error suspend";
                        isSuspend = true;
                    }
                        
                    boolean isSetback = false;
                    if (jobChainNode.getOnError() != null && jobChainNode.getOnError().equals("setback")){
                        isSetback = true;
                     }
                         
		                
                    strH = "<b>" + escapeHTML(state) + "</b>" + conHtmlBR;
                    strH += "<i><font point-size=\"8\" color=\"blue\" >" + escapeHTML(jobChainNode.getJob()) + "</font></i>" + conHtmlBR;
                    if (isSuspend){
                        strH += "<i><font point-size=\"8\" color=\"red\" >" + escapeHTML(suspend) + "</font></i>" + conHtmlBR;
                    }

                    if (isSetback){
                        Edge eSetbackState = newEdge(state, state);
                        eSetbackState.getEdgeProperties().setConstraint(false);
                        eSetbackState.getEdgeProperties().setStyle(Style.dotted);
                        eSetbackState.getEdgeProperties().setLabel("..setback");
                    }
                    
                    
                    Node n = graph.newNode(state);
                    n.getSingleNodeProperties().setLabel(strH);
                  

                    Edge e = newEdge(state, nextState);
                    e.getEdgeProperties().setLabel("..next");
						
                    //handling the on_return_code properties
                    if (jobChainNode.getOnReturnCodes() != null){
                        for (Object onReturnCodeItem : jobChainNode.getOnReturnCodes().getOnReturnCode()){
                             if (onReturnCodeItem instanceof OnReturnCode) {
                                 ToState toState = ((OnReturnCode) onReturnCodeItem).getToState();
                                 if (toState != null){
                                     String toStateValue = toState.getState();
                                     Edge eReturnCode = newEdge(state, toStateValue);
                                     eReturnCode.getEdgeProperties().setLabel("..exit:" + ((OnReturnCode) onReturnCodeItem).getReturnCode());
                                 }
                             }
                       }
                    }
                        
                        

                    //Show error state nodes and edges				         
                    String errorState = jobChainNode.getErrorState();

	                if (showErrorNodes && !isSetback && !isSuspend && errorState != null) {
  	                    
                        Edge eErrorState = newEdge(state, errorState);
	                    
                        eErrorState.getEdgeProperties().setConstraint(true);
                        eErrorState.getEdgeProperties().setStyle(Style.dotted);

                        
                        if (jobChainFileSinks.get(errorState) == null){
                            newErrorNode(errorState);
                        }
	                        
	                 }
                 }else{//Endknoten
                     //newEndNode(state, SVGColor.azure, SVGColor.lightgray);
                 }
            }
        }
         
      
 		
 		//Output the graph to a file
	     GraphIO io = new GraphIO(graph);
         if (dotOutputPath.equals("./")){
             io.setDotDir(liveFolder.getAbsolutePath());
             logger.debug("createGraphVizImageFile.dotOutputPath" + liveFolder.getAbsolutePath());
         }else{
             if (dotOutputPath.length()  > 0){
                 io.setDotDir(dotOutputPath);
                 logger.debug("createGraphVizImageFile.dotOutputPath" + dotOutputPath);
             }
         }
        
        
         logger.debug("createGraphVizImageFile.jobchain.getObjectName()" + jobchain.getObjectName());
	     io.writeGraphToFile(graphVizImageType, new File(imageOutputFolder.getAbsolutePath(),jobchain.getObjectName() + "." + graphVizImageType) );
	     return new File(imageOutputFolder.getAbsolutePath(),jobchain.getObjectName()).getAbsolutePath() + "." + graphVizImageType;
	}

	
   
	@Override public BigInteger getmaxOrders() {
		if (maxOrders == null) {
			return new BigInteger("0");
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

	private void addToList (List <String> pobjL, final String pstrS) {
		if (pstrS != null && pobjL.contains(pstrS) == false) {
			pobjL.add(pstrS);
		}
	}
 
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
