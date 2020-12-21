package aoc

import java.io.File
import java.nio.charset.Charset

fun main() {
    val foods = parseInput()

    val ingredients = foods.flatMap { it.ingredients }.toSet()
    val allergens = foods.flatMap { it.allergens }.toSet()

    val ingredientCount = mutableMapOf<String, Int>()
    for (food in foods) {
        for (ingredient in food.ingredients) {
            ingredientCount.merge(ingredient, 1, Int::plus)
        }
    }

    val allergenMap = mutableMapOf<String, Set<String>>()
    for (allergen in allergens) {
        allergenMap[allergen] = HashSet(ingredients)
    }
    for (food in foods) {
        for (allergen in food.allergens) {
            for (ingredient in ingredients) {
                if (!food.ingredients.contains(ingredient)) {
                    val ings = HashSet(allergenMap.getOrDefault(allergen, setOf()))
                    ings.remove(ingredient)
                    allergenMap[allergen] = ings
                }
            }
        }
    }
    var result = 0
    for (ingredient in ingredients) {
        var giveUp = false
        for (a in allergenMap.values) {
            if (a.contains(ingredient)) {
                giveUp = true
                break
            }
        }
        if (giveUp) {
            continue
        }
        result += ingredientCount.getOrDefault(ingredient, 0)
    }
    println("Part 1: $result")

    allergenMap.prune2()
    println("Part 2: " + allergenMap.toSortedMap().values.flatten().joinToString(","))
}

private data class Food(val ingredients: Set<String>, val allergens: Set<String>)

private fun parseInput(): List<Food> {
    val lines = File(ClassLoader.getSystemResource("input_day21.txt").file).readLines(Charset.defaultCharset())
    val result = mutableListOf<Food>()
    for (line in lines) {
        val ingredients = line.split("(")[0].trim().split(" ").toSet()
        val allergens = line.split("(")[1]
            .replace("contains", "")
            .replace(")", "")
            .trim()
            .split(", ")
            .toSet()
        result.add(Food(ingredients, allergens))
    }
    return result
}