package com.zhihu.demo.result;

public class Result<T> {
    private int code;
    private String msg;
    private T data;

    private Result(CodeMsg codeMsg) {
        if (codeMsg == null) {
            return;
        }
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }

    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        //成功的时候调用 data为返回的数据
        return new Result<>(data);
    }

    public static <T> Result<T> error(CodeMsg codeMsg) {
        //失败的时候调用 传入一个codeMsg对象 可以是常量也可以自定义
        return new Result<>(codeMsg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
