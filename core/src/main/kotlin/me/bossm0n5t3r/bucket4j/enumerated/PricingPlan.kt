package me.bossm0n5t3r.bucket4j.enumerated

import io.github.bucket4j.Bandwidth
import java.time.Duration

enum class PricingPlan(
    val limit: Bandwidth,
) {
    FREE(
        Bandwidth
            .builder()
            .capacity(20)
            .refillIntervally(20, Duration.ofHours(1))
            .build(),
    ),
    BASIC(
        Bandwidth
            .builder()
            .capacity(40)
            .refillIntervally(40, Duration.ofHours(1))
            .build(),
    ),
    PROFESSIONAL(
        Bandwidth
            .builder()
            .capacity(100)
            .refillIntervally(100, Duration.ofHours(1))
            .build(),
    ),
    ;

    companion object {
        fun resolvePlanFromUserRole(userRole: UserRole): PricingPlan =
            when (userRole) {
                UserRole.ANONYMOUS -> FREE
                UserRole.USER -> BASIC
                UserRole.PREMIUM -> PROFESSIONAL
                UserRole.ADMIN -> PROFESSIONAL
            }
    }
}
