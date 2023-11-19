package me.bossm0n5t3r.bucket4j.webflux

import io.github.bucket4j.Bucket
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Duration

class BucketTest {
    @Test
    fun basicTest() {
        // bucket with capacity 20 tokens and
        // with refilling speed 1 token per each 6 second (refillTokens = 10, refillPeriod = 60s)
        val bucket =
            Bucket.builder()
                .addLimit {
                    it.capacity(20).refillGreedy(10, Duration.ofMinutes(1L))
                }
                .build()

        repeat(20) {
            assertTrue(bucket.tryConsume(1))
        }
        assertFalse(bucket.tryConsume(1))
    }

    @Test
    fun basicRefillGreedyTest() =
        runBlocking {
            val tokenCapacity = 20L
            val refillTokens = 10
            val refillPeriod = Duration.ofSeconds(10L)
            // bucket with capacity 20 tokens and
            // with refilling speed 1 token per each 1 second (refillTokens = 10, refillPeriod = 10s)
            val bucket =
                Bucket.builder()
                    .addLimit {
                        it.capacity(tokenCapacity).refillGreedy(refillTokens.toLong(), refillPeriod)
                    }
                    .build()

            bucket.tryConsume(tokenCapacity) // Consume all directly
            assertFalse(bucket.tryConsume(1)) // Check emtpy

            repeat(refillTokens) {
                delay(refillPeriod.toMillis() / refillTokens) // Delay until refill greedy
                assertTrue(bucket.tryConsume(1)) // Check refill success
                assertFalse(bucket.tryConsume(1)) // Check refill only 1 token
            }
            assertFalse(bucket.tryConsume(1))
        }
}
