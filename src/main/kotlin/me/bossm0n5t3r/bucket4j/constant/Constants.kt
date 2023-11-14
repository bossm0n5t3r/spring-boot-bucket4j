package me.bossm0n5t3r.bucket4j.constant

object Constants {
    object Headers {
        const val X_RATE_LIMIT_REMAINING = "X-Rate-Limit-Remaining"
        const val X_RATE_LIMIT_RETRY_AFTER_SECONDS = "X-Rate-Limit-Retry-After-Seconds"
    }

    object Token {
        const val BEARER_ = "Bearer "
    }

    const val NANO_SECONDS = 1_000_000_000
}
