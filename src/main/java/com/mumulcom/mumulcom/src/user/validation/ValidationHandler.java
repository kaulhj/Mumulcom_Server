package com.mumulcom.mumulcom.src.user.validation;


import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<BaseResponseStatus> handleValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        FieldError fieldError = bindingResult.getFieldErrors().get(0);
        String field = fieldError.getField();
        String code = fieldError.getCode();
        assert code != null;
        switch (field) {
            case "email":
                if (code.equals("NotBlank")) {
                    return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_EMAIL);
                } else if (code.equals("Email")) {
                    return new BaseResponse<>(BaseResponseStatus.POST_USERS_INVALID_EMAIL);
                }
                break;
            case "nickname":
                if (code.equals("Size")) {
                    return new BaseResponse<>(BaseResponseStatus.POST_USERS_SIZE_NICKNAME);
                } else if (code.equals("Pattern")) {
                    return new BaseResponse<>(BaseResponseStatus.POST_USERS_INVALID_NICKNAME);
                }
                break;
            case "name":
                if (code.equals("NotBlank")) {
                    return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_NAME);
                }
                break;
            case "group":
                if (code.equals("NotBlank")) {
                    return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_GROUP);
                }
                break;
        }
        return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR);
    }

}
