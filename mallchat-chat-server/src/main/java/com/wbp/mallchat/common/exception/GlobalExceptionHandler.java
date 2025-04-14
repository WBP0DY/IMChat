package com.wbp.mallchat.common.exception;

import com.wbp.mallchat.common.domain.vo.resp.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    private ApiResult methodArgumentNotValidException (MethodArgumentNotValidException e) {
        StringBuilder errorMsg = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(x->errorMsg.append(x.getDefaultMessage()).append(","));
        String message = errorMsg.toString();
        log.error("validation parameters error！The reason is:{}", e);
        return ApiResult.fail(CommonErrorEnum.PARAM_VALID.getErrorCode(), message.substring(0, message.length()-1));
    }

    @ExceptionHandler({BusinessException.class})
    private ApiResult businessException (BusinessException e) {
        String message = e.getMessage();
        log.error("validation business error！The reason is:{}", e);
        return ApiResult.fail(BusinessErrorEnum.BUSINESS_ERROR.getErrorCode(), message);
    }

    @ExceptionHandler({Throwable.class})
    private ApiResult throwable (Throwable e) {
        String message = e.getMessage();
        log.error("validation parameters error！The reason is:{}", e);
        return ApiResult.fail(CommonErrorEnum.SYSTEM_ERROR.getErrorCode(), message);
    }

}
