package sos.net.sosftp;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;

import sos.net.SOSFTPCommand;
import sos.util.SOSLogger;
import sos.configuration.SOSConfiguration;

public class SOSFTPCommandSSH extends SOSFTPCommand {
	
	@SuppressWarnings("unused")
	String	conSVNVersion					= "$Id: SOSFTPOptions.java 17481 2012-06-29 15:40:36Z kb $";

	public SOSFTPCommandSSH(SOSLogger logger, Properties arguments_)
			throws Exception {
		super(logger, arguments_);
			
	}

	public SOSFTPCommandSSH(SOSConfiguration sosConfiguration_, SOSLogger logger)
			throws Exception {
		super(sosConfiguration_, logger);
		
	}
	
    
	/**
	 * 
	 * overwrite Method from super class.
	 * 
	 * Es darf keinen Banner f�r das SSH Job (=operation=execute) ausgegeben werden, wenn 
	 * banner_header bzw. banner_footer nicht angegeben sind.
	 * 
	 * This program logs output to stdout or to a file that has been specified by the parameter log_filename. 
	 * A template can be used in order to organize the output that is created. The output is grouped into header, file list and footer.
	 * This specifies a template file for header and footer output.
	 * Templates can use internal variables and parameters as placeholders in the form %{placeholder}. 
	 *   
	 * @param header
	 * @return String
	 * @throws Exception
	 */
	public String getBanner(boolean header) throws Exception {
		this.bannerHeader = "";
		this.bannerFooter = "";
		return super.getBanner(header);	
	}
	

}
