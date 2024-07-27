package com.nexusforge.aggregator.service;

import com.nexusforge.stock.StockPriceRequest;
import com.nexusforge.stock.StockServiceGrpc;
import com.nexusforge.user.StockTradeRequest;
import com.nexusforge.user.StockTradeResponse;
import com.nexusforge.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userClient;

    @GrpcClient("stock-service")
    private StockServiceGrpc.StockServiceBlockingStub stockClient;

    public StockTradeResponse trade(StockTradeRequest request){
        var priceRequest = StockPriceRequest.newBuilder()
                .setTicker(request.getTicker()).build();
        var priceResponse =this.stockClient.getStockPrice(priceRequest);

        //modify the price incase the user cheats. -> get price from stock service
        var tradeRequest = request.toBuilder()
                .setPrice(priceResponse.getPrice()).build();
        return this.userClient.tradeStock(tradeRequest);

    }

}
