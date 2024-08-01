package com.nexusforge.aggregator.service;

import com.nexusforge.user.UserInformation;
import com.nexusforge.user.UserInformationRequest;
import com.nexusforge.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userClient;

    public UserInformation getUserInformation(int userId) {
        var request = UserInformationRequest.newBuilder()
                .setUserId(userId)
                .build();
        log.info("{}", request);
        return this.userClient.getUserInformation(request);
    }

}
