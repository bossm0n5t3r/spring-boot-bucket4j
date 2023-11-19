package me.bossm0n5t3r.bucket4j.webflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["me.bossm0n5t3r"])
class SpringBootBucket4jWebfluxApplication

fun main(args: Array<String>) {
    runApplication<SpringBootBucket4jWebfluxApplication>(*args)
}
