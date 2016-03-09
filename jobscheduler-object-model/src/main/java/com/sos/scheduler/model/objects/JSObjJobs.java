package com.sos.scheduler.model.objects;

import org.apache.log4j.Logger;

import com.sos.scheduler.model.SchedulerObjectFactory;

/** \class JSObjJobs
 * 
 * \brief JSObjJobs -
 * 
 * \details
 *
 * \section JSObjJobs.java_intro_sec Introduction
 *
 * \section JSObjJobs.java_samples Some Samples
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
 *          Created on 09.02.2011 14:45:09 */

/** @author oh */
public class JSObjJobs extends Jobs {

    @SuppressWarnings("unused")
    private final String conClassName = "JSObjJobs";
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(JSObjJobs.class);

    public JSObjJobs(SchedulerObjectFactory schedulerObjectFactory) {
        super();
        objFactory = schedulerObjectFactory;
    }
}
