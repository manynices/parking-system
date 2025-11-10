package com.experiment.parkingsystem.exception;

import com.experiment.parkingsystem.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理资源未找到异常 (HTTP 404)
     * @param ex 捕获到的 ResourceNotFoundException
     * @return 返回封装了错误信息的 ResponseEntity
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage()); // 记录警告级别的日志

        ApiResponse<Object> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理所有未被特定方法捕获的其他异常 (HTTP 500)
     * 这是最后的防线，确保任何服务器内部错误都能以标准格式返回，而不是抛出堆栈信息。
     * @param ex 捕获到的通用 Exception
     * @return 返回封装了错误信息的 ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        log.error("An unexpected error occurred:", ex); // 记录错误级别的日志，并附上异常堆栈

        ApiResponse<Object> response = new ApiResponse<>();
        response.setSuccess(false);
        // 为了安全，不将详细的 ex.getMessage() 返回给客户端，以防泄露系统内部信息
        response.setMessage("服务器内部错误，请联系管理员");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}