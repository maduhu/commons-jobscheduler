package com.sos.scheduler.cmd;

import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdRemoveOrder;
import org.apache.log4j.Logger;

/** @uthor ss at 17.09.13 19:52 */
public class JSRemoveOrder extends JSCommand {

    private final Logger logger = Logger.getLogger(JSRemoveOrder.class);

    @SuppressWarnings("unused")
    private final JSCmdRemoveOrder cmdRemoveOrder;

    public JSRemoveOrder(String host, Integer port, String orderId, String jobChain) {
        super(host, port);
        this.cmdRemoveOrder = setCommand(orderId, jobChain);
    }

    public JSRemoveOrder(SchedulerObjectFactory factory, String orderId, String jobChain) {
        super(factory);
        this.cmdRemoveOrder = setCommand(orderId, jobChain);
    }

    private JSCmdRemoveOrder setCommand(String orderId, String jobChain) {
        JSCmdRemoveOrder cmd = getFactory().createRemoveOrder();
        cmd.setJobChain(jobChain);
        cmd.setOrder(orderId);
        setCommand(cmd);
        logger.info("Remove order for jobchain " + jobChain + " with orderId " + orderId);
        return cmd;
    }

}
