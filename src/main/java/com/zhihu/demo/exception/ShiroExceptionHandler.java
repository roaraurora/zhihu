package com.zhihu.demo.exception;

import com.zhihu.demo.result.CodeMsg;
import com.zhihu.demo.result.Result;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 使用ErrorController处理异常时 服务器端会打印异常的堆栈信息
 * 由JWTFilter即shiro的filter抛出的异常不属于controller抛出的异常 不会被RestControllerAdvice所捕获
 * 但这个异常会被Servlet容器捕获到 Servlet容器再将异常转发给注册好的异常处理映射 /error 做处理
 * 而Servlet容器打印了异常的堆栈信息 暂时没找到更好的办法阻止打印堆栈
 */
@RestController
public class ShiroExceptionHandler implements ErrorController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = "/error")
    public Result<Object> error(HttpServletResponse response, HttpServletRequest request) {
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        if (exception == null) {
            throw new GlobalException(new CodeMsg(response.getStatus(), "Exception send to ErrorController"));
        }
        Throwable cause = exception.getCause();
        if (cause instanceof AuthenticationException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return Result.error(CodeMsg.TOKEN_VERIFY_FALSE);
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return Result.error(CodeMsg.UNHANDLE_BEYOND_CONTROLLER_EXCEPTION);
    }
}
