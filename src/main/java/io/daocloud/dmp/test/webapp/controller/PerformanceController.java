package io.daocloud.dmp.test.webapp.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;

import static java.time.temporal.ChronoUnit.MILLIS;

@RestController
@RequestMapping("/performance")
public class PerformanceController {
    private HashMap<String, byte[]> BODY_CACHE = new HashMap<>();


    @RequestMapping("/{delay}/{size}")
    public Mono<byte[]> data(@PathVariable long delay, @PathVariable long size) {
        return Mono.just(delay)
                .delayElement(Duration.of(delay, MILLIS))
                .map(__ -> getBody(size));
    }

    @RequestMapping("/hello-world")
    public Mono<String> hello(@PathVariable long delay) {
        return Mono.just("hello, world");
    }

    @RequestMapping("/{delay}/hello-world")
    public Mono<String> delayHello(@PathVariable long delay) {
        return Mono
                .just("")
                .delayElement(Duration.of(delay, MILLIS))
                .map(__ -> "hello world");
    }

    private byte[] getBody(long size) {
        final String key = String.valueOf(size);
        if (BODY_CACHE.containsKey(key)) {
            return BODY_CACHE.get(key);
        }

        byte[] s = new byte[1024 * Integer.parseInt(key)];
        Arrays.fill(s, (byte) 10);
        BODY_CACHE.put(key, s);
        return s;
    }

}
