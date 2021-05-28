package com.cattle.common.enums;

public enum JobStatus {

    //已经创建
    CREATE("create"),
    RUNNING("running"),
    /** 错误中断 */
    INTERRUPT("interrupt"),
    WAIT("wait"),
    FINISH("finish");

    private String name;

    JobStatus(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

}
