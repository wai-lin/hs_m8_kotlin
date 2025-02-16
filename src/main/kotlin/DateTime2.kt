package com.example.datetime_exercise2

import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


data class Measurement(
    val time: Instant,
    val temperature: Double,
    val humidity: Double,
    val precipitation: Double
)

data class DailyStats(
    val date: LocalDate,
    val avgTemperature: Double,
    val avgHumidity: Double,
    val totalPrecipitation: Double
)

fun List<Measurement>.calcDailyStats(zoneId: ZoneId): List<DailyStats> {
    return this
        .groupBy { it.time.atZone(zoneId).toLocalDate() }
        .map { (date, measurements) ->
            val avgTemperature = measurements.map { it.temperature }.average()
            val avgHumidity = measurements.map { it.humidity }.average()
            val totalPrecipitation = measurements.map { it.precipitation }.sum()
            DailyStats(date, avgTemperature, avgHumidity, totalPrecipitation)
        }
        .sortedBy { it.date }
}

fun readCsvData(filePath: String): List<Measurement> {
    val file = File(filePath)
    return file.readLines().drop(1).map { line ->
        val (time, temperature, humidity, precipitation) = line.split(",")
        val cleanTime = time.substringBefore("[")
        Measurement(
            Instant.parse(cleanTime),
            temperature.toDouble(),
            humidity.toDouble(),
            precipitation.toDouble()
        )
    }
}


fun main() {
    println("\nMeteorological data:\n")
    val measurements = readCsvData("src/main/data/meteodata.csv")
    measurements.forEach { println(it) }

    println("\n\nEnter timezone (e.g. Asia/Bangkok):")
    val input = readLine()?.trim()
    val zoneId = try {
        if (input.isNullOrBlank()) error("Empty timezone")
        ZoneId.of(input)
    } catch (e: Exception) {
        println("Invalid timezone provided, defaulting to Asia/Bangkok.")
        ZoneId.of("Asia/Bangkok")
    }

    println("\nDaily stats in ${zoneId.id}:\n")
    val dailyStats = measurements.calcDailyStats(zoneId)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    dailyStats.forEach {
        val dateStr = it.date.format(formatter)
        println("Date: $dateStr, " +
                "Avg temperature: ${it.avgTemperature}, " +
                "Avg humidity: ${it.avgHumidity}, " +
                "Total precipitation: ${it.totalPrecipitation}")
    }
}