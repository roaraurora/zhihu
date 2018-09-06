package com.zhihu.demo.exception;

import com.zhihu.demo.result.CodeMsg;

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
