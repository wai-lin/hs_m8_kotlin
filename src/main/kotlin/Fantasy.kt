package org.example.fantsy


enum class CharacterLevel {
    LEVEL_1, LEVEL_2, LEVEL_3
}

data class CharacterMatchingStrategy(
    val name: String?,
    val level: CharacterLevel?,
    val characterClass: String?
) {
    companion object {
        val ANY = CharacterMatchingStrategy(null, null, null)
        fun byName(name: String) = CharacterMatchingStrategy(name, null, null)
        fun byLevel(level: CharacterLevel) = CharacterMatchingStrategy(null, level, null)
        fun byClass(characterClass: String) = CharacterMatchingStrategy(null, null, characterClass)
    }
}

data class MatchResult(
    val roundsFought: Int,
    val winner: Character?
)

internal object CharacterRepository {
    private val harryPotterCharacters = listOf(
        Sorcerer(
            name = "Harry Potter",
            health = 100,
            attackPower = 40,
            mana = 30,
            healingPower = 30,
            level = CharacterLevel.LEVEL_2
        ), // Balanced, more durable
        Sorcerer(
            name = "Hermione Granger",
            health = 90,
            attackPower = 40,
            mana = 40,
            healingPower = 30,
            level = CharacterLevel.LEVEL_1
        ), // High mana for tactical advantage
        Sorcerer(
            name = "Ron Weasley",
            health = 120,
            attackPower = 50,
            mana = 20,
            healingPower = 10,
            level = CharacterLevel.LEVEL_2
        ), // Higher health for tanking
        Sorcerer(
            name = "Severus Snape",
            health = 80,
            attackPower = 60,
            mana = 30,
            healingPower = 30,
            level = CharacterLevel.LEVEL_3
        ), // Strong attack, slightly higher health
        Sorcerer(
            name = "Albus Dumbledore",
            health = 90,
            attackPower = 40,
            mana = 40,
            healingPower = 30,
            level = CharacterLevel.LEVEL_1
        ), // Well-rounded, high healing power
        Sorcerer(
            name = "Lord Voldemort",
            health = 80,
            attackPower = 80,
            mana = 10,
            healingPower = 30,
            level = CharacterLevel.LEVEL_3
        ), // Powerful, but needs more survival
        Sorcerer(
            name = "Minerva McGonagall",
            health = 100,
            attackPower = 40,
            mana = 30,
            healingPower = 30,
            level = CharacterLevel.LEVEL_3
        ), // Balanced
        Sorcerer(
            name = "Bellatrix Lestrange",
            health = 80,
            attackPower = 70,
            mana = 20,
            healingPower = 30,
            level = CharacterLevel.LEVEL_1
        ), // Aggressive, but not overpowered
        Sorcerer(
            name = "Draco Malfoy",
            health = 100,
            attackPower = 40,
            mana = 30,
            healingPower = 30,
            level = CharacterLevel.LEVEL_2
        ), // Slightly more balanced
        Sorcerer(
            name = "Neville Longbottom",
            health = 130,
            attackPower = 30,
            mana = 10,
            healingPower = 30,
            level = CharacterLevel.LEVEL_1
        ), // Tanky with healing ability
    )

    private val starWarsCharacters = listOf(
        Warrior(
            name = "Luke Skywalker",
            health = 110,
            attackPower = 40,
            stamina = 20,
            defensePower = 30,
            level = CharacterLevel.LEVEL_2
        ), // Balanced, well-rounded
        Warrior(
            name = "Yoda",
            health = 80,
            attackPower = 30,
            stamina = 50,
            defensePower = 40,
            level = CharacterLevel.LEVEL_1
        ), // High mana, strategic healer
        Warrior(
            name = "Han Solo",
            health = 120,
            attackPower = 40,
            stamina = 10,
            defensePower = 30,
            level = CharacterLevel.LEVEL_2
        ), // Tanky and reliable
        Warrior(
            name = "Darth Vader",
            health = 100,
            attackPower = 60,
            stamina = 10,
            defensePower = 30,
            level = CharacterLevel.LEVEL_1
        ), // High health and power, needs healing
        Warrior(
            name = "Obi-Wan Kenobi",
            health = 100,
            attackPower = 40,
            stamina = 30,
            defensePower = 30,
            level = CharacterLevel.LEVEL_3
        ), // Strong defense and healing
        Warrior(
            name = "Emperor Palpatine",
            health = 80,
            attackPower = 80,
            stamina = 10,
            defensePower = 30,
            level = CharacterLevel.LEVEL_1
        ), // Strong attack, low healing
        Warrior(
            name = "Mace Windu",
            health = 110,
            attackPower = 40,
            stamina = 20,
            defensePower = 30,
            level = CharacterLevel.LEVEL_3
        ), // Well-rounded with decent attack
        Warrior(
            name = "Darth Maul",
            health = 90,
            attackPower = 60,
            stamina = 20,
            defensePower = 30,
            level = CharacterLevel.LEVEL_2
        ), // Fast and aggressive, needs support
        Warrior(
            name = "Kylo Ren",
            health = 100,
            attackPower = 50,
            stamina = 20,
            defensePower = 30,
            level = CharacterLevel.LEVEL_2
        ), // Balanced with raw power
        Warrior(
            name = "Finn",
            health = 130,
            attackPower = 20,
            stamina = 10,
            defensePower = 40,
            level = CharacterLevel.LEVEL_1
        ), // High health and resilience, strategic healing
    )

    fun getCharacters() = harryPotterCharacters + starWarsCharacters
}

abstract class Character(val name: String, var health: Int, val attackPower: Int) {
    abstract val level: CharacterLevel

    val characterClass: String get() = this::class.simpleName ?: "Unknown"

    open fun receiveAttack(attackPower: Int) {
        health -= attackPower
        if (health <= 0) {
            health = 0
            println("$name has been defeated")
        } else {
            println("$name has $health health remaining")
        }
    }

    abstract fun attack(target: Character)
}

interface Defender {
    val name: String
    var stamina: Int
    val defensePower: Int
    fun defend(attackPower: Int): Int
}

interface Healer {
    var mana: Int
    val healingPower: Int
    fun heal()
}

class Warrior(
    name: String,
    health: Int,
    attackPower: Int,
    override var stamina: Int = 3,
    override val defensePower: Int = 5,
    override val level: CharacterLevel
) : Character(name, health, attackPower), Defender {
    override fun defend(attackPower: Int): Int {
        return if (stamina <= 0) {
            println("$name is too tired to defend")
            attackPower
        } else {
            println("$name raises shield and defends against $defensePower damage")
            stamina -= 1
            (attackPower - defensePower).coerceAtLeast(0)
        }
    }

    override fun receiveAttack(attackPower: Int) {
        super.receiveAttack(defend(attackPower))
    }

    override fun attack(target: Character) {
        if (health <= 0) {
            println("$name is dead and cannot attack")
        } else if (stamina <= 0) {
            println("$name is too tired to attack")
        } else {
            println("$name swings a sword at ${target.name}")
            target.receiveAttack(attackPower)
            stamina -= 1
        }
    }
}

class Sorcerer(
    name: String,
    health: Int,
    attackPower: Int,
    override var mana: Int = 3,
    override val healingPower: Int = 10,
    override val level: CharacterLevel
) : Character(name, health, attackPower), Healer {
    override fun heal() {
        if (mana <= 0) {
            println("$name is out of mana")
        } else {
            mana -= 1
            health += healingPower
            println("$name heals self to $health health")
        }
    }

    override fun attack(target: Character) {
        if (health <= 0) {
            println("$name is dead and cannot attack")
        } else {
            // Heal before attacking
            heal()
            if (mana <= 0) {
                println("$name is out of mana")
            } else {
                println("$name casts a spell at ${target.name}")
                target.receiveAttack(attackPower)
                mana -= 1
            }
        }
    }
}

interface CharacterService {
    fun findChallenger(strategy: CharacterMatchingStrategy? = null): Character
    fun findOpponent(challenger: Character, strategy: CharacterMatchingStrategy? = null): Character
}

class RandomCharacterService : CharacterService {
    private val characters = CharacterRepository.getCharacters()

    override fun findChallenger(strategy: CharacterMatchingStrategy?): Character {
        val filtered = filterCharacters(characters, strategy)
        if (filtered.isEmpty())
            error("No characters match the given strategy for challenger")
        return filtered.random()
    }

    override fun findOpponent(challenger: Character, strategy: CharacterMatchingStrategy?): Character {
        val filtered = filterCharacters(characters, strategy)
            .filter { it.name != challenger.name }
        if (filtered.isEmpty())
            error("No characters match the given strategy for opponent")
        return filtered.random()
    }

    private fun filterCharacters(
        list: List<Character>,
        strategy: CharacterMatchingStrategy?
    ): List<Character> {
        return if (strategy == null || strategy == CharacterMatchingStrategy.ANY) {
            list
        } else {
            list.filter { character ->
                (strategy.name == null || character.name.contains(strategy.name, ignoreCase = true)) &&
                        (strategy.level == null || character.level == strategy.level) &&
                        (strategy.characterClass == null || character.characterClass.equals(
                            strategy.characterClass,
                            ignoreCase = true
                        ))
            }
        }
    }
}

interface MatchService {
    fun match(rounds: Int, matchingStrategy: CharacterMatchingStrategy? = null): MatchResult
}

class RandomMatchService(private val characterService: CharacterService) : MatchService {
    override fun match(rounds: Int, matchingStrategy: CharacterMatchingStrategy?): MatchResult {
        val challenger = characterService.findChallenger(matchingStrategy)
        val opponent = characterService.findOpponent(challenger, matchingStrategy)
        println(
            "Challenger: ${challenger.name} (H: ${challenger.health} | ${challenger.level} | ${challenger.characterClass} | Atk: ${challenger.attackPower})"
        )
        println(
            "Opponent: ${opponent.name} (H: ${challenger.health} | ${opponent.level} | ${opponent.characterClass} | Atk: ${opponent.attackPower})"
        )

        var currentRound = 0
        while (challenger.health > 0 && opponent.health > 0 && currentRound < rounds) {
            currentRound++
            println("\nROUND $currentRound:")
            challenger.attack(opponent)
            if (opponent.health > 0) {
                opponent.attack(challenger)
            }
        }
        val winner = when {
            challenger.health > opponent.health -> challenger
            opponent.health > challenger.health -> opponent
            else -> null
        }
        return MatchResult(roundsFought = currentRound, winner = winner)
    }
}

class Game(private val matchService: MatchService) {
    fun playMatch(rounds: Int, strategy: CharacterMatchingStrategy? = null) {
        val result = matchService.match(rounds, strategy)
        println("\nMatch over after ${result.roundsFought} rounds!")
        result.winner?.let {
            println("The winner is ${it.name}!")
        } ?: println("The match ended in a draw.")
    }
}

object GameFactory {
    fun createGame(): Game {
        val characterService = RandomCharacterService()
        val matchService = RandomMatchService(characterService)
        return Game(matchService)
    }
}


fun main() {
    val game = GameFactory.createGame()
    game.playMatch(20, CharacterMatchingStrategy.ANY)
}
