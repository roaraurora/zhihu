package com.zhihu.demo.exception;

import com.zhihu.demo.result.CodeMsg;

/**
 * @author 邓超
 * @description 自定义全局异常
 * @create 2018/9/13
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
