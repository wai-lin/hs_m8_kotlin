package org.example.ok

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

val jediNames = listOf("Luke Skywalker", "Yoda", "Obi-Wan Kenobi", "Mace Windu", "Qui-Gon Jinn")
val sithNames = listOf("Darth Vader", "Emperor Palpatine", "Darth Maul", "Kylo Ren", "Count Dooku")
val rebelNames = listOf("Leia Organa", "Han Solo", "Chewbacca", "C3PO", "R2D2")
val imperialNames = listOf("Stormtrooper", "Imperial Officer", "Imperial Guard", "Death Trooper", "TIE Fighter Pilot")

fun String.toStarWarsCharacter(fraction: Fraction): StarWarsCharacter {
    return StarWarsCharacter(this, fraction)
}

fun List<String>.toStarWarsCharacters(fraction: Fraction): List<StarWarsCharacter> {
    return this.map { it.toStarWarsCharacter(fraction) }
}

// Pair character using zip
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
//    for (i in 1..round) {
//        val roundResults = pairs.map { battleRound(it) }
//        results.addAll(roundResults)
//    }
    return results
}


fun simulateBestOf3(
    matchUps: List<Pair<StarWarsCharacter, StarWarsCharacter>>
): List<List<Pair<Int, List<Pair<StarWarsCharacter, Int>>>>> {
    val bestOf3 = matchUps.map { (char1, char2) ->
        simulateRound(
            listOf(char1 to char2, char1 to char2, char1 to char2)
        ).mapIndexed { index, round ->
            index + 1 to round
        }
    }
    return bestOf3
}

fun main() {
    val jedi = jediNames.toStarWarsCharacters(Fraction.JEDI)
    val sith = sithNames.toStarWarsCharacters(Fraction.SITH)
    val rebel = rebelNames.toStarWarsCharacters(Fraction.REBEL)
    val imperial = imperialNames.toStarWarsCharacters(Fraction.IMPERIAL)

    // unzip the list of pairs to two separate lists
    println("\n\nUnzip the list of pairs to two separate lists\n")
    val (j, s) = pairCharacters(jedi, sith).unzip()
    j.forEach { println(it) }
    s.forEach { println(it) }

    // try zipWithNext
    println("\n\nTry zipWithNext\n")
    val jediPairs = jedi.zipWithNext()
    jediPairs.forEach { println(it) }

    // use `reduce` to create scoreboard by character
    println("\n\nReduce to create scoreboard by character\n")
    val matchUps = pairCharacters(jedi, sith)
    val rounds = simulateRound(matchUps, 3)
    rounds.reduce { acc, pairs ->
        acc + pairs
    }.groupBy({ it.first }, { it.second })
        .forEach { (character, scores) ->
            println("Character: $character")
            println("Wins: ${scores.count { it == 1 }}")
            println("Losses: ${scores.count { it == 0 }}")
        }

    // `fold` to create scoreboard by fraction
    println("\n\nFold to create scoreboard by fraction\n")
    val matchUps2 = pairCharacters(rebel, imperial)

    println("How many rounds do you want to simulate?")
    val roundsToSimulate = readLine()?.toIntOrNull() ?: 3

    val rounds2 = simulateRound(matchUps2, roundsToSimulate)
    val scoreboard2 = rounds2
        .flatten()
        .fold(mutableMapOf<Fraction, Int>())
        { acc, (character, score) ->
            acc[character.fraction] = acc.getOrDefault(character.fraction, 0) + score
            acc
        }
    scoreboard2.forEach { (fraction, score) ->
        println("Fraction: $fraction")
        println("Score: $score")
    }
}