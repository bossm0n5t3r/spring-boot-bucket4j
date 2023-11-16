package me.bossm0n5t3r.bucket4j.exception

import jakarta.annotation.Priority
import me.bossm0n5t3r.bucket4j.constant.Constants.MESSAGE
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.result.view.ViewResolver
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2

@ControllerAdvice
@Priority(0) // should go before WebFluxResponseStatusExceptionHandler
class CustomWebExceptionHandler : ErrorWebExceptionHandler {
    override fun handle(
        exchange: ServerWebExchange,
        e: Throwable,
    ): Mono<Void> {
        return when (e) {
            is CommonException -> handleCommonException(e)
            is WebExchangeBindException -> handleWebExchangeBindException(e)
            is ServerWebInputException -> handleServerWebInputException(e)
            else -> handleInternalServerException(e)
        }.flatMap {
            val serverResponse = it.t1

            serverResponse.writeTo(exchange, ResponseContextInstance)
        }.then()
    }

    private fun handleCommonException(e: CommonException): Mono<Tuple2<ServerResponse, ServerExceptionResponse>> {
        val serverExceptionResponse = ServerExceptionResponse(e.errors)
        return ServerResponse
            .status(e.httpStatus)
            .headers {
                e.httpHeaders.forEach { key, value ->
                    it[key] = value
                }
            }
            .bodyValue(serverExceptionResponse).zipWith(Mono.just(serverExceptionResponse))
    }

    private fun handleWebExchangeBindException(ex: WebExchangeBindException): Mono<Tuple2<ServerResponse, ServerExceptionResponse>> {
        val responseBody = mutableMapOf<String, MutableList<String>>()
        for (fieldError in ex.fieldErrors) {
            val errors = responseBody.getOrPut(fieldError.field) { mutableListOf() }
            errors.add(fieldError.defaultMessage ?: "")
        }
        val serverExceptionResponse = ServerExceptionResponse(responseBody)
        return ServerResponse
            .status(HttpStatus.BAD_REQUEST)
            .bodyValue(serverExceptionResponse).zipWith(Mono.just(serverExceptionResponse))
    }

    private fun handleServerWebInputException(ex: ServerWebInputException): Mono<Tuple2<ServerResponse, ServerExceptionResponse>> {
        val responseBody = mapOf("ServerWebInputException" to listOf(ex.message))
        val serverExceptionResponse = ServerExceptionResponse(responseBody)
        return ServerResponse
            .status(HttpStatus.BAD_REQUEST)
            .bodyValue(serverExceptionResponse).zipWith(Mono.just(serverExceptionResponse))
    }

    private fun handleInternalServerException(ex: Throwable): Mono<Tuple2<ServerResponse, ServerExceptionResponse>> {
        val responseBody = mapOf(MESSAGE to listOf(ex.localizedMessage))
        val serverExceptionResponse = ServerExceptionResponse(responseBody)
        return ServerResponse
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .bodyValue(serverExceptionResponse).zipWith(Mono.just(serverExceptionResponse))
    }

    private object ResponseContextInstance : ServerResponse.Context {
        val strategies: HandlerStrategies = HandlerStrategies.withDefaults()

        override fun messageWriters(): List<HttpMessageWriter<*>> {
            return strategies.messageWriters()
        }

        override fun viewResolvers(): List<ViewResolver> {
            return strategies.viewResolvers()
        }
    }
}
