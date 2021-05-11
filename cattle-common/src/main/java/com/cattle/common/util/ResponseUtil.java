package com.cattle.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ResponseUtil {

    private int code;
    private String message;
    private Object data;

    public static ResponseUtil defaultSuccess(Object data){
        return new ResponseUtil(0,"success",data);
    }

    public static ResponseUtil fail(int code,String message,Object data){
        return new ResponseUtil(code,message,data);
    }

    public static ResponseUtil success(int code,String message,Object data){
        return new ResponseUtil(code,message,data);
    }

    public static ResponseUtil defaultFail(String message){
        return new ResponseUtil(-1,message,"");
    }

}
