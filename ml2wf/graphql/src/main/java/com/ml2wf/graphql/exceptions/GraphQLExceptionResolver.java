package com.ml2wf.graphql.exceptions;

import com.ml2wf.contract.exception.ConfigurationNotFoundException;
import com.ml2wf.contract.exception.NoVersionFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.NonNull;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class GraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

    // TODO: factorize with ExceptionHandlerController
    private static final String CONFIGURATION_NOT_FOUND_MESSAGE_PATTERN = "Configuration with name %s not found.";
    private static final String UNEXPECTED_ERROR = "Unexpected error while fetching data.";
    private static final String NO_VERSION_FOUND_MESSAGE_PATTERN = "No version found.";

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {
        // TODO: to improve
        if (ex instanceof ConfigurationNotFoundException cnfe) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.NOT_FOUND)
                    .message(String.format(CONFIGURATION_NOT_FOUND_MESSAGE_PATTERN,
                            cnfe.getConfigurationName()))
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }
        if (ex instanceof NoVersionFoundException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(NO_VERSION_FOUND_MESSAGE_PATTERN)
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.INTERNAL_ERROR)
                .message(UNEXPECTED_ERROR)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }
}