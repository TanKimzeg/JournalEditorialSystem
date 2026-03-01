package top.tankimzeg.editorial_system.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import top.tankimzeg.editorial_system.utils.ApiResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kim
 * @date 2026/1/21 16:48
 * @description 全局异常处理器
 */
@Hidden
@RestControllerAdvice()
@Slf4j
public class GlobalExceptionHandler {

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleBusinessException(BusinessException ex) {

        return ApiResponse.fromException(ex);
    }

    // 处理参数验证异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ApiResponse.failure(HttpStatus.BAD_REQUEST, "参数验证失败", errors);
    }

    // 处理JSR303验证异常
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<?> handleConstraintViolationException(ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }

        return ApiResponse.failure(HttpStatus.BAD_REQUEST, "参数验证失败", errors);
    }

    // 处理认证异常
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ApiResponse<?> handleAuthenticationException(Exception ex) {

        log.warn("Authentication failed", ex);
        return ApiResponse.unauthorized("认证失败");
    }

    // 处理权限不足异常
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<?> handleAccessDeniedException(AccessDeniedException ex) {

        log.warn("Access denied", ex);
        return ApiResponse.forbidden("权限不足");
    }

    // 处理参数类型不匹配异常
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {

        Class<?> requiredType = ex.getRequiredType();
        String requiredTypeName = requiredType != null ? requiredType.getSimpleName() : "未知类型";
        String error = String.format("参数 '%s' 类型错误，期望类型: %s",
                ex.getName(), requiredTypeName);

        return ApiResponse.badRequest(error);
    }

    // 处理其他所有异常
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleAllUncaughtException(Exception ex) {

        log.error("Unhandled exception caught by GlobalExceptionHandler", ex);
        return ApiResponse.internalServerError("服务器内部错误，请稍后重试");
    }
}
