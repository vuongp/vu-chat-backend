package com.example.plugins

import com.example.GenerateRequest
import com.example.GenerateResponse
import com.example.QuestionPostRequest
import com.example.QuestionPostResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 60000
    }
}

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World")
        }
        post("/question") {
            val question = call.receive<QuestionPostRequest>()

            val response = client.post("http://100.65.41.15:11434/api/generate") {
                contentType(ContentType.Application.Json)
                setBody(
                    GenerateRequest(
                        model = "vu-phi3",
                        prompt = question.message,
                        stream = false,
                    )
                )
            }
            if (response.status.isSuccess()) {
                val body = response.body<GenerateResponse>()
                println("Duration in seconds: ${body.total_duration}")
                call.respond(QuestionPostResponse(body.response))
            } else {
                call.respondText { "Something went wrong!" }
            }
        }
    }
}
