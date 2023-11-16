package me.bossm0n5t3r.bucket4j.application

import io.github.bucket4j.Bucket
import me.bossm0n5t3r.bucket4j.enumerated.PricingPlan
import me.bossm0n5t3r.bucket4j.enumerated.UserRole
import me.bossm0n5t3r.bucket4j.security.JWTProvider
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class PricingPlanService(
    private val jwtProvider: JWTProvider,
) {
    private val cache = ConcurrentHashMap<String, Bucket>()

    fun resolveBucket(token: String) = cache.computeIfAbsent(token, this::newBucket)

    private fun newBucket(token: String): Bucket {
        val userRole = jwtProvider.getRole(token) ?: UserRole.ANONYMOUS

        val pricingPlan = PricingPlan.resolvePlanFromUserRole(userRole)

        return Bucket.builder()
            .addLimit(pricingPlan.limit)
            .build()
    }
}
