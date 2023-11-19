package me.bossm0n5t3r.bucket4j.web.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/web")
class DummyController {
    @GetMapping
    fun healthCheck(): ResponseEntity<Unit> = ResponseEntity.ok().build()
}
