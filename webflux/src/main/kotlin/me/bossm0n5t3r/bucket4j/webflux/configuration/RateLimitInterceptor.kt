package me.bossm0n5t3r.bucket4j.webflux.configuration

import io.github.oshai.kotlinlogging.KotlinLogging
import me.bossm0n5t3r.bucket4j.application.PricingPlanService
import me.bossm0n5t3r.bucket4j.constant.Constants.Headers.X_RATE_LIMIT_REMAINING
import me.bossm0n5t3r.bucket4j.constant.Constants.Headers.X_RATE_LIMIT_RETRY_AFTER_SECONDS
import me.bossm0n5t3r.bucket4j.constant.Constants.NANO_SECONDS
import me.bossm0n5t3r.bucket4j.constant.Constants.Token.BEARER_
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Configuration
class RateLimitInterceptor(
    private val pricingPlanService: PricingPlanService,
) : WebFilter {
    private val logger = KotlinLogging.logger {}

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void> {
        return Mono.just(getAuthorizationHeader(exchange))
            .map { getRowToken(it) }
            .flatMap { token ->
                val bucket = pricingPlanService.resolveBucket(token)
                val probe = bucket.tryConsumeAndReturnRemaining(1)
                val remainingLimit = probe.remainingTokens
                if (probe.isConsumed) {
                    logger.info { "Remaining limit - $remainingLimit" }
                    exchange.response.headers.set(X_RATE_LIMIT_REMAINING, remainingLimit.toString())
                } else {
                    val waitForRefill = probe.nanosToWaitForRefill / NANO_SECONDS
                    exchange.response.headers.set(X_RATE_LIMIT_RETRY_AFTER_SECONDS, waitForRefill.toString())
                    exchange.response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS)
                    return@flatMap exchange.response.setComplete()
                }
                chain.filter(exchange)
            }
    }

    private fun getAuthorizationHeader(exchange: ServerWebExchange): String {
        return exchange.request.headers[HttpHeaders.AUTHORIZATION]
            ?.firstOrNull { it.isNotEmpty() }
            ?: ""
    }

    private fun getRowToken(authorizationHeader: String): String {
        return if (authorizationHeader.startsWith(BEARER_)) {
            authorizationHeader.substringAfter(BEARER_)
        } else {
            authorizationHeader
        }
    }
}
