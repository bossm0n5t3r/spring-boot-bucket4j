package me.bossm0n5t3r.bucket4j.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

class TooManyRequestException(
    httpHeaders: HttpHeaders = HttpHeaders.EMPTY,
    errors: Map<String, List<String>> = emptyMap(),
) : CommonException(
        httpStatus = HttpStatus.TOO_MANY_REQUESTS,
        httpHeaders = httpHeaders,
        errors = errors,
    )
