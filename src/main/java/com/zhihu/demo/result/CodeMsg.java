package com.zhihu.demo.result;

public class CodeMsg {
    private int code;
    private String msg;

    public static final CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static final CodeMsg SERVER_ERROR = new CodeMsg(500100, "server error");
    public static final CodeMsg UNAUTHORIZED = new CodeMsg(500401, "unauthorized error"); //未认证错误
    //    public static final CodeMsg SHIRO_ERROR = new CodeMsg(500402,"shiro exception"); //shiro异常
    public static final CodeMsg TOKEN_VERIFY_FALSE = new CodeMsg(500403, "token validation failed"); //token验证失败
    public static final CodeMsg USER_NOT_FOUND = new CodeMsg(500404, "unable to find user match input username"); //未找到用户名对应的用户
    public static final CodeMsg UNHANDLE_BEYOND_CONTROLLER_EXCEPTION = new CodeMsg(500405, "this error stand for those exception handle by /error"); //未被controllerAdvice捕获的异常 同时不是定义在MyShiroRealm中抛出的AuthenticationException
    public static final CodeMsg PASSWORD_ERR = new CodeMsg(500406, "user login password not correct"); //用户密码错误
    public static final CodeMsg VO_ERR = new CodeMsg(500407, "user vo error"); //请求数据有误 不符合vo校验规则
    public static final CodeMsg ROLE_NOT_FOUND = new CodeMsg(500408, "user's role is not found"); //未找到用户的系统角色

    // by sun
    public static final CodeMsg INSERT_COMMENT_ERROR = new CodeMsg(700401, "insert comment error"); //未找到用户的系统角色
    public static final CodeMsg COMMENT_IS_NULL = new CodeMsg(700402, "comment is null"); //未找到用户的系统角色
    public static final CodeMsg DELETE_COMMENT_ID_ERROR_ = new CodeMsg(700403, "comment is null"); //未找到用户的系统角色
    public static final CodeMsg DELETE_COMMENT_ERROR = new CodeMsg(700403, "comment is null"); //未找到用户的系统角色
    public static final CodeMsg MODIFY_COMMENT_PNUM_ERROR = new CodeMsg(700403, "comment is null"); //未找到用户的系统角色
    public static final CodeMsg MODIFY_ID_ERROR = new CodeMsg(700403, "comment is null"); //未找到用户的系统角色

    public static final CodeMsg INSERT_QUESTION_ERROR = new CodeMsg(700401, "insert comment error"); //未找到用户的系统角色
    public static final CodeMsg QUESTION_IS_NULL = new CodeMsg(700402, "comment is null"); //未找到用户的系统角色
    public static final CodeMsg DELETE_QUESTION_ID_ERROR_ = new CodeMsg(700403, "comment is null"); //未找到用户的系统角色
    public static final CodeMsg DELETE_QUESTION_ERROR = new CodeMsg(700403, "comment is null"); //未找到用户的系统角色
    public static final CodeMsg MODIFY_QUESTOPM_ID_ERROR = new CodeMsg(700403, "comment is null"); //未找到用户的系统角色
    public static final CodeMsg MODIFY_QUESTION_ERROR = new CodeMsg(700403, "comment is null"); //未找到用户的系统角色






    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
