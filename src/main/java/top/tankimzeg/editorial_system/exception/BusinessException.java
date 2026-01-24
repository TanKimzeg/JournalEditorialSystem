package top.tankimzeg.editorial_system.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Kim
 * @date 2026/1/21 16:11
 * @description 自定义业务异常类
 */
@Getter
public class BusinessException extends RuntimeException{

    private final HttpStatus status;

    private final int code;

    public BusinessException(HttpStatus status, int code, String message){
        super(message);
        this.status = status;
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.code = HttpStatus.BAD_REQUEST.value();
    }

    public BusinessException(HttpStatus status, String message){
        super(message);
        this.status = status;
        this.code = status.value();
    }

    public BusinessException(int code, String message){
        super(message);
        this.code = code;
        this.status = HttpStatus.valueOf(code);
    }

}
