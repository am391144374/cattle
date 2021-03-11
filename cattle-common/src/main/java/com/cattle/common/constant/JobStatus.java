package com.cattle.common.constant;

public interface JobStatus {

    enum Status{
        RUNNING("running"),
        /** 错误中断 */
        INTERRUPT("interrupt"),
        FINISH("finish");

        private String name;

        Status(String name){
            this.name = name;
        }

        public String getName(){
            return this.name;
        }
    }

}
