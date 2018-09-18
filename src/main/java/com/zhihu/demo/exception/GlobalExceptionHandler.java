package com.zhihu.demo.exception;

import com.zhihu.demo.result.CodeMsg;
import com.zhihu.demo.result.Result;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice //可以指定范围
public class GlobalExceptionHandler {

    /**
     * 当用户没有权限(Authentication / permission / role)时被导向此处
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public Result<String> handle401(ShiroException e) {
        return Result.error(new CodeMsg(500502, e.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        //todo 这里的状态码有点多 希望能找到更好的方式处理响应报文的状态码
        e.printStackTrace(); //打印堆栈
        if (e instanceof GlobalException) {
            GlobalException exception = (GlobalException) e;
            return Result.error(exception.getCodeMsg());
        } else if (e instanceof BindException) {
            return Result.error(CodeMsg.VO_ERR);
        } else if (e instanceof HttpMessageNotReadableException) {
            return Result.error(CodeMsg.MISSING_PARAM);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return Result.error(CodeMsg.UNSUPPORTED_METHOD);
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
