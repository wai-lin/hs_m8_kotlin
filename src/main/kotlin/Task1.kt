package org.example

fun updateAtIndex(strArr: Array<String>, vararg atIndex: Int, fn: (String) -> String): Array<String> {
    val arr = strArr.copyOf()
    for (index in atIndex) {
        if (index < 0 || index >= arr.size) {
            error("Index $index is out of bounds")
        }
        arr[index] = fn(arr[index])
    }
    return arr
}

fun updateAtIndex2(strArr: Array<String>, vararg atIndex: Int, fn: (String) -> String): Array<String> {
    val arr = strArr.copyOf()
    return arr.mapIndexed { index, s ->
        if (index in atIndex) fn(s) else s
    }.toTypedArray()
}

fun main() {
    updateAtIndex(
        strArr = arrayOf("a", "b", "c", "d", "e"),
        1, 3,
    ) { it.uppercase() }.forEach { print(it) }

    println()

    updateAtIndex2(
        strArr = arrayOf("a", "b", "c", "d", "e"),
        1, 3,
    ) { it.uppercase() }.forEach { print(it) }
}