package com.cattle.common.enums;

public enum JobStatus {

    RUNNING("running"),
    /** 错误中断 */
    INTERRUPT("interrupt"),
    FINISH("finish");

    private String name;

    JobStatus(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

}
