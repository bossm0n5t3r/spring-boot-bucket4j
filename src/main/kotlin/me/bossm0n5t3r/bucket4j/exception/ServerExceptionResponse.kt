package me.bossm0n5t3r.bucket4j.exception

data class ServerExceptionResponse(val errors: Map<String, List<String>>)
