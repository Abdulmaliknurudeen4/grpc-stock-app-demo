package com.nexusforge.user.service;

import com.nexusforge.user.*;
import com.nexusforge.user.service.handler.UserInformationRequestHandler;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

//RestController

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase{

    private final UserInformationRequestHandler userInformationRequestHandler;

    public UserService(UserInformationRequestHandler userInformationRequestHandler) {
        this.userInformationRequestHandler = userInformationRequestHandler;
    }

    @Override
    public void getUserInformation(UserInformationRequest request, StreamObserver<UserInformation> responseObserver) {
       var userInformation = this.userInformationRequestHandler.getUserInformation(request);
       responseObserver.onNext(userInformation);
       responseObserver.onCompleted();
    }

    @Override
    public void tradeStock(StockTradeRequest request, StreamObserver<StockTradeResponse> responseObserver) {
        super.tradeStock(request, responseObserver);
    }
}
