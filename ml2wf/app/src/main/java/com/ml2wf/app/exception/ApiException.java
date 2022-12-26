package com.ml2wf.app.exception;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@Builder
public class ApiException {

    HttpStatus status;
    String message;
}
