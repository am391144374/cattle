package com.cattle.common.exception;

public class ProcessHandlerNullException extends RuntimeException {

    public ProcessHandlerNullException(String msg){
        super(msg);
    }

    public ProcessHandlerNullException(){
        super();
    }
}
