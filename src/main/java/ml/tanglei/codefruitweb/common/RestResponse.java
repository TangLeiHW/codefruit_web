package ml.tanglei.codefruitweb.common;

import java.io.Serializable;

/**
 *自定义响应结构
 */
public class RestResponse implements Serializable {

    //响应业务状态
    private Integer status;
    //响应消息
    private String msg;
    //响应中的数据
    private Object data;

    public static RestResponse build(Integer status, String msg, Object data) {
        return new RestResponse(status, msg, data);
    }

    public static RestResponse build(Integer status, String msg) {
        return new RestResponse(status, msg, null);
    }

    public static  RestResponse ok(Object data) {
        return new RestResponse(data);
    }

    public static RestResponse ok() {
        return new RestResponse(null);
    }

    public RestResponse() {

    }

    public RestResponse(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public RestResponse(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
