package com.ktr.exception;

public class ProcessHandlerNullException extends RuntimeException {

    public ProcessHandlerNullException(String msg){
        super(msg);
    }

    public ProcessHandlerNullException(){
        super();
    }
}
