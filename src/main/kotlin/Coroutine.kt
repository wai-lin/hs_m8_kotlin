package org.example.coroutines_exercises

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.temporal.ChronoUnit

enum class MenuItem(val time: Long) {
    AMERICANO(2),
    ESPRESSO(1),
    DOUBLE_ESPRESSO(2),
    CAPUCCINO(4),
    FLAT_WHITE(4),
}

data class Order(val id: Int, val menuItem: MenuItem)

class CoffeeShop(
    val openDuration: Long,
    val unit: ChronoUnit,
    val baristas: List<String>
) {
    private val orderChannel = Channel<Order>(Channel.UNLIMITED)
    private val coffeeMachine = Mutex()

    private val startTime = System.currentTimeMillis()
    private val closingTime = startTime + unit.duration.multipliedBy(openDuration).toMillis()

    fun isOpen(): Boolean = System.currentTimeMillis() < closingTime

    suspend fun placeOrder(order: Order) {
        if (isOpen()) {
            println("\nOrder placed: order #${order.id} for ${order.menuItem}")
            orderChannel.send(order)
        } else {
            this.closeOrders()
            println("\nShop closed. Order #${order.id} for ${order.menuItem} rejected.")
        }
    }

    suspend fun processOrders() = coroutineScope {
        val baristasJobs = baristas.map { name ->
            launch {
                val rechargeTime = 100

                for (order in orderChannel) {
                    coffeeMachine.withLock {
                        println("$name is preparing order #${order.id} for ${order.menuItem}")
                        delay(order.menuItem.time + rechargeTime)
                        println("$name has completed order #${order.id} for ${order.menuItem}")
                    }
                }
            }
        }
        baristasJobs.joinAll()
    }

    fun closeOrders() {
        println("Coffee Shop is closing. No new orders will be accepted.")
        orderChannel.close()
    }

}


class OrderGenerator(private val coffeeShop: CoffeeShop) {
    private var orderId = 1

    suspend fun generateOrders() {
        while (coffeeShop.isOpen()) {
            val menuItem = MenuItem.values().random()
            val order = Order(orderId++, menuItem)
            coffeeShop.placeOrder(order)
            delay(500)
        }
        println("\nOrderGenerator: Shop is closed. Stopping order generation.")
    }
}

fun main() {
    runBlocking {
        val shop = CoffeeShop(
            openDuration = 10,
            unit = ChronoUnit.SECONDS,
            baristas = listOf("Alice", "Bob")
        )
        println("Coffee shop is now OPEN!")

        val orderGenerator = OrderGenerator(shop)

        coroutineScope {
            launch { orderGenerator.generateOrders() }
            launch { shop.processOrders() }
        }
    }
}