package me.olutu.application

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*


fun HTML.index() {
	head {
		title("Hello from Ktor!")
	}
	body {
		div {
			+"Hello from Ktor"
		}
		div {
			id = "root"
		}
		script(src = "/static/shoppingList.js") {}
	}
}

fun main() {
	embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
		install(CORS){
			anyHost()
			allowHeader(HttpHeaders.ContentType)
		}
		install(Compression) {
			gzip()
		}
		install(ContentNegotiation){
			json()
		}
		routing {
			get("/") {
				call.respondHtml(HttpStatusCode.OK, HTML::index)
			}
			get("/hello") {
				call.respondText("Hello, API!")
			}
			static("/static") {
				resources()
			}
		}
	}.start(wait = true)
}
