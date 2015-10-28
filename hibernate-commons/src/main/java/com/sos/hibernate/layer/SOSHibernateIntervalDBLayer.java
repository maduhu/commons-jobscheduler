package com.sos.hibernate.layer;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.SOSHibernateIntervalFilter;

/**
 * \class SOSHibernateDBLayer
 * 
 * \brief SOSHibernateDBLayer -
 * 
 * \details
 * 
 * \section SOSHibernateDBLayer.java_intro_sec Introduction
 * 
 * \section SOSHibernateDBLayer.java_samples Some Samples
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
 * \author Uwe Risse \version 06.10.2011 \see reference
 * 
 * Created on 06.10.2011 14:23:43
 */

public abstract class SOSHibernateIntervalDBLayer extends SOSHibernateDBLayer {
	
    private Logger logger = Logger.getLogger(SOSHibernateIntervalFilter.class);

    public abstract SOSHibernateIntervalFilter getFilter();

    public abstract void onAfterDeleting(DbItem h);

    public abstract List<DbItem> getListOfItemsToDelete();

    public abstract long deleteInterval();

    public SOSHibernateIntervalDBLayer() {
        super();
    }

    public long deleteInterval(int interval, int limit) {
        long deleted = 0;

        if (limit == 0) {
            GregorianCalendar to = new GregorianCalendar();
            to.add(GregorianCalendar.DAY_OF_YEAR, -interval);
            this.getFilter().setIntervalFrom(null);
            this.getFilter().setIntervalTo(to.getTime());
            try{
               deleted = deleteInterval();
            }catch (Exception e){
            	e.printStackTrace();
        	}
        } else {
            if (connection == null){
            	initConnection(getConfigurationFileName());
            }
            if (interval > 0) {
                GregorianCalendar to = new GregorianCalendar();
                to.add(GregorianCalendar.DAY_OF_YEAR, -interval);
                this.getFilter().setLimit(limit);
                this.getFilter().setIntervalFrom(null);
                this.getFilter().setIntervalTo(to.getTime());
                List<DbItem> listOfDBItems = this.getListOfItemsToDelete();

                Iterator<DbItem> dbitemEntries = listOfDBItems.iterator();
                int i = 0;
                while (dbitemEntries.hasNext()) {
                    DbItem h = (DbItem) dbitemEntries.next();
                    try {
                    	connection.connect();
    					connection.beginTransaction();
						connection.delete(h);
            			connection.commit();
					} catch (Exception e) {
						logger.error("Error occurred connecting to DB: ", e);
					}
                    this.onAfterDeleting(h);
                    deleted = deleted + 1;
                    i = i + 1;
                    if (i == limit) {
                        i = 0;
                    }
                }
            }
        }
        return deleted;
    }

    public long deleteInterval(int interval) {
        return deleteInterval(interval, 300);
    }

}
