package com.ming.admin.exception;

import com.ming.admin.util.Ajax;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandling {
    @ExceptionHandler(value = java.sql.SQLIntegrityConstraintViolationException.class)
    public Ajax SQLIntegrityConstraintViolationExceptionController(Exception e){
        return Ajax.error(e.getMessage());
    }
}
