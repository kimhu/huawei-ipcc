/**
 * 
 */
package com.eiisys.ipcc.exception;

/**
 * 业务异常类<br>
 * 所有Service抛出的运行时异常以此为标准
 */
public class IpccException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String code;
    private String[] args;
//    private String msg;
    
    protected IpccException () {
        super();
    }
    
    public IpccException (String code, String... args) {
        super(code);
        this.code = code;
        this.args = args;
    }
    
//    public IpccException (String code, String msg) {
//        super(code);
//        this.code = code;
//        this.msg = msg;
//    }
    
    
    public String getCode() {
        return code;
    }
    
    public String[] getArgs() {
        return args;
    }

//    public String getMsg() {
//        return msg;
//    }

}
