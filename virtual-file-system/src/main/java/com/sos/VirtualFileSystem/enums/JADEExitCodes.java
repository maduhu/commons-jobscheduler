package com.sos.VirtualFileSystem.enums;

import com.sos.VirtualFileSystem.common.SOSMsgVfs;

/** @author KB */
public enum JADEExitCodes {
    virtualFileSystemError(98, "SOSVfs_T_0310"), someUnspecificError(99, "SOSVfs_T_0310"), UnsupportedParametersFound(15, "SOSVfs_T_0310"),
    ParametersMissingButRequired(14, "SOSVfs_T_0310"), connectionError(13, "SOSVfs_T_0310"), authenticatenError(12, "SOSVfs_T_0310"), nListError(11,
            "SOSVfs_T_0310");

    public String description;
    public String MsgCode;
    public int ExitCode;

    public String getText() {
        return description;
    }

    private JADEExitCodes(final int pintExitCode, final String name) {
        String k;
        if (name == null) {
            k = this.name();
        } else {
            k = new SOSMsgVfs(name).get();
            MsgCode = name;
        }
        description = k;
        ExitCode = pintExitCode;
    }

    public static String[] getArray() {
        String[] strA = new String[JADEExitCodes.values().length];
        int i = 0;
        for (JADEExitCodes enuType : JADEExitCodes.values()) {
            strA[i++] = enuType.description;
        }
        return strA;
    }

}