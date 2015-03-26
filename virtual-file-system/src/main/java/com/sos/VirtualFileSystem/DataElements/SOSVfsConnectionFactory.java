/**
 *
 */
package com.sos.VirtualFileSystem.DataElements;
import org.apache.log4j.Logger;

import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.VirtualFileSystem.Factory.VFSFactory;
import com.sos.VirtualFileSystem.Interfaces.ISOSVFSHandler;
import com.sos.VirtualFileSystem.Interfaces.ISOSVfsFileTransfer;
import com.sos.VirtualFileSystem.Options.SOSConnection2OptionsAlternate;
import com.sos.VirtualFileSystem.Options.SOSFTPOptions;

/**
 * @author KB
 *
 */
public class SOSVfsConnectionFactory {
	@SuppressWarnings("unused") private final String		conClassName		= this.getClass().getSimpleName();
	@SuppressWarnings("unused") private static final String	conSVNVersion		= "$Id$";
	private final static Logger		logger				= Logger.getLogger(SOSVfsConnectionFactory.class);
	protected SOSFTPOptions									objOptions			= null;
	private SOSVfsConnectionPool							objConnPoolSource	= null;
	private SOSVfsConnectionPool							objConnPoolTarget	= null;

	/**
	 *
	 */
	public SOSVfsConnectionFactory(final SOSFTPOptions pobjOptions) {
		super();
		objOptions = pobjOptions;
		createConnectionPool();
	}

	public SOSVfsConnectionPool getSourcePool() {
		return objConnPoolSource;
	}

	public SOSVfsConnectionPool getTargetPool() {
		return objConnPoolTarget;
	}

	public void createConnectionPool() {
		if (objConnPoolSource != null && objOptions.PollingServer.isTrue()) {
			// dann nehmen wir genau diesen Pool
			logger.debug("use existing connection pool");
		}
		else {
			int intMaxParallelTransfers = 1;
			if (objOptions.ConcurrentTransfer.isTrue()) {
				intMaxParallelTransfers = objOptions.MaxConcurrentTransfers.value() + 1;
			}
			if (objConnPoolSource == null || objOptions.reuseConnection.isFalse()) {
				logger.debug("create connection pool");
				objConnPoolSource = new SOSVfsConnectionPool();
				objConnPoolTarget = new SOSVfsConnectionPool();
				for (int i = 0; i < intMaxParallelTransfers; i++) {
					objConnPoolSource.add(getVfsHandler(true));
					// TODO handle multiple targets
					if (objOptions.NeedTargetClient() == true) {
						objConnPoolTarget.add(getVfsHandler(false));
					}
				}
			}
			else {
				// TODO check for changes in the connection options (host, user)
				logger.debug("use existing connection pool");
			}
		}
	}

	/**
	 * 
	 * @param isSource
	 * @return
	 */
	private ISOSVFSHandler getVfsHandler(final boolean isSource) {
		ISOSVFSHandler handler = null;
		try {
			SOSConnection2OptionsAlternate options;
			String dataType;
			if (isSource) {
				options = objOptions.getConnectionOptions().Source();
				dataType = objOptions.getDataSourceType();
			}
			else {
				options = objOptions.getConnectionOptions().Target();
				dataType = objOptions.getDataTargetType();
			}
			options.loadClassName.SetIfNotDirty(objOptions.getConnectionOptions().loadClassName);
			VFSFactory.setConnectionOptions(options);
			handler = VFSFactory.getHandler(dataType);
			handler.Options(objOptions);
			
			if (isSource) {
				handler.setSource();
			}
			else{
				handler.setTarget();
			}
			
			ISOSVfsFileTransfer client = (ISOSVfsFileTransfer)handler;
			try{
				handler.Connect(options);
				handler.Authenticate(options);
			}
			catch(Exception e){
				if (!options.Alternatives().host.IsEmpty() && !options.Alternatives().user.IsEmpty()) {
					// TODO respect alternate authentication, eg password and/or public key
					logger.info(String.format("Connection failed : %s", e.toString()));
					logger.info(String.format("Try again using the alternate options ..."));
						
					JobSchedulerException.LastErrorMessage = "";
						
					try{
						client.disconnect();
					}
					catch(Exception ce){
						logger.warn(String.format("client disconnect failed : %s",ce.toString()));
					}
						
					handler.Connect(options.Alternatives());
					handler.Authenticate(options.Alternatives());
					
					options.setAlternateOptionsUsed("true");
				}
				else{
					logger.debug(String.format("alternate options are not defined"));
					throw e;
				}
			}
			
			handleClient(client, options, isSource);
		}
		catch (Exception ex) {
			//			https://change.sos-berlin.com/browse/SOSFTP-212
			//			throw (RuntimeException) ex;
			throw new JobSchedulerException(ex);
		}
		return handler;
	}

	/**
	 * 
	 * @param client
	 * @throws Exception
	 */
	private void handleClient(ISOSVfsFileTransfer client,SOSConnection2OptionsAlternate options,boolean isSource) throws Exception{
		if (objOptions.passive_mode.value() || options.passive_mode.isTrue()) {
			client.passive();
		}
		//objConnectOptions.transfer_mode is not used?
		if (options.transfer_mode.isDirty() && options.transfer_mode.IsNotEmpty()) {
			client.TransferMode(options.transfer_mode);
		}
		else {
			client.TransferMode(objOptions.transfer_mode);
		}
		client.ControlEncoding(objOptions.ControlEncoding.Value());
		// TODO pre-commands for source and target separately
		if (objOptions.PreFtpCommands.IsNotEmpty() && isSource == false) {
			// TODO Command separator as option
			for (String strCmd : objOptions.PreFtpCommands.split()) {
				strCmd = objOptions.replaceVars(strCmd);
				client.getHandler().ExecuteCommand(strCmd);
			}
		}
		if (options.PreFtpCommands.IsNotEmpty()) {
			// TODO Command separator as option
			for (String strCmd : options.PreFtpCommands.split()) {
				strCmd = options.replaceVars(strCmd);
				client.getHandler().ExecuteCommand(strCmd);
			}
		}
	}

	public void clear() {
		objConnPoolSource.clear();
		objConnPoolTarget.clear();
	}
}
