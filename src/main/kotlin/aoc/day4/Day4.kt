package aoc.day4

import java.io.File
import java.nio.charset.Charset

fun main() {
    val input = loadInput("input_day4.txt")
    println(input.count { it.isValid2() })
}

data class Passport(
    val byr: Int?,
    val iyr: Int?,
    val eyr: Int?,
    val hgt: String?,
    val hcl: String?,
    val ecl: String?,
    val pid: String?,
    val cid: Int?
) {
    fun isValid(): Boolean {
        return listOf(byr, iyr, eyr, hgt, hcl, ecl, pid).all { it != null }
    }

    fun isValid2(): Boolean {
        if (isValid()) {
            if (byr!! < 1920 || byr > 2002) return false
            if (iyr!! < 2010 || iyr > 2020) return false
            if (eyr!! < 2020 || eyr > 2030) return false
            if (hgt == null) return false
            if (hgt.contains("cm")) {
                val i = hgt.replace("cm", "").toInt()
                if (i < 150 || i > 193) return false
            } else if (hgt.contains("in")) {
                val i = hgt.replace("in", "").toInt()
                if (i < 59 || i > 76) return false
            } else {
                return false
            }
            if (hcl?.matches("^#([a-f0-9]{6})\$".toRegex()) == false) return false
            if (listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").none { it == ecl }) return false
            if (pid?.matches("^\\d{9}\$".toRegex()) == false) return false
            return true
        }
        return false
    }
}

fun loadInput(inputFile: String): List<Passport> {
    val text = File(ClassLoader.getSystemResource(inputFile).file).readText(Charset.defaultCharset())
    val result = mutableListOf<Passport>()
    text.split("\n\n").forEach { input ->
        run {
            val map = HashMap<String, String>()
            input.replace("\n", " ")
                .split(" ")
                .map { f -> f.split(":") }
                .map { g -> map.put(g[0], g[1]) }
            result.add(
                Passport(
                    map["byr"]?.toInt(),
                    map["iyr"]?.toInt(),
                    map["eyr"]?.toInt(),
                    map["hgt"],
                    map["hcl"],
                    map["ecl"],
                    map["pid"],
                    map["cid"]?.toInt()
                )
            )
        }
    }
    return result
}