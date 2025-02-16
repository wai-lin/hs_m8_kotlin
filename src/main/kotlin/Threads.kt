package org.example.thread_exercise

import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

fun makeCoffee(orders: AtomicInteger) {
    while (true) {
        val currentOrder = orders.getAndUpdate { if (it > 0) it - 1 else it }
        if (currentOrder <= 0) break

        try {
            println("${Thread.currentThread().name} is making your coffee.")
            Thread.sleep(1500)
            println("Your coffee is ready. Enjoy!")
        } catch (e: InterruptedException) {
            println("The coffee making process was interrupted.")
        }
    }
}

val coffeeMachine = Semaphore(1, true)

fun makeCoffeeWithMachine(orders: AtomicInteger) {
    while (true) {
        val currentOrder = orders.getAndUpdate { if (it > 0) it - 1 else it }
        if (currentOrder <= 0) break

        try {
            coffeeMachine.acquire()
            println("${Thread.currentThread().name} acquired the coffee machine for order #$currentOrder.")
            println("${Thread.currentThread().name} is making your coffee.")
            Thread.sleep(1500)
            println("Your coffee is ready. Enjoy!")
        } catch (e: InterruptedException) {
            println("The coffee making process was interrupted.")
        } finally {
            coffeeMachine.release()
        }
    }
}

fun main() {
    // Thread
    println("\nEx1 Thread: ")

    thread (name="Bonnie") {
        try {
            println("${Thread.currentThread().name} is making your coffee.")
            Thread.sleep(5000)
            println("Your coffee is ready. Enjoy!")
        } catch (e: InterruptedException) {
            println("The coffee making process was interrupted.")
        }
    }

    // Atomic
    println("\nEx2 AtomicInteger: ")

    val orders = AtomicInteger(5)

    val clyde = Thread { makeCoffee(orders) }
    clyde.name = "Clyde"

    val bonnie = Thread { makeCoffee(orders) }
    bonnie.name = "Bonnie"

    bonnie.start()
    clyde.start()

    bonnie.join()
    clyde.join()

    println("All orders have been processed.")

    // Semaphore

    println("\nEx3 Semaphore: ")

    val orders2 = AtomicInteger(7)

    val alice = Thread { makeCoffeeWithMachine(orders2) }
    alice.name = "Alice"

    val bob = Thread { makeCoffeeWithMachine(orders2) }
    bob.name = "Bob"

    alice.start()
    bob.start()

    alice.join()
    bob.join()
}
