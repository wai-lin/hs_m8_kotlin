package org.example.fantsy

abstract class Character(val name: String, var health: Int, val attackPower: Int) {
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
) : Character(name, health, attackPower), Defender {
    override fun defend(attackPower: Int): Int {
        return if (stamina <= 0) {
            println("$name is too tired to defend")
            attackPower
        } else {
            println("$name raises shield and defends against $defensePower damage")
            stamina -= 1
            attackPower - defensePower
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
    override var mana: Int = 3
) : Character(name, health, attackPower), Healer {
    override val healingPower: Int = 10

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
            // Check if there is enough mana to attack after healing
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

fun main() {
    match(
        character1 = Warrior(name = "Thorne Ironfist", health = 100, attackPower = 10),
        character2 = Sorcerer(name = "Eldrin Starfire", health = 50, attackPower = 20)
    )
}

internal fun match(character1: Character, character2: Character) {
    var round = 0
    while (character1.health > 0 && character2.health > 0 && round < 10) {
        round++
        println("\nROUND $round:")
        character1.attack(character2)
        character2.attack(character1)
    }
    when {
        character1.health <= 0 && character2.health > 0 -> println("\n${character2.name} is the victor in round $round!")
        character2.health <= 0 && character1.health > 0 -> println("\n${character2.name} is the victor in round $round!")
        else -> println("\nIt's a draw!")
    }
}