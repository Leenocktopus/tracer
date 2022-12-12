package com.leandoer.senderapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;


    @GetMapping("/random-number")
    public Long randomNumber() {
        return sampleService.generateNumber();
    }


    @GetMapping("/random-binary")
    public String randomBinary() {
        return sampleService.generateBinary();
    }

    @GetMapping("/error-binary")
    public String errorBinary() {
        return sampleService.errorBinary();
    }

    @GetMapping("/bounded-repeating")
    public String boundedRepeating() {
        for (int i = 0; i < 200; i++) {
            sampleService.boundedRepeating();
        }
        return "";
    }
}
