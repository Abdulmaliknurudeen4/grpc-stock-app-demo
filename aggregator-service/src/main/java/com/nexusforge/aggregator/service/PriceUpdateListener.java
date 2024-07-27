package com.nexusforge.aggregator.service;

import com.nexusforge.aggregator.dto.PriceUpdateDto;
import com.nexusforge.stock.PriceUpdate;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class PriceUpdateListener implements StreamObserver<PriceUpdate> {
    private static final Logger log = LoggerFactory.getLogger(PriceUpdateListener.class);
    private final Set<SseEmitter> emitters = Collections.synchronizedSet(new HashSet<>());
    private final long sseTimeout;

    public PriceUpdateListener(@Value("${sse.timeout:300000}") long sseTimeout) {
        this.sseTimeout = sseTimeout;
    }

    public SseEmitter createEmitter() {
        var emitter = new SseEmitter(sseTimeout);
        this.emitters.add(emitter);
        //periodic cleaing of set
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((ex) -> emitters.remove(emitter));
        emitter.onCompletion(() -> emitters.remove(emitter));
        return emitter;
    }

    @Override
    public void onNext(PriceUpdate priceUpdate) {
        var dto = new PriceUpdateDto(priceUpdate.getTicker().toString(), priceUpdate.getPrice());
        // keep emitting if it's true, once it fails, remove the emitter event
        // for other servers not netty that do not call the onErrorCallbAck method()
        this.emitters.removeIf(e -> !this.send(e, dto));
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("streaming error", throwable);
        this.emitters.forEach(e -> e.completeWithError(throwable));
        this.emitters.clear();
    }

    @Override
    public void onCompleted() {
        this.emitters.forEach(ResponseBodyEmitter::complete);
        this.emitters.clear();
    }

    private boolean send(SseEmitter emitter, Object o){
        try {
            emitter.send(o);
            return true;
        } catch (IOException e) {
            log.warn("SSE error {}", e.getMessage());
            return false;
        }
    }
}
