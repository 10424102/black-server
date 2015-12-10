package org.team10424102.blackserver.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.team10424102.blackserver.Api;
import org.team10424102.blackserver.exceptions.BusinessException;
import org.team10424102.blackserver.exceptions.SystemException;

@ControllerAdvice
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ExceptionHandlingController {
    static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @Autowired ApplicationContext context;

    @ExceptionHandler(SystemException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "服务器发生内部错误, 即将关闭")
    public void handleSystemError(SystemException e) {
        logger.error("严重系统错误, 即将关闭服务器", e);
        ((ConfigurableApplicationContext)context).close();
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Api.Result handleBusinessException(BusinessException exception) {
        return Api.error(exception);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "请求参数不完整")
    public void handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        logger.warn("请求参数不完整", e);
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "您的请求已经傲娇~~")
//    public void handleException(Exception e) {
//        logger.warn("服务器发生异常，这种消息怎么可以外泄！", e);
//    }



}
