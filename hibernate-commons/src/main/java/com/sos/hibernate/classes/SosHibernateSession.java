package com.sos.hibernate.classes;

import com.sos.hibernate.interfaces.IHibernateOptions;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

//import javax.print.attribute.standard.Finishings;

/**
 * \class SosHibernateSession
 * 
 * \brief SosHibernateSession -
 * 
 * \details
 * 
 * \section SosHibernateSession.java_intro_sec Introduction
 * 
 * \section SosHibernateSession.java_samples Some Samples
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
 * \author Uwe Risse \version 02.02.2012 \see reference
 * 
 * Created on 02.02.2012 16:08:04
 */
public class SosHibernateSession {
    private static Session        session           = null;
    private static SessionFactory sessionFactory    = null;
    public static File            configurationFile = null;

    protected SosHibernateSession() {
        // no Instances
    }

    public static Session getInstance(final File configurationFile) {
        if (session == null) {
            try {
                Configuration configuration = getConfiguration(getDefaultClassMapping());
                configuration.configure(configurationFile);
                openSession(configuration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            session.clear();
        }
        return session;
    }

    public static Session getInstance(IHibernateOptions options) {
        if (session == null) {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            try {
                Configuration configuration = getConfiguration(getDefaultClassMapping());
                configuration.setProperty("hibernate.connection.url", options.gethibernate_connection_url().Value());
                configuration.setProperty("hibernate.connection.password", options.gethibernate_connection_password().Value());
                configuration.setProperty("hibernate.connection.url", options.gethibernate_connection_url().Value());
                configuration.setProperty("hibernate.connection.username", options.gethibernate_connection_username().Value());
                configuration.setProperty("hibernate.dialect", options.gethibernate_dialect().Value());
                configuration.setProperty("hibernate.show_sql", options.gethibernate_show_sql().Value());
                configuration.setProperty("hibernate.connection.autocommit", options.gethibernate_connection_autocommit().Value());
                configuration.setProperty("hibernate.format_sql", options.gethibernate_format_sql().Value());
                openSession(configuration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            session.clear();
        }
        return session;
    }

    private static ClassList getDefaultClassMapping() {
        ClassList classList = new ClassList();
        classList.addClassIfExist("com.sos.dailyschedule.db.DailyScheduleDBItem");
        classList.addClassIfExist("com.sos.scheduler.history.db.SchedulerTaskHistoryDBItem");
        classList.addClassIfExist("com.sos.scheduler.history.db.SchedulerOrderStepHistoryDBItem");
        classList.addClassIfExist("com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem");
        classList.addClassIfExist("com.sos.scheduler.db.SchedulerInstancesDBItem");
        classList.addClassIfExist("sos.ftphistory.db.JadeFilesDBItem");
        classList.addClassIfExist("sos.ftphistory.db.JadeFilesHistoryDBItem");
        classList.addClassIfExist("com.sos.eventing.db.SchedulerEventDBItem");
        classList.addClassIfExist("com.sos.jobnet.db.EventsDBItem");
        classList.addClassIfExist("com.sos.jobnet.db.JobNetPlanDBItem");
        classList.addClassIfExist("com.sos.jobnet.db.JobNetNodeDBItem");
        classList.addClassIfExist("com.sos.jobnet.db.JobNetEdgesDBItem");
        classList.addClassIfExist("com.sos.jobnet.db.JobNetHistoryDBItem");
        classList.addClassIfExist("com.sos.jobnet.db.JobNetCmdHistoryDBItem");
        classList.addClassIfExist("com.sos.jobnet.db.JobNetDBItem");

        classList.addClassIfExist("com.sos.auth.shiro.db.SOSUserDBItem");
        classList.addClassIfExist("com.sos.auth.shiro.db.SOSUserRightDBItem");
        classList.addClassIfExist("com.sos.auth.shiro.db.SOSUserRoleDBItem");
        classList.addClassIfExist("com.sos.auth.shiro.db.SOSUser2RoleDBItem");
        
        
        classList.addClassIfExist("com.sos.tools.logback.db.LoggingEventDBItem");
        classList.addClassIfExist("com.sos.tools.logback.db.LoggingEventExceptionDBItem");
        classList.addClassIfExist("com.sos.tools.logback.db.LoggingEventPropertyDBItem");
          
        return classList;
    }

    private static Configuration getConfiguration(ClassList forClasses) {
        Configuration configuration = new Configuration();
        for(Class c : forClasses.getClasses()) {
            configuration.addAnnotatedClass(c);
        }
        return configuration;
    }

    private static void openSession(Configuration configuration) {
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
        session.doWork(new Work() {
            public void execute(Connection connection) throws SQLException {
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                connection.setAutoCommit(false);
            }
        });
        session.setFlushMode(FlushMode.ALWAYS);
    }

    public static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (session != null) {
            session.close();
        }
        session = null;
        sessionFactory = null;
    }
}