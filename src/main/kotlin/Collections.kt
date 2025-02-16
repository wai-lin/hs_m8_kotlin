package org.example

val jediNames = listOf("Luke Skywalker", "Yoda", "Obi-Wan Kenobi", "Mace Windu", "Qui-Gon Jinn")
val sithNames = listOf("Darth Vader", "Emperor Palpatine", "Darth Maul", "Kylo Ren", "Count Dooku")
val rebelNames = listOf("Leia Organa", "Han Solo", "Chewbacca", "C3PO", "R2D2")
val imperialNames = listOf("Stormtrooper", "Imperial Officer", "Imperial Guard", "Death Trooper", "TIE Fighter Pilot")

//code to convert each character name to a StarWarsCharacter object.
//the conversion as extension function on String, accepting a Fraction as an argument.
fun String.toStarWarsCharacter(fraction: Fraction): StarWarsCharacter {
    return StarWarsCharacter(this, fraction)
}

// extension fun on listOf
fun List<String>.starWarsCharacters(fraction: Fraction): List<StarWarsCharacter> {
    return this.map { it.toStarWarsCharacter(fraction) }
}

//Fraction enum with values JEDI, SITH, REBEL, IMPERIAL, etc ...
enum class Fraction {
    JEDI,
    SITH,
    REBEL,
    IMPERIAL,
}

//Create the following classes:
//StarWarsCharacter with properties name: String and fraction: Fraction.
class StarWarsCharacter(
    val name: String,
    val fraction: Fraction
) {
    override fun toString(): String {
        return "$name - $fraction"
    }
}

fun pairCharacters(
    list1: List<StarWarsCharacter>,
    list2: List<StarWarsCharacter>
): List<Pair<StarWarsCharacter, StarWarsCharacter>> {
    return list1.mapIndexedNotNull { index, character1 ->
        if (index < list2.size) character1 to list2[index]
        else null
    }
}

fun simulateRound(
    pairs: List<Pair<StarWarsCharacter, StarWarsCharacter>>
): List<List<Pair<StarWarsCharacter, Int>>> {
    return pairs.map { (character1, character2) ->
        val winner = setOf(character1, character2).random()
        val looser = if (winner == character1) character2 else character1
        listOf(
            winner to 1,
            looser to 0,
        )
    }
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

fun simulateFlatBestOf3(
    matchUps: List<Pair<StarWarsCharacter, StarWarsCharacter>>
): List<Pair<StarWarsCharacter, Int>> {
    val flattenBestOf3 = matchUps.map { (char1, char2) ->
        simulateRound(
            listOf(char1 to char2, char1 to char2, char1 to char2)
        ).flatten()
    }
    return flattenBestOf3.flatten()
}

fun main() {
    // convert both lists to a list of StarWarsCharacter objects.
    val jedi = jediNames.starWarsCharacters(Fraction.JEDI)
    val sith = sithNames.starWarsCharacters(Fraction.SITH)
    val rebel = rebelNames.starWarsCharacters(Fraction.REBEL)
    val imperial = imperialNames.starWarsCharacters(Fraction.IMPERIAL)

    // Ex1
    println("Ex1::\n")
    val allCharacters = jedi + sith + rebel + imperial
    allCharacters.forEach { println(it) }

    // Ex2
    println("\nEx2::\n")
    val matchUps = pairCharacters(jedi, sith)
    println("Pairs::")
    matchUps.forEach { println(it) }

    println("\nWinners::")
    val fightResults = simulateRound(matchUps)
    fightResults.forEach { println(it) }

    // Ex3
    println("\nEx3::")
    val bestOf3 = simulateBestOf3(matchUps)
    bestOf3.forEachIndexed { index, matchUp ->
        val matchNo = index + 1
        println("\nMatchUp $matchNo::")
        matchUp.forEach { (round, results) ->
            println("Round $round:")
            results.forEach { (character, score) ->
                println("$character - $score")
            }
        }
    }

    // Ex4
    println("\nEx4::")
    val flatBestOf3 = simulateFlatBestOf3(matchUps)
    flatBestOf3.forEach { println(it) }

    // Filters
    println("\nJedi and Rebel characters::")
    val lightSide = allCharacters.filter {
        it.fraction in setOf(Fraction.JEDI, Fraction.REBEL)
    }
    lightSide.forEach { println(it) }

    println("\nNot Jedi and Rebel characters::")
    val darkSide = allCharacters.filter {
        it.fraction in setOf(Fraction.SITH, Fraction.IMPERIAL)
    }
    darkSide.forEach { println(it) }

    // Match with flat results
    val paris = pairCharacters(
        lightSide,
        darkSide,
    )
    val flatResults = simulateFlatBestOf3(paris)

    // Group by character
    println("\nGroup by character::")
    val charGroup = flatResults.groupBy { (char, _) -> char }
    charGroup.forEach { (character, results) ->
        println("\n$character")
        println("$results")
    }
    // Total score of each character
    val totalScore = charGroup.map { (character, results) ->
        character to results.sumOf { (_, score) -> score }
    }.sortedByDescending { (_, score) -> score }
    println("\nTotal score of each character::")
    totalScore.forEach { println(it) }

    // Group by fraction
    println("\nGroup by fraction::")
    val fracGroup = flatResults.groupBy { (char, _) -> char.fraction }
    fracGroup.forEach { (fraction, results) ->
        println("\n$fraction")
        println("$results")
    }
    // Total score by fractions
    val totalScoreByFrac = fracGroup.map { (fraction, results) ->
        fraction to results.sumOf { (_, score) -> score }
    }.sortedByDescending { (_, score) -> score }
    println("\nTotal score by fractions::")
    totalScoreByFrac.forEach { println(it) }
}