package me.bossm0n5t3r.bucket4j.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

open class CommonException(
    val httpStatus: HttpStatus,
    val httpHeaders: HttpHeaders,
    val errors: Map<String, List<String>>,
) : RuntimeException()
