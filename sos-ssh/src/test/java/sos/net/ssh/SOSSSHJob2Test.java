package sos.net.ssh;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.JSHelper.Listener.JSListenerClass;
import com.sos.JSHelper.io.Files.JSTextFile;
import com.sos.i18n.annotation.I18NResourceBundle;

/**
* \class SOSSSHJob2Test
*
* \brief SOSSSHJob2Test -
*
* \details
*
* \section SOSSSHJob2Test.java_intro_sec Introduction
*
* \section SOSSSHJob2Test.java_samples Some Samples
*
* \code
*   .... code goes here ...
* \endcode
*
* <p style="text-align:center">
* <br />---------------------------------------------------------------------------
* <br /> APL/Software GmbH - Berlin
* <br />##### generated by ClaviusXPress (http://www.sos-berlin.com) #########
* <br />---------------------------------------------------------------------------
* </p>
* \author KB
* @version $Id$17.05.2010
* \see reference
*
* Created on 17.05.2010 13:01:51
 */
@I18NResourceBundle(baseName = "com.sos.net.messages", defaultLocale = "en")
// oh 07.05.14 test haengt? [SP] testSimulateShellParam haengt, testExecuteWithCCAndDelimiter schl�gt fehl
public class SOSSSHJob2Test extends JSJobUtilitiesClass<SOSSSHJobOptions> {
	@SuppressWarnings({ "hiding" })
	private static final Logger	logger			= Logger.getLogger(SOSSSHJob2Test.class);

	private SOSSSHJob2 objSSH = null;
	private SOSSSHJobOptions objOptions = null;
	
	public SOSSSHJob2Test() {
		super(new SOSSSHJobOptions());
	}

	private void initializeClazz () {
		objSSH = new SOSSSHJobTrilead();
		objOptions = objSSH.getOptions();
		objSSH.setJSJobUtilites(this);
		JSListenerClass.bolLogDebugInformation = true;
		JSListenerClass.intMaxDebugLevel = 9;
    if( !Logger.getRootLogger().getAllAppenders().hasMoreElements() ) {
      BasicConfigurator.configure();
    }
		logger.setLevel(Level.DEBUG);
	}
	@Test
	public void testExecute() throws Exception {
		initializeClazz();
		String strArgs[] = new String[] { "-command", "ls", "-auth_method", "password", "-host", "wilma.sos", "-auth_file=test", "-user=test",
				"-password", "12345" };
		objOptions.CommandLineArgs(strArgs);
		objSSH.execute();
		assertEquals("auth_file", objOptions.auth_file.Value(), "test");
		assertEquals("user", objOptions.user.Value(), "test");
	}

	@Test (expected=com.sos.JSHelper.Exceptions.JobSchedulerException.class)  // auth_file not found
	public void testExecuteUsingKeyFile() throws Exception {
		initializeClazz();
		String strArgs[] = new String[] { "-command", "ls", "-auth_method=publickey", "-host", "wilma.sos", "-auth_file=./src/test/resources/testing-key.key", "-user", "test",
				"-password", "12345" };
		objOptions.CommandLineArgs(strArgs);
		objSSH.execute();
		assertEquals("auth_file", objOptions.auth_file.Value(), "test");
		assertEquals("user", objOptions.user.Value(), "test");
	}

	@Test(expected = com.sos.JSHelper.Exceptions.JobSchedulerException.class)
	public void testExecuteWithCC() throws Exception {
		initializeClazz();
		String strArgs[] = new String[] { "-command", "ls;exit 5", "-auth_method", "password", "-host", "wilma.sos", "-auth_file", "test", "-user", "kb",
				"-password", "kb" };
		objOptions.CommandLineArgs(strArgs);
		objSSH.execute();
		assertEquals("auth_file", objOptions.auth_file.Value(), "test");
		assertEquals("user", objOptions.user.Value(), "test");
		assertEquals("ExitCode not as expected", objSSH.getCC(), 5);
	}

	@Test
	public void testExecuteWithCC0() throws Exception {
		initializeClazz();
		String strArgs[] = new String[] { "-command", "ls hallo;exit 0", "-auth_method", "password", "-host", "wilma.sos", "-auth_file", "test", "-user",
				"test", "-password", "12345", "-ignore_stderr", "true" };
		objOptions.CommandLineArgs(strArgs);
		objSSH.execute();
		assertEquals("auth_file", objOptions.auth_file.Value(), "test");
		assertEquals("user", objOptions.user.Value(), "test");
		assertEquals("ExitCode not as expected", objSSH.getCC(), 0);
	}

	@Test
	public void testExecuteWithCCAndIgnore() throws Exception {
		initializeClazz();
		String strArgs[] = new String[] { "-command", "ls hallo;exit 0", "-auth_method", "password", "-host", "wilma.sos", "-auth_file", "test", "-user",
				"test", "-password", "12345", "-ignore_stderr", "false" };
		objOptions.CommandLineArgs(strArgs);
		objSSH.execute();
		assertEquals("auth_file", objOptions.auth_file.Value(), "test");
		assertEquals("user", objOptions.user.Value(), "test");
		assertEquals("ExitCode not as expected", objSSH.getCC(), 0);
	}

	/**
	 *
	*
	* \brief testExecuteWithCCAndDelimiter
	*
	* \details
	*  we expect an error for "ls hallo" and an immediate exit with cc = 1.
	*
	* \return void
	*
	 */
	@Test
	@Ignore("Test set to Ignore for later examination")
	public void testExecuteWithCCAndDelimiter() throws Exception {
		initializeClazz();
		String strArgs[] = new String[] { "-command", "ls hallo%%exit 0", "-auth_method", "password", "-host", "wilma.sos", "-auth_file", "test", "-user",
				"test", "-password", "12345", "-ignore_stderr", "false" };
		objOptions.CommandLineArgs(strArgs);
		objSSH.execute();
		assertEquals("auth_file", objOptions.auth_file.Value(), "test");
		assertEquals("user", objOptions.user.Value(), "test");
		assertEquals("ExitCode not as expected", objSSH.getCC(), 0);
	}

	@Test
	public void testExecuteCmdString() throws Exception {
		initializeClazz();
		String strArgs[] = new String[] { "-command_script", "ps;ls", "-auth_method", "password", "-host", "wilma.sos", "-auth_file", "test", "-user", "test",
				"-password", "12345" };
		objOptions.CommandLineArgs(strArgs);
		objSSH.execute();
		assertEquals("auth_file", objOptions.auth_file.Value(), "test");
		assertEquals("user", objOptions.user.Value(), "test");
	}

	@Test
	public void testExecuteCmdScriptFile() throws Exception {
		initializeClazz();
		String strArgs[] = new String[] { "-command_script_file", "R:/nobackup/junittests/testdata/SSH/hostname.sh", "-auth_method", "password", "-host", "wilma.sos", "-auth_file", "test",
				"-user", "test", "-password", "12345" };
		objOptions.CommandLineArgs(strArgs);
		objSSH.execute();
		assertEquals("auth_file", objOptions.auth_file.Value(), "test");
		assertEquals("user", objOptions.user.Value(), "test");
	}
	

	@Test
	public void testExecuteCommands() throws Exception {
		initializeClazz();
		String strArgs[] = new String[] { "-command_script", "ps;ls $SCHEDULER_PARAM_test", "-auth_method", "password", "-host", "wilma.sos", "-auth_file",
				"test", "-user", "test", "-password", "12345" };
		objOptions.CommandLineArgs(strArgs);
		objSSH.setJSJobUtilites(this);
		objSSH.execute();

		String strCmd[] = objSSH.getCommands2Execute();

		//		assertEquals("strCmd", "ps", strCmd[0]);
		//		assertEquals("strCmd", "ls .", strCmd[1]);
		assertEquals("auth_file", objOptions.auth_file.Value(), "test");
		assertEquals("user", objOptions.user.Value(), "test");
	}

	@Test
	public void testExecuteScriptFile() throws Exception {
		initializeClazz();
		JSTextFile objScriptFile = new JSTextFile("t.1");
		objScriptFile.WriteLine("ps");
		objScriptFile.deleteOnExit();
		objScriptFile.close();

		String strArgs[] = new String[] { "-command_script_file", "t.1", "-auth_method", "password", "-host", "wilma.sos", "-auth_file", "test", "-user",
				"test", "-password", "12345" };
		objOptions.CommandLineArgs(strArgs);
		objSSH.setJSJobUtilites(this);
		objSSH.execute();

		String strCmd[] = objSSH.getCommands2Execute();

		//		assertEquals("strCmd", "ps", strCmd[0]);
		//		assertEquals("strCmd", "ls .", strCmd[1]);
		assertEquals("auth_file", objOptions.auth_file.Value(), "test");
		assertEquals("user", objOptions.user.Value(), "test");
	}

	@Test
	@Ignore("Test set to Ignore for later examination")
	public void testSimulateShellParam() throws Exception {
		initializeClazz();
		String strArgs[] = new String[] { "-command", "ls", "-auth_method", "password", "-host", "wilma.sos", "-port", "22", "-user", "test", "-password",
				"12345", "-simulate_shell", "true", "-simulate_shell_prompt_trigger", "test@192:~>", "-simulate_shell_login_timeout", "100000" };
		objOptions.CommandLineArgs(strArgs);
		objSSH.execute();
	}

	@Override
	public String replaceSchedulerVars(final boolean isWindows, final String pstrString2Modify) {
		String strTemp = pstrString2Modify;
		HashMap<String, String> objJobOrOrderParams = new HashMap<String, String>();
		objJobOrOrderParams.put("host", "wilma");
		objJobOrOrderParams.put("test", ".");
		objJobOrOrderParams.put("test1", "value_of_test1");

		if (isNotNull(objJobOrOrderParams)) {
			Set<String> paramNames = objJobOrOrderParams.keySet();
			String regExPattern = "(?i)";
			String regex = "(?i)"; // case insensitive
			//
			/**
			 * beides zulassen, % und $
			 * m�gliche Kombinationen sind:
			 *
			 * %SCHEDULER_PARAM_name%
			 * %name%
			 * ${SCHEDULER_PARAM_name}
			 * $SCHEDULER_PARAM_name
			 * �{SCHEDULER_PARAM_name}
			 * �SCHEDULER_PARAM_name
			 * ${name}
			 * �{name}
			 * $name
			 * �name
			 *
			 * Managed-DB:
			 *    �{...}
			 */
			String[] strPatterns = new String[] { "%%SCHEDULER_PARAM_%1$s%%", "%%%1$s%%", "(\\$|�)\\{?SCHEDULER_PARAM_%1$s\\}?", "(\\$|�)\\{?%1$s\\}?" };
			for (String strPattern : strPatterns) {
				regExPattern = strPattern;
				//				logger.debug("regExPattern = " + regExPattern);
				for (String name : paramNames) {
					String strParamValue = objJobOrOrderParams.get(name);
					if (name.contains("password") == false) {
						logger.debug("name = " + name + ", value = " + strParamValue);
					}
					regex = String.format(regExPattern, name);
					logger.debug("regex = " + regex + ", strParamValue = " + strParamValue);
					strTemp = myReplaceAll(strTemp, regex, strParamValue);
				}
				logger.debug("String after replace = " + strTemp);
			}
		}
		return strTemp;
	}

	/**
	 *
	 * \brief myReplaceAll
	 *
	 * \details
	 *
	 * \return String
	 *
	 * @param source
	 * @param what
	 * @param replacement
	 * @return
	 */
	@Override
	public String myReplaceAll(final String source, final String what, final String replacement) {
		String newReplacement = replacement.replaceAll("\\$", "\\\\\\$");
		return source.replaceAll("(?m)" + what, newReplacement);
	}

}
