package me.olutu.application

import ShoppingListItem
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
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import org.litote.kmongo.index


val shoppingList = mutableListOf(
	ShoppingListItem("Cucumbers 🥒", 1),
	ShoppingListItem("Tomatoes 🍅", 2),
	ShoppingListItem("Orange Juice 🍊", 3)
)

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
			route(ShoppingListItem.path) {
				get {
					call.respond(shoppingList)
				}
				post {
					shoppingList += call.receive<ShoppingListItem>()
					call.respond(HttpStatusCode.OK)
				}
				delete("/{id}"){
					val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
					shoppingList.removeIf { it.id == id }
					call.respond(HttpStatusCode.OK)
				}
			}
		}
	}.start(wait = true)
}
