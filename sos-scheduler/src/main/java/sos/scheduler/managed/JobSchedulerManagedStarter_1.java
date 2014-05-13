package sos.scheduler.managed;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import sos.scheduler.job.JobSchedulerJob;
import sos.spooler.Job_chain;
import sos.spooler.Log;
import sos.spooler.Order;
import sos.spooler.Variable_set;
import sos.util.SOSClassUtil;
import sos.xml.SOSXMLValidator;

/**
 * Die Klasse instantiiert die dynamischen Job-Ketten und Jobs im Scheduler. 
 * 
 * @author andreas.pueschel@sos-berlin.com
 * @since 1.0 2005-03-05 
 */

public class JobSchedulerManagedStarter_1 extends JobSchedulerJob {

    /** Attribut: workflowOrders: Liste der Auftr�ge und zugeh�riger Modelle */
    private ArrayList workflowOrders = new ArrayList();

    /** Attribut: orderIterator: Iterator f�r Auftr�ge */
    private Iterator orderIterator = null;

    /** Settings Attribut: maxOrderCount: max. Gr��e der Auftragswarteschlangen aller Job-Ketten */
    private int maxOrderCount = 0;
    
    /** L�uft der Job als startscript? */
    private boolean startscript = true;

    private static final String schedulerXSDOld = "config/scheduler_interface_v1.0.xsd";
    private static final String schedulerXSD = "config/scheduler.xsd";
	
    private static final String spoolerXmlHeadOld = "<spooler xmlns=\"http://www.sos-berlin.com/schema/scheduler_interface_v1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sos-berlin.com/schema/scheduler_interface_v1.0 "+schedulerXSDOld+"\">";
    private static final String spoolerXmlHead = "<spooler xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\""+schedulerXSD+"\">";
    
    /**
     * Initialisierung
     */
    public boolean spooler_init() {
    	boolean rc = super.spooler_init();
    	startscript = spooler_job==null;
    	if (!rc) return false || startscript;
    	// herausfinden, obs ein Startscript ist:
    	
    	if (startscript)
			try {
				getLog().info("SchedulerManagedStarter is running as startscript");
			} catch (Exception e2) {}
		else
			try {
				getLog().info("SchedulerManagedStarter is running as job");
			} catch (Exception e3) {}
    	
        try {
        	String order="";
        	String model="";
        	String job="";
        	String once="";
        	String remove="";
        	try{
        	Variable_set params = spooler_task.params();
        	if(params!=null){
        		order = params.var("order");
        		model = params.var("model");
        		job   = params.var("job");
        		once  = params.var("once");
        		remove = params.var("remove_job_chain");
        	}
        	} catch (Exception e){}
            if (!startscript && (order+model+job+once).length()>0){            	
            	if (order.length()>0) this.initOrders(order);
            	else if (job.length()>0 && (model.equals("0") || model.length()==0)){
            		initJobs(job, once.equalsIgnoreCase("yes"),"0");
            	} else if(job.length()>0 && (!model.equals("0"))){
            		if(!initJobChains(model, remove.equalsIgnoreCase("true"))) initJobs(job,false,model);
            	} else if(!model.equals("0")){
            		initJobChains(model, remove.equalsIgnoreCase("true"));
            	}
            }else{
            	// initialize jobs, job chains and orders
            	this.initJobs("",false,"0");
            	this.initJobChains();
            	this.initOrders("");
            }
            return true;
        }
        catch (Exception e) {
            try {
				getLog().error("error occurred in initialization: " + e.getMessage());
			} catch (Exception e1) {}
                      
            return false || startscript;
        }
        finally {
            if (this.getConnection() !=  null) {
                try { this.getConnection().rollback(); } catch (Exception ex) {} // no error handling
                try { this.getConnection().disconnect(); } catch (Exception ex) {} // no error handling
                // this.setConnection(null);
            }
        }
    }
        
        
    /**
     * Cleanup
     */
    public void spooler_exit() {
        
    	// processing on shutdown of scheduler
    }
                

    /**
     * Initialisierung: Jobs erzeugen
     */
    public void initJobs(String jobID, boolean once, String model) throws Exception {
        
        HashMap currJob 	= null;
        ArrayList currJobs	= null;
       
        // Element jobElement   = null;
        // Document jobDocument = null;
        
        try { // to retrieve jobs
            
        	currJobs = this.getJobsOfWorkflowModel(Integer.parseInt(model), jobID);

            if (currJobs.isEmpty()) { 
                if (this.getLog() != null) this.getLog().info("no jobs found");
                return;
            }

            if (this.getLog() != null) this.getLog().debug3("..  " + currJobs.size() + " jobs found.");
            Iterator it = currJobs.iterator();

            while (it.hasNext()) {
                currJob = (HashMap) it.next();

                // Job ist evtl. in vorigem Job-Lauf angelegt worden
                // if ( this.spooler.job_exists(currJob.get("job_name").toString())) continue;
                
                
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();                
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();                
                Document jobDocument = docBuilder.newDocument();
                Element jobElement = jobDocument.createElement("job");
                if(currJob.get("job_name")!=null) jobElement.setAttribute("name", currJob.get("job_name").toString());
                if(currJob.get("title")!=null) jobElement.setAttribute("title", currJob.get("title").toString());
                if(currJob.get("model")==null || currJob.get("model").toString().length()==0 || currJob.get("model").toString().equals("0")){
                	jobElement.setAttribute("order","no");
                } else jobElement.setAttribute("order","yes");
                //if(currJob.get("spooler_id")!=null) jobElement.setAttribute("spooler_id", currJob.get("spooler_id").toString());
                if(currJob.get("timeout")!=null){
                	long timeout=Long.parseLong(currJob.get("timeout").toString());
                	if (timeout>0) jobElement.setAttribute("timeout",currJob.get("timeout").toString());
                }
                if(currJob.get("priority")!=null){
                	long priority=Long.parseLong(currJob.get("priority").toString());
                	if (priority>0) jobElement.setAttribute("priority",currJob.get("priority").toString());
                }
                if(currJob.get("tasks")!=null){
                	String tasks=currJob.get("tasks").toString();
                	if (tasks.length()>0) jobElement.setAttribute("tasks",tasks);
                }
                if(currJob.get("idle_timeout")!=null){
                	long idle_timeout=Long.parseLong(currJob.get("idle_timeout").toString());
                	if (idle_timeout>0) jobElement.setAttribute("idle_timeout",currJob.get("idle_timeout").toString());
                }
                if(currJob.get("min_tasks")!=null){
                	long min_tasks=Long.parseLong(currJob.get("min_tasks").toString());
                	if (min_tasks>0) jobElement.setAttribute("min_tasks",currJob.get("min_tasks").toString());
                }
                if(currJob.get("force_idle_timeout")!=null){
                	int fit=Integer.parseInt(currJob.get("force_idle_timeout").toString());
                	if (fit==0) jobElement.setAttribute("force_idle_timeout","no");
                	else jobElement.setAttribute("force_idle_timeout","yes");
                }
                String jobDescription = this.getConnection().getClob("SELECT \"DESCRIPTION\" FROM " + JobSchedulerManagedObject.getTableManagedJobs() 
    				  	+ " WHERE \"ID\"=" + currJob.get("id"));
      			if(jobDescription!=null && jobDescription.length()>0){      				
      				Element descriptionElement = jobDocument.createElement("description");
      			 	Text textNode = jobDocument.createTextNode(jobDescription);      			 	
      			 	descriptionElement.appendChild(textNode);      	  		    
      			 	jobElement.appendChild(descriptionElement);      	  		    
      			}
    			
                String jobParams = this.getConnection().getClob("SELECT \"PARAMS\" FROM " + JobSchedulerManagedObject.getTableManagedJobs() 
    				  	+ " WHERE \"ID\"=" + currJob.get("id"));
      			if(jobParams!=null && jobParams.length()>0){
      			 	Document paramsDocument = docBuilder.parse(new ByteArrayInputStream(jobParams.getBytes()));
      	  		    jobElement.appendChild(jobElement.getOwnerDocument().importNode(paramsDocument.getDocumentElement(), true));  	  			    
      			}
      			
      			
      			
      			String jobScript = this.getConnection().getClob("SELECT \"SCRIPT\" FROM " + JobSchedulerManagedObject.getTableManagedJobs() 
      					+ " WHERE \"ID\"=" + currJob.get("id"));
      			if (jobScript == null || jobScript.length() == 0) {
      				if (this.getLog() != null) this.getLog().warn("no job script found for managed job: " + currJob.get("id"));
      				continue;
      			}  			    
      			if(!validateScript(jobScript,"Job "+currJob.get("job_name"))) continue;
      			Document scriptDocument = docBuilder.parse(new ByteArrayInputStream(jobScript.getBytes()));
      			jobElement.appendChild(jobElement.getOwnerDocument().importNode(scriptDocument.getDocumentElement(), true));
      			String jobMonitor=null;
      			try{ // darf schiefgehen, weil evtl Monitor Feld nicht vorhanden ist
      				jobMonitor = this.getConnection().getClob("SELECT \"MONITOR_SCRIPT\" FROM " + JobSchedulerManagedObject.getTableManagedJobs() 
      						+ " WHERE \"ID\"=" + currJob.get("id"));
      			} catch (Exception e){
      				this.getLog().info("Table "+JobSchedulerManagedObject.getTableManagedJobs()+" does not have column \"MONITOR_SCRIPT\". " +
      				"Please update table.");
      			}
      			if (jobMonitor == null || jobMonitor.length() == 0) {
      				if (this.getLog() != null) this.getLog().debug3("no monitor script found for managed job: " + currJob.get("id"));  			  	
      			}else{  			    
      				//if(!validateMonitor(jobMonitor,"Job "+currJob.get("job_name"))) continue;
      				Document monitorDocument = docBuilder.parse(new ByteArrayInputStream(jobMonitor.getBytes()));
      				Element monitor = monitorDocument.getDocumentElement();
      				Node script = monitor.getFirstChild();
      				if(script!=null && script.getNodeType() == Node.ELEMENT_NODE){
      					this.getLog().debug9("Found script element.");
      					Element eScript = (Element) script;
      					String language = eScript.getAttribute("language");
      					if (language.length()>0){
      						this.getLog().debug9("Monitor Script language: "+language);
      						//String startScript = this.getConnectionSettings().getSectionEntry("scheduler","script","monitor_start_script."+language.toLowerCase());
      						String startScript= this.getConnection().getClob("SELECT \"LONG_VALUE\" FROM "+JobSchedulerManagedObject.getTableSettings()+" WHERE \"APPLICATION\"='scheduler' " +
      								"AND \"SECTION\"='script' AND \"NAME\"='"+"monitor_start_script."+language.toLowerCase()+"'");
      						if (startScript!=null && startScript.length()>0){
      							this.getLog().debug6("Inserting Startscript into Monitor-Script");
      							String[] haelften = jobMonitor.split("<!\\[CDATA\\[");
      							String newJobMonitor = haelften[0] + "<![CDATA[" +startScript+"\n"+haelften[1];
      							monitorDocument = docBuilder.parse(new ByteArrayInputStream(newJobMonitor.getBytes()));
      						}
      					}
      				}
      				jobElement.appendChild(jobElement.getOwnerDocument().importNode(monitorDocument.getDocumentElement(), true));
      			}
      			
  			    
  			    
  			    String jobRuntime = this.getConnection().getClob("SELECT \"RUN_TIME\" FROM " + JobSchedulerManagedObject.getTableManagedJobs() 
				  	+ " WHERE \"ID\"=" + currJob.get("id"));
  			    Element runtimeElement = null;
  			    if(jobRuntime!=null && jobRuntime.length()>0){
  			    	if (!validateRuntime(jobRuntime,"Job "+currJob.get("job_name"))) continue;
  			    	String dummyRuntime = "<dummy>"+jobRuntime+"</dummy>";  			    	
  			    	Document dummyDocument = docBuilder.parse(new ByteArrayInputStream(dummyRuntime.getBytes()));
  			    	Element dummyElement = dummyDocument.getDocumentElement();
  			    	  			    	for (Node child = dummyElement.getFirstChild(); child != null; child = child.getNextSibling())     {  			    		
  			    		jobElement.appendChild(jobElement.getOwnerDocument().importNode(child, true));
  			    		if (child.getNodeType() == child.ELEMENT_NODE){
  			    			Element childElement = (Element) child;
  			    			getLogger().debug9("Found <"+childElement.getNodeName()+"> element");
  			    			if (childElement.getNodeName().equalsIgnoreCase("run_time")){  	  			    				
  	  			    				runtimeElement = childElement;
  	  			    		}
  			    		}  			    		
  			    	}
  			    	/*if (!runtimeElement.getAttribute("once").equalsIgnoreCase("yes")) {
  			    		runtimeElement.setAttribute("once","yes");
  			    	}*/
  	  			      	  			    
  			    }
  			    
  			    

  			    jobDocument.appendChild(jobElement);
  			    
  			  
  			    /*
  			    String jobContent = this.getConnection().getClob("SELECT \"JOB\" FROM " + JobSchedulerManagedObject.getTableManagedJobs() 
						+ " WHERE \"ID\"=" + currJob.get("id"));
  			    if (jobContent == null || jobContent.length() == 0) {
  			    	if (this.getLog() != null) this.getLog().warn("no job configuration found for managed job: " + currJob.get("id"));
  			    	continue;
  			    }*/
//  			set up a transformer
  	            TransformerFactory transfac = TransformerFactory.newInstance();
  	            Transformer trans = transfac.newTransformer();
  	            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
  	            trans.setOutputProperty(OutputKeys.INDENT, "yes");

  	            //create string from xml tree
  	            StringWriter sw = new StringWriter();
  	            StreamResult result = new StreamResult(sw);
  	            DOMSource source = new DOMSource(jobDocument);
  	            trans.transform(source, result);
  	            String xmlString = sw.toString();

                try {
                    // Job ist evtl. in vorigem Job-Lauf angelegt worden
                    if (currJob.get("job_name") != null && this.spooler.job(currJob.get("job_name").toString()) != null) {
                        // this.spooler.job(currJob.get("job_name").toString()).remove();
                        spooler.execute_xml("<modify_job job=\"" + currJob.get("job_name").toString() + "\" cmd=\"remove\"/>");
                    }
                } catch (Exception e) {} // gracefully ignore this error
                
  			    try {
					if (this.getLog() != null) this.getLog().info(".. executing xml: "+xmlString);
					//spooler.execute_xml(jobContent);
					spooler.execute_xml(xmlString);
					
					if (once){
						boolean start_job=true;
						if (runtimeElement!=null){							
					    	String repeat = runtimeElement.getAttribute("repeat");
					    	if (repeat.length()>0 && !repeat.equals("0")) start_job=false;
					    	Node period = runtimeElement.getFirstChild();
					    	while (period!=null && start_job){
					    		if (period.getNodeType()==Node.ELEMENT_NODE){
					    			Element periodElement = (Element) period;
					    			repeat = periodElement.getAttribute("repeat");
					    		    if (repeat.length()>0 && !repeat.equals("0")) start_job=false;
					    		}
					    		period = period.getNextSibling();
					    	}
						}
						if(start_job) spooler.execute_xml("<start_job job=\""+currJob.get("job_name")+"\" at=\"now\">"+
								jobParams+"</start_job>");
					}
				} catch (Exception e1) {
					if(this.getLog()!=null) this.getLog().warn("Failed to initialize job \""+currJob.get("job_name")+"\": "+e1);
				}
  			    
            }
        } catch (Exception e) {
            throw new Exception("initJobs(): fatal error occurred: " + e.getMessage());
        }
    }
        

    public void initJobChains() throws Exception {
    	initJobChains("", false);
    }
    /**
     * Initialisierung: Jobketten erzeugen
     */
    public boolean initJobChains(String modelId, boolean remove) throws Exception {
        
        HashMap currWorkflowModel 		= null;
        ArrayList currWorkflowModels 	= null;
        ArrayList currJobs 				= null;

        try { // to retrieve worfklow models
            currWorkflowModels = this.getWorkflowModels(modelId);

            if (currWorkflowModels.isEmpty()) { 
                if (this.getLog() != null) this.getLog().info("no workflow models found");
                return false;
            }

            if (this.getLog() != null) this.getLog().debug3("..  " + currWorkflowModels.size() + " workflow models found.");
            Iterator it = currWorkflowModels.iterator();

            while (it.hasNext()) {
                currWorkflowModel = (HashMap) it.next();
                
                try {
					// Job Chain ist evtl. in vorigem Job-Lauf angelegt worden
					if (modelId.length()==0){
						if ( this.spooler.job_chain_exists(currWorkflowModel.get("name").toString())) continue;
					} else{
						// wenns die schon gibt, false zur�ckgeben, damit bekannt ist,
						// dass keine Jobchain eingerichtet wurde
						if ( this.spooler.job_chain_exists(currWorkflowModel.get("name").toString())) {
							if (remove){
								Job_chain oldChain = spooler.job_chain(currWorkflowModel.get("name").toString());
								oldChain.remove();
							}else return false;
						}
					}
					Job_chain jobChain=null;
					/*if ( this.spooler.job_chain_exists(currWorkflowModel.get("name").toString())){
						jobChain = spooler.job_chain(currWorkflowModel.get("name").toString());                	
					} else */jobChain = this.spooler.create_job_chain();
					jobChain.set_name(currWorkflowModel.get("name").toString());

					if (this.getLog() != null) this.getLog().debug6("..  create job chain for workflow model: " +
					       currWorkflowModel.get("name").toString());

					// Tasks des aktuellen Workflow Modells einlesen
					currJobs = new ArrayList();
					currJobs = this.getJobsOfWorkflowModel(Integer.parseInt(
					        currWorkflowModel.get("id").toString()));

					if (currJobs.isEmpty()) {
					    if (this.getLog() != null) this.getLog().warn(".. no job found for this workflow model [" +
					        currWorkflowModel.get("id").toString() + "]: " + currWorkflowModel.get("name"));
					    continue;
					}

					Iterator itJobs = currJobs.iterator();

					HashMap currJob = new HashMap();
					int maxOutputLevel = 0;
					Set inputLevels = new HashSet();
					Set outputLevels = new HashSet();
					Set errorLevels = new HashSet();
					while (itJobs.hasNext()) {
					    currJob = (HashMap) itJobs.next();
					    if (currJob.containsKey("job_name") &&
					            currJob.containsKey("input_level") &&
					            currJob.containsKey("output_level") &&
					            currJob.containsKey("error_level")) {
					    	
					    	
					    	initJobs(currJob.get("id").toString(), false,currWorkflowModel.get("id").toString());
					    	
					        /*if (Integer.parseInt(currJob.get("output_level").toString()) >  maxOutputLevel)
					            maxOutputLevel = Integer.parseInt(currJob.get("output_level").
					                                  toString());*/

					        jobChain.add_job(currJob.get("job_name").toString(),
					               currJob.get("input_level").toString(),
					               currJob.get("output_level").toString(),
					               currJob.get("error_level").toString()
					               );
					        
					        outputLevels.add(currJob.get("output_level").toString());
					        inputLevels.add(currJob.get("input_level").toString());
					        errorLevels.add(currJob.get("error_level").toString());
					        if (this.getLog() != null) this.getLog().debug3(".. job_name=" +
					            currJob.get("job_name").toString() +
					            ", input_level=" + currJob.get("input_level").toString() +
					            ", output_level=" + currJob.get("output_level").toString() +
					            ", error_level=" + currJob.get("error_level").toString() +
					            " .. added.");
					       
					    } // if
					} // while
					// Endzust�nde f�r Fehlerzust�nde ohne Folgeverarbeitung bekanntgeben:
					Iterator iter =errorLevels.iterator();
					while(iter.hasNext()){
						Object oLevel = iter.next();
						if(!inputLevels.contains(oLevel)){
							if (this.getLog() != null) this.getLog().debug9(".. try to add_end_state with error_level: " +
					                oLevel.toString());
					        jobChain.add_end_state(oLevel.toString());
					        if (this.getLog() != null) this.getLog().debug9(".. .. add_end_state with " +
					                oLevel.toString() + " ok.");
						}
					}
					
					// Endzustand des letzten Jobs mit positiven Status bekanntgeben
					/*if (this.getLog() != null) this.getLog().debug9(".. try to add_end_state with maxOutputLevel: " +
					       Integer.toString(maxOutputLevel));
					jobChain.add_end_state(Integer.toString(maxOutputLevel));
					if (this.getLog() != null) this.getLog().debug9(".. .. add_end_state with with maxOutputLevel: " +
					       Integer.toString(maxOutputLevel) + " ok.");*/
					
					// Ausganszust�nde, die nicht als Eingangszustand auftauchen als Endzustand bekanntgeben:
					outputLevels.removeAll(inputLevels);
					Iterator outIter = outputLevels.iterator();
					while (outIter.hasNext()){
						String endState = outIter.next().toString();
						if (this.getLog() != null) this.getLog().debug9(".. try to add_end_state with OutputLevel: " +
							       endState);
						jobChain.add_end_state(endState);
						if (this.getLog() != null) this.getLog().debug9(".. .. add_end_state with with OutputLevel: " +
							       endState + " ok.");
					}
					

					// Job Chain ist evtl. in parallelem Job-Lauf angelegt worden
					if (!spooler.job_chain_exists(currWorkflowModel.get("name").toString())) 
					    this.spooler.add_job_chain(jobChain);
				} catch (Exception e1) {
					if (this.getLog()!=null) this.getLog().warn("Failed to initialize Job Chain \""+currWorkflowModel.get("name").toString()+"\": "+e1);
				}
            
            } // while

            if (this.getLog() != null) this.getLog().info("Job Chains initialized.");
      
        } catch (Exception e) {
            throw new Exception("initJobChains(): fatal error occurred: " + e.getMessage());
        }
        return true;
    }


    /**
     * Initialisierung: Persistente Auftr�ge hinzuf�gen
     * 
     * @return boolean
     */
    public boolean initOrders(String orderID) {

        boolean rc                          = true;
        String currentWorkflowOrderId   	= new String();
        String currentWorkflowModelId   	= new String();
        String currentWorkflowModelName 	= new String();
        String currentJobLevel         		= new String();
        
        String whereOrderID					= new String();

        HashMap rec  						= new HashMap();
        HashMap task 						= new HashMap();

        if(orderID.length()>0) whereOrderID = " AND o.\"ID\"="+orderID;
        try {
           	String query = new String("SELECT o.\"SPOOLER_ID\", o.\"JOB_CHAIN\", o.\"ORDER_ID\"" 
                    + ", o.\"TITLE\", m.\"ID\" " 
                    + ", m.\"NAME\""
                    + ", MIN(j.\"INPUT_LEVEL\") AS \"LEVEL\""
                    + " FROM " + JobSchedulerManagedObject.getTableManagedOrders() + " o, "
                    + JobSchedulerManagedObject.getTableManagedJobs() + " j, " 
                    + JobSchedulerManagedObject.getTableManagedModels() + " m"
                    + " WHERE o.\"JOB_CHAIN\"=m.\"NAME\" AND m.\"SUSPENDED\"=0 AND j.\"SUSPENDED\"=0 AND o.\"SUSPENDED\"=0" 
                    + " AND j.\"MODEL\"=m.\"ID\""
					+ whereOrderID // z.B. "AND o.\"ID\"=42" oder ""
                    + " AND (o.\"SPOOLER_ID\"='" + this.spooler.id().toLowerCase() + "' OR o.\"SPOOLER_ID\" IS NULL)"
                    + " AND (m.\"SPOOLER_ID\"='" + this.spooler.id().toLowerCase() + "' OR m.\"SPOOLER_ID\" IS NULL)"
                    + " AND (j.\"SPOOLER_ID\"='" + this.spooler.id().toLowerCase() + "' OR j.\"SPOOLER_ID\" IS NULL)"
                    + " GROUP BY o.\"SPOOLER_ID\", o.\"JOB_CHAIN\", o.\"ORDER_ID\", o.\"TITLE\", m.\"ID\", m.\"NAME\""
                    );

       		this.setWorkflowOrders(this.getConnection().getArray(query));
       		this.setOrderIterator(this.getWorkflowOrders().iterator());
            
            if (!orderIterator.hasNext()) {
                if (this.getLog() != null) this.getLog().info("no persistent orders found");
                return false;
            }

            while(this.getOrderIterator().hasNext()) {
                rec = (HashMap) this.getOrderIterator().next();

                currentWorkflowOrderId   = rec.get("spooler_id").toString() + "-" +rec.get("job_chain").toString()+"-" +rec.get("order_id").toString();
                //currentWorkflowModelId   = rec.get("model").toString();
                currentWorkflowModelId   = rec.get("id").toString();
                //currentWorkflowModelName = rec.get("model_name").toString();
                currentWorkflowModelName = rec.get("name").toString();
                currentJobLevel          = rec.get("level").toString();
          
                // do not add orders if job chain exceeds setting for order queue length
                if ( maxOrderCount > 0 ) {
                    if ( spooler.job_chain(rec.get("name").toString()).order_count() >= this.getMaxOrderCount() ) {
                        this.getLog().info(".. current order for workflow [" + currentWorkflowOrderId +
  				              "] skipped: orderQueueLength [" +
  				              spooler.job_chain(rec.get("name").toString()).order_count() +
  				              "] exceeds maximum size [" + this.getMaxOrderCount() + "]");
                        return orderIterator.hasNext();
                    }
                }

                if (this.getLog() != null) {
                    this.getLog().debug3(".. current workflowOrderID is "   + currentWorkflowOrderId);
                    this.getLog().debug3(".. current workflowModelID is "   + currentWorkflowModelId);
                    this.getLog().debug3(".. current workflowModelName is " + currentWorkflowModelName);
                    this.getLog().debug3(".. current level is " + currentJobLevel);
                }


                
                Order order = spooler.create_order();
                order.set_id(currentWorkflowOrderId);        // Auftragsnummer
                if (rec.get("title")!=null && rec.get("title").toString().length()>0){
                	order.set_title(rec.get("title").toString());
                } else order.set_title(rec.get("order_id").toString());   // Auftragstitel 
                order.set_state(currentJobLevel);            // Position in der Jobkette                

          
                try { // to create order payload
                	String spoolerId = (String) rec.get("spooler_id");
                    String payload = this.getConnection().getClob("SELECT \"PARAMS\" FROM " + JobSchedulerManagedObject.getTableManagedOrders()
                            + " WHERE \"SPOOLER_ID\"" + 
								((spoolerId != null && spoolerId.length()>0) ? "='"+ spoolerId+"'" : " IS NULL") 
                            + " AND \"JOB_CHAIN\"='" + rec.get("job_chain").toString() + "'"
                            + " AND \"ORDER_ID\"='" + rec.get("order_id").toString() + "'"
                            );
                            
                    if (payload != null && payload.length() > 0) {
                        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                        Document payloadDocument = docBuilder.parse(new ByteArrayInputStream(payload.getBytes()));

                        sos.spooler.Variable_set orderData = spooler.create_variable_set();
                  
                        Node node = payloadDocument.getFirstChild();
                        while( node != null  &&  node.getNodeType() != Node.ELEMENT_NODE ) node = node.getNextSibling();

                        if ( node == null ) {
                            throw new Exception("payload contains no xml elements");
                        }
                        
                        Element payloadElement = (Element)node;
                        if (!payloadElement.getNodeName().equals( "params" ) )  
                            throw new Exception( "element <params> is missing" );
                             
                        node = payloadElement.getFirstChild();
                        while( node != null  &&  node.getNodeType() != Node.ELEMENT_NODE )  node = node.getNextSibling();
                 
                        boolean ascending = false;
                        String parameterName = "";
                        String parameterValue = "";
                        
                        while(node != null) {
                            NamedNodeMap attributes = node.getAttributes();

                            if ( node.getNodeName().equals( "param" ) ) {
                                try {
                                    if (attributes.getNamedItem("name") == null) {
                                        throw new Exception ("attribute <param name=\"...\"> is missing");
                                    }
                                    parameterName = attributes.getNamedItem("name").getNodeValue();
                                } catch (Exception e) {
                                    throw new Exception( "illegal value for attribute group: " + attributes.getNamedItem("name").getNodeValue());
                                }

                                try {
                                    if (attributes.getNamedItem("value") == null) {
                                        throw new Exception ("attribute <param value=\"...\"> is missing");
                                    }
                                    parameterValue = attributes.getNamedItem("value").getNodeValue(); 
                                } catch (Exception e) {
                                    throw new Exception( "illegal value for attribute: " + attributes.getNamedItem("value").getNodeValue());
                                }
                                
                                orderData.set_var(parameterName, parameterValue);
                                
                            } else if ( node.getNodeName().equals( "params" ) ) {
                              	break;
                            } 
                            
                            if (node.hasChildNodes() && !ascending) {
                                node = node.getFirstChild();
                            } else  if (node.getNextSibling() != null) {
                                node = node.getNextSibling();
                                ascending = false;
                            } else if (node.getParentNode() != null) {
                                node = node.getParentNode();
                                ascending = true;
                            } else {
                                break;
                            }
                        }

                        // set default order parameters
                        orderData.set_var("scheduler_order_id", order.id());
                        orderData.set_var("scheduler_order_managed_id", this.getConnectionSettings().getLockedSequenceAsString("scheduler", "counter", "scheduler_managed_order_id"));
                        orderData.set_var("scheduler_order_title", order.title());
                        orderData.set_var("scheduler_order_job_chain", currentWorkflowModelName);
                        this.getConnection().commit();

                        order.set_payload(orderData);
                    }
                }
                catch (Exception e) {
                    try { if (this.getConnection() != null) this.getConnection().rollback(); } catch (Exception ex) {}
                    getLog().warn("an error occurred setting the payload: " + e.getMessage());
                }
                try{ // to set the runtime
                	String spoolerId = (String) rec.get("spooler_id");
                    String runtime = this.getConnection().getClob("SELECT \"RUN_TIME\" FROM " + JobSchedulerManagedObject.getTableManagedOrders()
                            + " WHERE \"SPOOLER_ID\"" + 
								((spoolerId != null && spoolerId.length()>0) ? "='"+ spoolerId+"'" : " IS NULL") 
                            + " AND \"JOB_CHAIN\"='" + rec.get("job_chain").toString() + "'"
                            + " AND \"ORDER_ID\"='" + rec.get("order_id").toString() + "'"
                            );
                    
                    if (runtime!=null && runtime.length()>0) {
                    	if(!validateRuntime(runtime,"Order "+rec.get("order_id"))) continue;
                    	getLog().debug3("order.run_time(): ");                    	
                    	order.run_time().set_xml(runtime);                    	
                    }
                } catch(Exception e){
                	getLog().warn("an error occurred setting the runtime: " + e.getMessage());
                }
                
                this.getLog().debug3(".. add order to workflow " + currentWorkflowModelName);               
                try { // to add the order
                	if (orderID.length()>0) {
                		String answer = spooler.execute_xml("<remove_order job_chain=\""+currentWorkflowModelName+
                			"\" order=\""+order.id()+"\" />");                		
                	}

                    //spooler.job_chain(currentWorkflowModelName).add_order(order);
                	spooler.job_chain(currentWorkflowModelName).add_or_replace_order(order);
                    
                }
                catch (Exception e) {
                    // ignore this error, scheduler cares for repeated orders
                	this.getLog().debug6("an ignorable error occurred while adding order: " +  e.getMessage());
                }               
                
                this.getLog().info( "order [" + currentWorkflowOrderId + 
                          "] added to workflow [" + currentWorkflowModelId + "] " +
                          "with level [" + currentJobLevel + "]: " + 
						  currentWorkflowModelName );
            } // while

        } catch (Exception e) {
            rc = false;
            try {
				getLog().warn("initOrders(): error occurred: " + e.getMessage());
			} catch (Exception e1) {
			}
        }

        if (rc) {
            return this.getOrderIterator().hasNext();
        } else {
            return false;
        }
    }
    

  /**
   * liefert den Job f�r den n�chsten Auftrag zur�ck.
   *
   * @param workflowModel Modell des Workflows
   * @param taskLevel f�r die den Auftrag mit diesem Input-Level bedient
   * @return HashMap
   * @exception Exception.
   */
  	public HashMap getCurrentJob(int workflowModel,
                                 int taskLevel) throws Exception {

  	    StringBuffer query = null;
  	    if (this.getLog() != null) this.getLog().debug3("calling " + SOSClassUtil.getMethodName());

  	    try {
  	        query = new StringBuffer(
  	                "SELECT \"TITLE\", \"JOB_NAME\" FROM " + JobSchedulerManagedObject.getTableManagedJobs() + " WHERE \"MODEL\"=")
  	                .append(Integer.toString(workflowModel))
  	                .append(" AND \"INPUT_LEVEL\"=")
  	                .append(Integer.toString(taskLevel));
  	               // .append(" AND \"RUN_MODE\"=1");

  	        if (this.getLog() != null) this.getLog().debug6(".. query: " + query.toString());
  	        return this.getConnection().getSingle(query.toString());
  		}
  		catch (Exception e) {
  		  if (this.getLog() != null) this.getLog().warn( SOSClassUtil.getMethodName() + ": an error occurred: " + e.getMessage());
  		  throw new Exception(e);
  		}
  }


    /**
     * liefert ein verf�gbares Workflow Modell zur Task zur�ck
     *
     * @param jobName Name des Jobs
     * @return HashMap
     * @exception Exception.
     */
    public HashMap getWorkflowModel(String jobName) throws Exception {

        StringBuffer query = null;
    	if (this.getLog() != null) this.getLog().debug3("calling " + SOSClassUtil.getMethodName());

      	try {
      	    query = new StringBuffer(
      	    	"SELECT m.\"ID\", j.\"TITLE\", m.\"NAME\" FROM ")
      	    	.append(JobSchedulerManagedObject.getTableManagedJobs() + " j, ")
      	    	.append(JobSchedulerManagedObject.getTableManagedModels() + " m")
      	    	.append(" WHERE j.\"MODEL\"=m.\"ID\" AND j.\"SUSPENDED\"=0")
      	    	.append(" AND j.\"JOB_NAME\"='" + jobName.replaceAll("'", "''") + "'")
      	    	.append(" AND m.\"SUSPENDED\"=0")
      	    	.append(" AND (m.\"SPOOLER_ID\"='" + this.spooler.id().toLowerCase() +
      	    	"' OR m.\"SPOOLER_ID\" IS NULL)");

      	    if (this.getLog() != null) this.getLog().debug6(".. query: " + query.toString());
      	    return this.getConnection().getSingle(query.toString());
      	}
      	catch (Exception e) {
      	    if (this.getLog() != null) this.getLog().warn( SOSClassUtil.getMethodName() + ": an error occurred: " + e.getMessage());
      	    throw new Exception(e);
      	}
    }


    public ArrayList getWorkflowModels() throws Exception {
    	return getWorkflowModels("");
    }
    
    /**
     * liefert alle verf�gbaren Workflow Modelle zur�ck
     *
     * @return ArrayList
     * @exception Exception.
     */
    public ArrayList getWorkflowModels(String id) throws Exception {

  	    StringBuffer query = null;
  	    if (this.getLog() != null) this.getLog().debug3("calling " + SOSClassUtil.getMethodName());
  	    String whereId = "\"ID\">0";
  	    if (id.length()>0) whereId="\"ID\"="+id;
  	    
    	try {
    	    query = new StringBuffer(
    	    	"SELECT \"ID\", \"TITLE\", \"NAME\" FROM ")
    	    	.append(JobSchedulerManagedObject.getTableManagedModels())
    	    	.append(" WHERE \"SUSPENDED\"=0")
    	    	.append(" AND (\"SPOOLER_ID\"='" + this.spooler.id().toLowerCase() + "' OR \"SPOOLER_ID\" IS NULL) " +
    	    			" AND "+whereId);

    	    if (this.getLog() != null) this.getLog().debug6(".. query: " + query.toString());
    	    return this.getConnection().getArray(query.toString());
    	}
    	catch (Exception e) {
    	    if (this.getLog() != null) this.getLog().warn( SOSClassUtil.getMethodName() + ": an error occurred: " + e.getMessage());
    	    throw new Exception(e);
    	}
  	}

    

    /**
     * liefert alle Tasks des angegebenen Modells zur�ck
     *
     * @param modelId
     * @return ArrayList
     * @exception Exception
     */
  	private ArrayList getJobsOfWorkflowModel(int modelId) throws Exception {

  	    return getJobsOfWorkflowModel(modelId,"");
  }

  	/**
     * liefert alle Tasks des angegebenen Modells zur�ck
     *
     * @param modelId
     * @param jobID
     * @return ArrayList
     * @exception Exception
     */
  	private ArrayList getJobsOfWorkflowModel(int modelId, String jobId) throws Exception {

  	    StringBuffer query = null;
  	    String whereJobId = "";
  	    if (jobId.length()>0) whereJobId=" AND j.\"ID\"="+jobId;
  	    if (this.getLog() != null) this.getLog().debug3("calling " + SOSClassUtil.getMethodName());

    	try {
    		if (modelId > 0) {
    			query = new StringBuffer(
    	    		"SELECT j.\"ID\", j.\"TITLE\", j.\"JOB_NAME\", j.\"MODEL\", j.\"INPUT_LEVEL\", j.\"SPOOLER_ID\",")
					.append(" j.\"OUTPUT_LEVEL\", j.\"ERROR_LEVEL\", j.\"JOB_LOCK\", j.\"TIMEOUT\", j.\"PRIORITY\"," +
							" j.\"TASKS\", j.\"IDLE_TIMEOUT\", j.\"MIN_TASKS\", j.\"FORCE_IDLE_TIMEOUT\" FROM ")
					.append(JobSchedulerManagedObject.getTableManagedJobs() + " j, " + JobSchedulerManagedObject.getTableManagedModels() + " m")
					.append(" WHERE j.\"MODEL\"=m.\"ID\" AND m.\"SUSPENDED\"=0")
					.append(whereJobId)
					.append(" AND (m.\"SPOOLER_ID\"='" + this.spooler.id().toLowerCase() + "' OR m.\"SPOOLER_ID\" IS NULL)")
					.append(" AND (j.\"SPOOLER_ID\"='" + this.spooler.id().toLowerCase() + "' OR j.\"SPOOLER_ID\" IS NULL)")
					.append(" AND j.\"SUSPENDED\"=0 AND m.\"ID\"=")
					.append(modelId)
					.append(" ORDER BY j.\"OUTPUT_LEVEL\" ASC");
    		} else {
    			query = new StringBuffer(
	    		"SELECT j.\"ID\", j.\"TITLE\", j.\"JOB_NAME\", j.\"MODEL\", j.\"INPUT_LEVEL\", j.\"SPOOLER_ID\",")
				.append(" j.\"OUTPUT_LEVEL\", j.\"ERROR_LEVEL\", j.\"JOB_LOCK\", j.\"TIMEOUT\", j.\"PRIORITY\"," +
						" j.\"TASKS\", j.\"IDLE_TIMEOUT\", j.\"MIN_TASKS\", j.\"FORCE_IDLE_TIMEOUT\" FROM ")
				.append(JobSchedulerManagedObject.getTableManagedJobs() + " j")
				.append(" WHERE j.\"MODEL\"=0")
				.append(whereJobId)				
				.append(" AND (j.\"SPOOLER_ID\"='" + this.spooler.id().toLowerCase() + "' OR j.\"SPOOLER_ID\" IS NULL)")
				.append(" AND j.\"SUSPENDED\"=0")
				.append(" ORDER BY j.\"OUTPUT_LEVEL\" ASC");
    			/*
    			if (this.getConnection() instanceof SOSOracleConnection) {
    				query = new StringBuffer(
    	    		"SELECT j.\"ID\", j.\"TITLE\", j.\"JOB_NAME\", j.\"MODEL\", j.\"INPUT_LEVEL\", j.\"SPOOLER_ID\",")
					.append(" j.\"OUTPUT_LEVEL\", j.\"ERROR_LEVEL\", j.\"JOB_LOCK\", j.\"TIMEOUT\", j.\"PRIORITY\"," +
							" j.\"TASKS\", j.\"IDLE_TIMEOUT\" FROM ")
					.append(JobSchedulerManagedObject.getTableManagedJobs() + " j, " + JobSchedulerManagedObject.getTableManagedModels() + " m")
					.append(" WHERE j.\"MODEL\"=m.\"ID\"(+) AND m.\"SUSPENDED\"(+)=0 AND j.\"SUSPENDED\"=0")
					.append(" AND (m.\"SPOOLER_ID\"(+)='" + this.spooler.id().toLowerCase() + "')")
					.append(whereJobId)
					.append(" AND (j.\"SPOOLER_ID\"='" + this.spooler.id().toLowerCase() + "' OR j.\"SPOOLER_ID\" IS NULL)")
					.append(" ORDER BY j.\"OUTPUT_LEVEL\" ASC");
    			} else {
    				query = new StringBuffer(
    				"SELECT j.\"ID\", j.\"TITLE\", j.\"JOB_NAME\", j.\"MODEL\", j.\"INPUT_LEVEL\", j.\"SPOOLER_ID\",")
					.append(" j.\"OUTPUT_LEVEL\", j.\"ERROR_LEVEL\", j.\"JOB_LOCK\", j.\"TIMEOUT\", j.\"PRIORITY\"," +
							" j.\"TASKS\", j.\"IDLE_TIMEOUT\" FROM ")
					.append(JobSchedulerManagedObject.getTableManagedJobs() + " j left outer join " + JobSchedulerManagedObject.getTableManagedModels() + " m")
					.append(" ON j.\"MODEL\"=m.\"ID\"")
					.append(" WHERE (m.\"SUSPENDED\"=0 OR m.\"SUSPENDED\" IS NULL) AND j.\"SUSPENDED\"=0")
					.append(whereJobId)
					.append(" AND (m.\"SPOOLER_ID\"='" + this.spooler.id().toLowerCase() + "' OR m.\"SPOOLER_ID\" IS NULL)")
					.append(" AND (j.\"SPOOLER_ID\"='" + this.spooler.id().toLowerCase() + "' OR j.\"SPOOLER_ID\" IS NULL)")
					.append(" ORDER BY j.\"OUTPUT_LEVEL\" ASC");
    			}*/

    		}

    	    if (this.getLog() != null) this.getLog().debug6(".. query: " + query.toString());
    	    return this.getConnection().getArray(query.toString());
    	}
    	catch (Exception e) {
    	    if (this.getLog() != null) this.getLog().warn( SOSClassUtil.getMethodName() + ": an error occurred: " + e.getMessage());
    	    throw new Exception(e);
    	}
  }

   
    private boolean validateRuntime(String runtime, String forWho){
    	getLog().debug9("Validating run_time...");
    	File xsdFile = new File(schedulerXSD);
    	if(!xsdFile.canRead()){
    		getLog().warn("Cannot read "+schedulerXSD+". Validation will not be performed");
    		return true;
    	}
    	String xml = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
    		+spoolerXmlHead 
    		  +"<config>"
			    + "<jobs>"
				  + "<job name=\"dummy\">"
				    + runtime
				  + "</job>"
				+ "</jobs>"
			  +"</config>"
			+"</spooler>";
    	
    	ByteArrayInputStream bai = new ByteArrayInputStream(xml.getBytes());
    	InputSource source = new InputSource(bai);
    	try{
    		SOSXMLValidator.validate(source);
    	} catch (Exception e){
    		getLog().warn("run_time for "+forWho+" is not valid: "+e);
    		getLog().debug1("run_time: "+runtime);
    		getLog().debug6("run_time+dummy: "+xml);
    		return false;
    	}
    	getLog().debug9("run_time is valid.");
    	return true;    	
    }
    
    private boolean validateScript(String script, String forWho){
    	getLog().debug9("Validating run_time...");
    	File xsdFile = new File(schedulerXSD);
    	String head = spoolerXmlHead;
    	if(!xsdFile.canRead()){
    		xsdFile = new File(schedulerXSDOld);
    		head = spoolerXmlHeadOld;
    		if (!xsdFile.canRead()){
    		  getLog().warn("Cannot read "+schedulerXSD+". Validation will not be performed");
    		  return true;
    		}
    	}
    	
    	String xml = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
    		+head 
    		  +"<config>"
			    + "<jobs>"
				  + "<job name=\"dummy\">"
				    + script
				  + "</job>"
				+ "</jobs>"
			  +"</config>"
			+"</spooler>";
    	
    	ByteArrayInputStream bai = new ByteArrayInputStream(xml.getBytes());
    	InputSource source = new InputSource(bai);
    	try{
    		SOSXMLValidator.validate(source);
    	} catch (Exception e){
    		getLog().warn("script for "+forWho+" is not valid: "+e);
    		getLog().debug1("Script: "+script);
    		getLog().debug6("Script+dummy: "+xml);
    		return false;
    	}
    	getLog().debug9("script is valid.");
    	return true;    	
    }
    
    /**
     * @return Returns the maxOrderCount.
     */
    public int getMaxOrderCount() {
        return maxOrderCount;
    }
    
    /**
     * @param maxOrderCount The maxOrderCount to set.
     */
    public void setMaxOrderCount(int maxOrderCount) {
        this.maxOrderCount = maxOrderCount;
    }
    
    /**
     * @return Returns the orderIterator.
     */
    public Iterator getOrderIterator() {
        return orderIterator;
    }
    
    /**
     * @param orderIterator The orderIterator to set.
     */
    public void setOrderIterator(Iterator orderIterator) {
        this.orderIterator = orderIterator;
    }
    
    /**
     * @return Returns the workflowOrders.
     */
    public ArrayList getWorkflowOrders() {
        return workflowOrders;
    }
    
    /**
     * @param workflowOrders The workflowOrders to set.
     */
    public void setWorkflowOrders(ArrayList workflowOrders) {
        this.workflowOrders = workflowOrders;
    }

	
	public Log getLog() {
		if (startscript) return spooler.log();
		return spooler_log;
	}
	
	
}