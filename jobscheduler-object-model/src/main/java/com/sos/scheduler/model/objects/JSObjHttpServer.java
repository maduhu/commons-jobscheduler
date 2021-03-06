package com.sos.scheduler.model.objects;

import org.apache.log4j.Logger;

import com.sos.scheduler.model.SchedulerObjectFactory;

/** \class JSObjHttpServer
 * 
 * \brief JSObjHttpServer -
 * 
 * \details
 *
 * \section JSObjHttpServer.java_intro_sec Introduction
 *
 * \section JSObjHttpServer.java_samples Some Samples
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
 *          Created on 09.02.2011 14:33:22 */

/** @author oh */
public class JSObjHttpServer extends HttpServer {

    @SuppressWarnings("unused")
    private final String conClassName = "JSObjHttpServer";
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(JSObjHttpServer.class);

    public JSObjHttpServer(SchedulerObjectFactory schedulerObjectFactory) {
        super();
        objFactory = schedulerObjectFactory;
    }
}
