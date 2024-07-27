package com.nexusforge.user.service.advice;

import com.nexusforge.user.exception.InsufficientBalanceException;
import com.nexusforge.user.exception.InsufficientSharesException;
import com.nexusforge.user.exception.UnknownTickerException;
import com.nexusforge.user.exception.UnknownUserException;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ServiceExceptionHandler {

    @GrpcExceptionHandler(UnknownTickerException.class)
    public Status handleInvalidArguments(UnknownTickerException e) {
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(UnknownUserException.class)
    public Status handleUnknownEntitties(UnknownUserException e) {
        return Status.NOT_FOUND.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler({InsufficientBalanceException.class, InsufficientSharesException.class})
    public Status handlePreConditionFailure(Exception e) {
        return Status.FAILED_PRECONDITION
                .withDescription(e.getMessage());
    }

}
