package com.experiment.parkingsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 自定义异常，用于表示请求的资源不存在。
 * 当此异常从控制器中抛出且未被捕获时，
 * Spring 将其映射为 HTTP 404 Not Found 状态码。
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * 构造函数。
     * @param message 异常的详细信息，将作为错误响应的一部分返回给客户端。
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}