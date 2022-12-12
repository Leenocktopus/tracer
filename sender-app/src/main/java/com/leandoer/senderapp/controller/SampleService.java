package com.leandoer.senderapp.controller;

import com.leandoer.sender.annotation.Generator;
import com.leandoer.sender.annotation.PayloadType;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class SampleService {

    @Generator(type = PayloadType.NUMBER, labels = {"java", "decimal"})
    public Long generateNumber() {
        return ThreadLocalRandom.current().nextLong();
    }

    @Generator(type = PayloadType.BINARY_STRING, labels = {"binary", "hardware"})
    public String generateBinary() {
        return Long.toBinaryString(ThreadLocalRandom.current().nextLong()) +
                Long.toBinaryString(ThreadLocalRandom.current().nextLong());
    }

    @Generator(type = PayloadType.BINARY_STRING, labels = {"java"})
    public String errorBinary() {
        return ThreadLocalRandom.current().nextBoolean() ? "0000000000000000000000000000000000000000000000000"
                : "1111111111111111111111111111111111111111111111111111111";
    }

    @Generator(type = PayloadType.NUMBER, labels = {"hardware"})
    public Long boundedRepeating() {
        return ThreadLocalRandom.current().nextLong(10000000000L, 20000000000L);
    }
}
