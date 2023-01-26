package com.ml2wf.app.controllers;

import com.ml2wf.app.exception.ApiException;
import com.ml2wf.contract.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    private static final String VARIABLE_TASK_NOT_FOUND_MESSAGE_PATTERN = "Unknown reference to task %s in constraints.";
    private static final String VERSION_NOT_FOUND_MESSAGE_PATTERN = "Version %s not found.";
    private static final String CONFIGURATION_NOT_FOUND_MESSAGE_PATTERN = "Configuration with name %s not found.";
    private static final String NO_VERSION_FOUND_MESSAGE_PATTERN = "No version found.";
    private static final String DUPLICATED_VERSION_NAME_MESSAGE_PATTERN = "Version with name %s already exists.";

    @ExceptionHandler(value = {VariableTaskNotFoundException.class})
    public ResponseEntity<ApiException> variableTaskNotFoundExceptionHandler(VariableTaskNotFoundException exception) {
        // TODO: use aspect for logging
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(String.format(VARIABLE_TASK_NOT_FOUND_MESSAGE_PATTERN, exception.getOperand().getValue()))
                        .build()
                );
    }

    @ExceptionHandler(value = {VersionNotFoundException.class})
    public ResponseEntity<ApiException> versionNotFoundExceptionHandler(VersionNotFoundException exception) {
        // TODO: use aspect for logging
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(String.format(VERSION_NOT_FOUND_MESSAGE_PATTERN, exception.getVersionName()))
                        .build()
                );
    }

    @ExceptionHandler(value = {NoVersionFoundException.class})
    public ResponseEntity<ApiException> noVersionFoundExceptionHandler(NoVersionFoundException exception) {
        // TODO: use aspect for logging
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(NO_VERSION_FOUND_MESSAGE_PATTERN)
                        .build()
                );
    }

    @ExceptionHandler(value = {DuplicatedVersionNameException.class})
    public ResponseEntity<ApiException> duplicatedVersionNameExceptionHandler(DuplicatedVersionNameException exception) {
        // TODO: use aspect for logging
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(String.format(DUPLICATED_VERSION_NAME_MESSAGE_PATTERN, exception.getVersionName()))
                        .build()
                );
    }

    @ExceptionHandler(value = {ConfigurationNotFoundException.class})
    public ResponseEntity<ApiException> configurationNotFoundExceptionHandler(ConfigurationNotFoundException exception) {
        // TODO: use aspect for logging
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(String.format(CONFIGURATION_NOT_FOUND_MESSAGE_PATTERN, exception.getConfigurationName()))
                        .build()
                );
    }
}
