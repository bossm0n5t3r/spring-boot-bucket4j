package me.bossm0n5t3r.bucket4j.web.configuration

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.bossm0n5t3r.bucket4j.application.PricingPlanService
import me.bossm0n5t3r.bucket4j.constant.Constants.Headers.X_RATE_LIMIT_REMAINING
import me.bossm0n5t3r.bucket4j.constant.Constants.Headers.X_RATE_LIMIT_RETRY_AFTER_SECONDS
import me.bossm0n5t3r.bucket4j.constant.Constants.NANO_SECONDS
import me.bossm0n5t3r.bucket4j.constant.Constants.Token.BEARER_
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class RateLimitInterceptor(
    private val pricingPlanService: PricingPlanService,
) : HandlerInterceptor {
    private val logger = KotlinLogging.logger {}

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION) ?: ""
        val token =
            if (authorizationHeader.startsWith(BEARER_)) {
                authorizationHeader.substringAfter(BEARER_)
            } else {
                authorizationHeader
            }

        val bucket = pricingPlanService.resolveBucket(token)
        val probe = bucket.tryConsumeAndReturnRemaining(1)
        val remainingLimit = probe.remainingTokens

        return if (probe.isConsumed) {
            logger.info { "Remaining limit - $remainingLimit" }
            response.addHeader(X_RATE_LIMIT_REMAINING, remainingLimit.toString())
            true
        } else {
            logger.error { "You have exhausted your API Request Quota" }
            val waitForRefill = probe.nanosToWaitForRefill / NANO_SECONDS
            response.addHeader(X_RATE_LIMIT_RETRY_AFTER_SECONDS, waitForRefill.toString())
            response.sendError(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "You have exhausted your API Request Quota",
            )
            false
        }
    }
}
