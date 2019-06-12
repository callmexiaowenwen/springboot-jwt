package com.gemii.auth.exception;

/**
 * @ClassName LoginFailedException
 * @Description TODO
 * @Author wen.liu
 * @Date 2019-06-10 18:10
 **/
public class LoginFailedException extends RuntimeException {


    private String exceptionCode;

    private String exceptionDesc;


    public LoginFailedException(String exceptionCode, String exceptionDesc) {
        super();
        this.exceptionCode = exceptionCode;
        this.exceptionDesc = exceptionDesc;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getExceptionDesc() {
        return exceptionDesc;
    }

    public void setExceptionDesc(String exceptionDesc) {
        this.exceptionDesc = exceptionDesc;
    }


}
