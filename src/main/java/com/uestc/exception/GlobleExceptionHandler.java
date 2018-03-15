package com.uestc.exception;

import com.uestc.result.CodeMsg;
import com.uestc.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@ControllerAdvice
@ResponseBody
public class GlobleExceptionHandler {

    @ExceptionHandler(value = Exception.class)//指定异常拦截的类型
    public Result<String> exceptionHandler(HttpServletRequest request, Exception exception) {
        //GlobleException是自己定义的一个异常类，将service层的错误以这种异常的形式抛出，在这里被拦截
        if (exception instanceof GlobleException) {
            GlobleException globleException = (GlobleException) exception;
            return Result.error(globleException.getCodeMsg());
        } else if (exception instanceof BindException) { //如果手机号码格式错误，会抛出这个异常
            BindException bindException = (BindException) exception;
             List<ObjectError> errors = bindException.getAllErrors();
             String message = errors.get(0).getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(message));
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
