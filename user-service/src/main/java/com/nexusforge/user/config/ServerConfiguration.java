package com.nexusforge.user.config;

import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/*
Optional configuration
* */

@Configuration
public class ServerConfiguration {

    @Bean
    public GrpcServerConfigurer serverConfigurer(){
        //settings -> thread pools, interceptors
        return serverBuilder -> serverBuilder
                .executor(Executors.newVirtualThreadPerTaskExecutor());
    }
}
