package org.example.fs_exercise

import java.io.File
import java.io.IOException

enum class Fraction {
    JEDI,
    SITH,
    REBEL,
    IMPERIAL,
}

class StarWarsCharacter(
    val name: String,
    val fraction: Fraction
) {
    override fun toString(): String {
        return "Name: $name\nFraction: $fraction\n"
    }
}

fun Sequence<StarWarsCharacter>.groupByFraction(): Map<Fraction, List<StarWarsCharacter>> {
    return this.groupBy { it.fraction }
}

fun pairCharacters(
    list1: List<StarWarsCharacter>,
    list2: List<StarWarsCharacter>
): List<Pair<StarWarsCharacter, StarWarsCharacter>> {
    val pairs = list1.zip(list2)
    return pairs
}

fun battleRound(
    pair: Pair<StarWarsCharacter, StarWarsCharacter>
): List<Pair<StarWarsCharacter, Int>> {
    val winner = setOf(pair.first, pair.second).random()
    val looser = if (winner == pair.first) pair.second else pair.first
    return listOf(
        winner to 1,
        looser to 0,
    )
}

fun simulateRound(
    pairs: List<Pair<StarWarsCharacter, StarWarsCharacter>>,
    round: Int = 1,
): List<List<Pair<StarWarsCharacter, Int>>> {
    val results = mutableListOf<List<Pair<StarWarsCharacter, Int>>>()
    val roundsSequence = generateSequence { pairs }
    val rounds = roundsSequence.take(round)
    rounds.forEach { roundPairs ->
        val roundResults = roundPairs.map { battleRound(it) }
        results.addAll(roundResults)
    }
    return results
}

fun loadStarWarsCharacters(path: String): Sequence<StarWarsCharacter> {
    return try {
        val file = File(path)

        file.bufferedReader().useLines { line ->
            line.drop(1).map {
                val (name, fraction) = it.split(",")
                StarWarsCharacter(
                    name = name,
                    fraction = Fraction.valueOf(fraction.uppercase())
                )
            }.toList() // toList() is necessary to close the bufferedReader
        }.asSequence() // asSequence() is necessary to convert back as a sequence
    } catch (e: IOException) {
        e.printStackTrace()
        error("An error occurred. ${e.message}")
    }
}

fun saveResults(summary: List<List<Pair<StarWarsCharacter, Int>>>, path: String, format: String) {
    if (format == "csv") {
        val file = File("$path.csv")
        file.bufferedWriter().use { writer ->
            writer.write("P1,P1Fraction,P1Score,P2,P2Fraction,P2Score\n")
            summary.forEach {
                val (p1, p2) = it.get(0) to it.get(1)
                writer.write("${p1.first.name},${p1.first.fraction},${p1.second},")
                writer.write("${p2.first.name},${p2.first.fraction},${p2.second}\n")
            }
        }
    }

    if (format == "json") {
        val file = File("$path.json")
        file.bufferedWriter().use { writer ->
            writer.write("[\n")
            summary.forEachIndexed { index, it ->
                val (p1, p2) = it.get(0) to it.get(1)
                writer.write("{\n")
                writer.write("\t\"P1\": {\n")
                writer.write("\t\t\"Name\": \"${p1.first.name}\",\n")
                writer.write("\t\t\"Fraction\": \"${p1.first.fraction}\",\n")
                writer.write("\t\t\"Score\": ${p1.second}\n")
                writer.write("\t},\n")
                writer.write("\t\"P2\": {\n")
                writer.write("\t\t\"Name\": \"${p2.first.name}\",\n")
                writer.write("\t\t\"Fraction\": \"${p2.first.fraction}\",\n")
                writer.write("\t\t\"Score\": ${p2.second}\n")
                writer.write("\t}\n")
                if (index < summary.size - 1) writer.write("},\n")
                else writer.write("}\n")
            }
            writer.write("]\n")
        }
    }
}

fun main() {
    val characters = loadStarWarsCharacters("src/main/data/star_wars.csv")
//    characters.forEach { println(it) }

    val fractions = characters.groupByFraction()
//    fractions.forEach { (fraction, characters) ->
//        println("\nFraction: $fraction")
//        characters.forEach { println(it) }
//    }

    val pair1 = pairCharacters(
        fractions.get(Fraction.JEDI)!!,
        fractions.get(Fraction.SITH)!!
    )
    val pair2 = pairCharacters(
        fractions.get(Fraction.REBEL)!!,
        fractions.get(Fraction.IMPERIAL)!!
    )

    val pair1Results = simulateRound(pair1, 3)
    val pair2Results = simulateRound(pair2, 3)
    val summary = (pair1Results + pair2Results)

    val resultLoc = "src/main/outputs/star_wars_results"
    saveResults(summary, resultLoc, "csv")
    saveResults(summary, resultLoc, "json")
}
