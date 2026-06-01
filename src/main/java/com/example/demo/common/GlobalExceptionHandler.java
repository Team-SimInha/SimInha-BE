package com.example.demo.common;

import com.example.demo.service.AuthException;
import com.example.demo.service.ReportNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuth(AuthException e) {
        HttpStatus status = switch (e.getCode()) {
            case "USERNAME_TAKEN" -> HttpStatus.CONFLICT;
            case "INVALID_CREDENTIALS", "UNAUTHORIZED" -> HttpStatus.UNAUTHORIZED;
            default -> HttpStatus.BAD_REQUEST;
        };
        return ResponseEntity.status(status).body(ApiResponse.fail(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleReportNotFound(ReportNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail("REPORT_NOT_FOUND", e.getMessage()));
    }
}
