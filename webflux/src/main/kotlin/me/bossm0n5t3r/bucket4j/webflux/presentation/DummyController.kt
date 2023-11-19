package me.bossm0n5t3r.bucket4j.webflux.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
@RequestMapping("/webflux")
class DummyController {
    @GetMapping
    fun healthCheck(): Mono<ResponseEntity<Unit>> = ResponseEntity.ok().build<Unit>().toMono()
}
