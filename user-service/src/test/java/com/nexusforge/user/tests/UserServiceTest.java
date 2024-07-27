package com.nexusforge.user.tests;

import com.nexusforge.common.Ticker;
import com.nexusforge.user.StockTradeRequest;
import com.nexusforge.user.TradeAction;
import com.nexusforge.user.UserInformationRequest;
import com.nexusforge.user.UserServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "grpc.server.port=-1",
        "grpc.server.in-process-name=integration-test",
        "grpc.client.user-service.address=in-process:integration-test"
})
public class UserServiceTest {
    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub stub;

    @Test
    public void userInformationTest(){
        var request = UserInformationRequest.newBuilder()
                .setUserId(1)
                .build();
        var response = this.stub.getUserInformation(request);
        log.info("{}", response);
        Assertions.assertEquals(10_000, response.getBalance());
        Assertions.assertEquals("Sam", response.getName());
        Assertions.assertTrue(response.getHoldingsList().isEmpty());


    }

    @Test
    public void unknownUserTest(){

       var ex=  Assertions.assertThrows(StatusRuntimeException.class, ()->{

            var request = UserInformationRequest.newBuilder()
                    .setUserId(10)
                    .build();
            var response = this.stub.getUserInformation(request);

        });

        Assertions.assertEquals(Status.Code.NOT_FOUND, ex.getStatus().getCode());

    }

    @Test
    public void unknownTickerBuyTest(){
        var ex=  Assertions.assertThrows(StatusRuntimeException.class, ()->{
            var request = StockTradeRequest.newBuilder()
                    .setUserId(1)
                    .setAction(TradeAction.BUY)
                    .setTicker(Ticker.UNKNOWN)
                    .setPrice(45)
                    .setQuantity(2)
                    .build();

            var response = this.stub.tradeStock(request);

        });

        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ex.getStatus().getCode());

    }

    @Test
    public void insufficientSharesTest(){

        var ex=  Assertions.assertThrows(StatusRuntimeException.class, ()->{
            var request = StockTradeRequest.newBuilder()
                    .setUserId(1)
                    .setAction(TradeAction.SELL)
                    .setTicker(Ticker.GOOGLE)
                    .setPrice(45)
                    .setQuantity(2)
                    .build();

            var response = this.stub.tradeStock(request);

        });

        Assertions.assertEquals(Status.Code.FAILED_PRECONDITION, ex.getStatus().getCode());
    }

    @Test
    public void insufficientBalanceTest(){
        var ex=  Assertions.assertThrows(StatusRuntimeException.class, ()->{
            var request = StockTradeRequest.newBuilder()
                    .setUserId(1)
                    .setAction(TradeAction.BUY)
                    .setTicker(Ticker.GOOGLE)
                    .setPrice(10_000)
                    .setQuantity(2)
                    .build();

            var response = this.stub.tradeStock(request);

        });

        Assertions.assertEquals(Status.Code.FAILED_PRECONDITION, ex.getStatus().getCode());
    }


}
