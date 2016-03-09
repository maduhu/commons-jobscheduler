package sos.scheduler.interfaces;

/** \class IJobSchedulerMonitor_impl
 * 
 * \brief IJobSchedulerMonitor_impl -
 * 
 * \details
 *
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
 * \author KB \version $Id$ \see reference
 *
 * Created on 28.02.2012 12:52:23 */

/** @author KB */
// $Id: Monitor_impl.java 3946 2005-09-26 08:52:01Z jz $

/** Interface f�r die Implementierung eines Monitors
 *
 * @author Joacim Zschimmer, Zschimmer GmbH
 * @version $Revision: 3946 $ */

public interface IJobSchedulerMonitor_impl {

    /*
     * This Variables must come with the Base-Class Job_impl or directly
     * specified in the Adapter-Class due to it's impossible to specifiy
     * Variables in an interface (?) public Log spooler_log; public Task
     * spooler_task; public Job spooler_job; public Spooler spooler;
     */
    /*
     * protected Monitor_impl() { }
     */
    final static boolean continueWithProcess = true;
    final static boolean continueWithProcessBefore = true;
    final static boolean continueWithTaskAfter = false;

    public boolean spooler_task_before() throws Exception;

    /*
     * { return true; }
     */

    public void spooler_task_after() throws Exception;

    /*
     * { }
     */

    public boolean spooler_process_before() throws Exception;

    /*
     * { return true; }
     */

    public boolean spooler_process_after(boolean spooler_process_result) throws Exception;
    /*
     * { return spooler_process_result; }
     */
}
