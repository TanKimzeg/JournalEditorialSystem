package top.tankimzeg.editorial_system.utils;

import lombok.Data;
import org.springframework.http.HttpStatus;
import top.tankimzeg.editorial_system.exception.BusinessException;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse(HttpStatus status, String message, T data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(int code, String message, T data) {
        this.status = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK, "success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK, message, data);
    }

    public static ApiResponse<?> failure(HttpStatus status, String message) {
        return new ApiResponse<>(status, message, null);
    }

    public static <T> ApiResponse<T> failure(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static ApiResponse<?> failure(String message) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, message, null);
    }

    public static ApiResponse<?> fromException(BusinessException ex) {
        return new ApiResponse<>(ex.getStatus(), ex.getMessage(), null);
    }

    public static ApiResponse<?> unauthorized(String message) {
        return new ApiResponse<>(HttpStatus.UNAUTHORIZED, message, null);
    }

    public static ApiResponse<?> forbidden(String message) {
        return new ApiResponse<>(HttpStatus.FORBIDDEN, message, null);
    }

    public static ApiResponse<?> badRequest(String message) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, message, null);
    }

    public static ApiResponse<?> internalServerError(String message) {
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }
}
