package com.sos.JSHelper.interfaces;

import com.sos.JSHelper.Options.JSOptionsClass;

public interface IJadeEngine extends Runnable {

    public abstract boolean execute() throws Exception;

    public abstract int getCC();

    public abstract String getState();

    public abstract void logout();

    public abstract void setJadeOptions(JSOptionsClass pobjOptions);

    @Override
    public abstract void run();

}