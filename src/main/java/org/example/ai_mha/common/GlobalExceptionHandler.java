package org.example.ai_mha.common;

import org.example.ai_mha.exception.BusinessException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handlerException(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(","));

        return Result.error(ResultCode.PARAM_ERROR.getCode(), ResultCode.PARAM_MISSING.getMsg(), message);
    }

    //处理业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handlerException(BusinessException e){
        //如果异常携带额外的数据
        if(e.getData() != null){
            return Result.error(e.getCode(), e.getMessage(), e.getData());
        }
        //如果异常没有携带额外的数据
        else{
            return Result.error(e.getCode(), e.getMessage(),null);
        }
    }
}
