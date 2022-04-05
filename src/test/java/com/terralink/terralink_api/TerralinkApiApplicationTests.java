package com.terralink.terralink_api;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

@SpringBootTest
class TerralinkApiApplicationTests {

    @Test
    void simpleFluxExample() {
        Flux<Integer> flux = Flux
                .just(1, 2, 3, 4)
                .filter(number -> number > 2)
                .map(number -> number * 2)
                .log();

        StepVerifier
                .create(flux)
                .expectNext(6, 8)
                .verifyComplete();
    }

    @Test
    void fluxFlatMapExample() {
        Flux<String> flux = Flux
                .just("Thales", "Romania")
                .flatMap(string -> Flux.fromArray(string.split("")))
                .log();

        StepVerifier
                .create(flux)
                .expectNext("T", "h", "a", "l", "e", "s", "R", "o", "m", "a", "n", "i", "a")
                .verifyComplete();
    }

    @Test
    void fluxAsyncFlatMapExample() {
        Flux<String> flux = Flux
                .just("Thales", "Romania")
                .flatMap(string -> Flux
                        .fromArray(string.split(""))
                        // we will not know from which string the characters will come next
                        .delayElements(Duration.ofMillis(new Random().nextInt(1000))))
                .log();

        StepVerifier
                .create(flux)
                .expectNextCount(13)
                .verifyComplete();
    }

    @Test
    void infiniteFluxExample() {
        // will emit and increment the counter's value every 2 seconds
        Flux<Object> flux = Flux
                .create(sink -> {
                    sink.onRequest(request -> {
                        int counter = 1;
                        while (!sink.isCancelled()) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                break;
                            }

                            sink.next(counter);
                            counter++;
                        }
                    });
                }).log();

        StepVerifier
                .create(flux)
                .expectNext(1)
                .expectNext(2)
                // if we don't cancel here the flux will continue indefinitely
                .thenCancel()
                .verify();
    }

    @Test
    void simpleMonoExample() {
        Mono<Integer> mono = Mono
            .just(5)
            .map(number -> number * 3)
            .log();

        StepVerifier
            .create(mono)
            .expectNext(5 * 3)
            .verifyComplete();
    }

    @Test
    void errorMonoExample() {
        Mono<Integer> mono = Mono
            .just(0)
            .map(number -> 5 / number)
            .log();

        StepVerifier
            .create(mono)
            .expectError()
            .verify();
    }

    @Test
    void errorHandlingMonoExample() {
        Mono<Integer> mono = Mono
            .just(0)
            .map(number -> 5 / number)
            .onErrorReturn(0)
            .log();

        StepVerifier
            .create(mono)
            .expectNext(0)
            .verifyComplete();
    }

    @Test
    void flatMapMonoExample() {
        Mono<String> mono = Mono
            .just("Test")
            .flatMap(string ->
                Mono
                    .just(string + "!")
                    .delayElement(Duration.ofSeconds(2))
            ).log();

        StepVerifier
            .create(mono)
            .expectNext("Test!")
            .verifyComplete();
    }

    @Test
    void flatMapManyExample() {
        // usefull when we need to transform a mono into flux
        Flux<String> flux = Mono
            .just("Test")
            .flatMapMany(string -> 
                Flux
                    .just(string.split(""))
                    .delayElements(Duration.ofSeconds(1))
            ).log();

        StepVerifier
            .create(flux)
            .expectNext("T", "e", "s", "t")
            .verifyComplete();
    }

    @Test
    void asyncMonosExample() {
        Long t1 = System.currentTimeMillis();
        Mono<LinkedHashMap<String, List<String>>> mono = Mono.zip(
            Mono.just(List.of("A1", "A2", "A3")).delayElement(Duration.ofSeconds(5)),
            Mono.just(List.of("B1", "B2", "B3")).delayElement(Duration.ofSeconds(3))    
        ).map(tuple -> {
            LinkedHashMap<String, List<String>> map = new LinkedHashMap<String, List<String>>();
            map.put("DB1", tuple.getT1());
            map.put("DB2", tuple.getT2());

            return map;
        }).log();

        StepVerifier
            .create(mono)
            .consumeNextWith(dataMap -> {
                System.out.println((System.currentTimeMillis() - t1) / 1000);
                System.out.println(dataMap);
            }).verifyComplete();
    }

}
