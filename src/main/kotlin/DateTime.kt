package com.example.datetime_exercise

fun main() {
    // get current date and time as ZonedDateTim
    val currentDateTime = java.time.ZonedDateTime.now()
    println("Current date and time: $currentDateTime")

    // Print the zone and zone offset.
    val zone = currentDateTime.zone
    val zoneOffset = currentDateTime.offset
    println("Zone: $zone")
    println("Zone offset: $zoneOffset")

    // From the date and time, get the same instant, but in different time zone
    val newYorkZone = java.time.ZoneId.of("UTC")
    val newYorkDateTime = currentDateTime.withZoneSameInstant(newYorkZone)
    println("New York date and time: $newYorkDateTime")

    // Print the zone id and zone offset for this new time zone
    val newYorkZoneId = newYorkDateTime.zone
    val newYorkZoneOffset = newYorkDateTime.offset
    println("New York zone: $newYorkZoneId")
    println("New York zone offset: $newYorkZoneOffset")

    // Calculate the time difference between the local time zone and the other time zone
    val timeDifference = currentDateTime.offset.totalSeconds - newYorkDateTime.offset.totalSeconds
    println("Time difference: $timeDifference seconds")
}
