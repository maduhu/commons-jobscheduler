package com.sos.dialog.components;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

/** @author Uwe Risse */
public class SOSDateTime extends DateTime {

    private Date date;
    private int style;

    public SOSDateTime(Composite parent, int style) {
        super(parent, style);
        addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                setInternalDate();
            }
        });
    }

    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    private void setInternalDate() {
        Calendar c = Calendar.getInstance();
        c.set(super.getYear(), super.getMonth(), super.getDay(), super.getHours(), super.getMinutes(), super.getSeconds());
        date = c.getTime();
    }

    public Date getDate(String sDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        d = formatter.parse(sDate);
        return d;
    }

    public Date getTime(String sTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date d = new Date();
        try {
            d = formatter.parse(sTime);
        } catch (ParseException e) {
            formatter = new SimpleDateFormat("HH:mm");
            try {
                d = formatter.parse(sTime);
            } catch (ParseException e1) {
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    d = formatter.parse(sTime);
                } catch (ParseException e2) {
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    d = formatter.parse(sTime);
                }
            }
        }
        return d;
    }

    public String getISODate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public String getISOTime() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(date);
    }

    public int getYear() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public int getMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH);
    }

    public int getDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR);
    }

    public int getMinute() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    public int getSecond() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.SECOND);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        super.setDate(getYear(), getMonth(), getDay());
    }

    public void setTime(Date date) {
        this.date = date;
        super.setTime(getHour(), getMinute(), getSecond());
    }

    public void setDate(String date) throws ParseException {
        if (!"".equals(date)) {
            this.setDate(getDate(date));
        } else {
            this.setDate(new Date());
        }
    }

    public void setTime(String time) throws ParseException {
        if (!"".equals(time)) {
            this.setTime(getTime(time));
        } else {
            this.setTime("00:00:00");
        }
    }

}