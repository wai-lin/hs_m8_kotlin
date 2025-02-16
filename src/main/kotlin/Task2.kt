package org.example

fun Int.isEven(): Boolean {
    return this % 2 == 0
}
fun Int.isOdd(): Boolean {
    return this.isEven().not()
}

fun Array<String>.twice(): Array<String> {
    return this + this
}

fun main() {
    val number = 4
    println(number.isEven())
    println(number.isOdd())

    val array = arrayOf("a", "b", "c")
    println(array.twice().contentToString())
}