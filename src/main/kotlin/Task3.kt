package org.example

data class Contact(
    val email: String? = null,
    val phone: String? = null,
)

data class Person(
    val name: String,
    val contact: Contact,
)

fun main() {
    val person = Person(
        name = "Alice",
        contact = Contact(),
    )
    println("Before: $person")

    with (person) {
        copy(
            contact = contact.copy(
                email = "alice@gmail.com",
                phone = "+1-123-456-7890",
            )
        )
    }.let { println("After: $it") }
}