package me.bossm0n5t3r.bucket4j.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["me.bossm0n5t3r"])
class SpringBootBucket4jWebApplication

fun main(args: Array<String>) {
    runApplication<SpringBootBucket4jWebApplication>(*args)
}
