import java.util.Scanner

enum class Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE, BLACK
}

fun getColor(): Color {
    val ranMin: Int = 0
    val ranMax = Color.values().size
    val c = (ranMin..ranMax).random()
    return when (c) {
        1 -> Color.RED
        2 -> Color.ORANGE
        3 -> Color.YELLOW
        4 -> Color.GREEN
        5 -> Color.BLUE
        else -> {
            Color.PURPLE
        }
    }
}

fun testingMore() {
    val language: String // Type specified, immutable and cannot be changed later
    language = "French"

    // Numbers, can specifcy :Number if mixes use of int/floating (for a var)

    var score: Int // mutable, can be changed
    score = 95

    // Bytes: Must be between these values (8-bit)
    val minByte: Byte = -128
    val maxByte: Byte = 127

    // Short: Must be between these values (16-bit)
    val minShort: Short = -32768
    val maxShort: Short = 32767

    // Int: Larger, used if no type specified (32-bit)
    val testInt = 1 // But specifying Byte would save memory
    println("$testInt") // To print a value

    // Long: Even larger than Int (64-bit)
    // Up to 2^63 -1

    // Double: allows for decimals, double-precision (64-bit)
    // Float: allows for decimals, single-precision, smaller (32-bit)
    // Must use F to indicate float
    val doubleDistance = 2450.23
    val floatDistance = 2450.23F


    print("string inside quotes")
    println("Puts cursor on next line") // Similar to write/writeline

    val userInput = readLine()!!

    // Reads the next integer from the keyboard
    val reader = Scanner(System.`in`)
    var integer: Int = reader.nextInt()

    // Specialty of if statement
    val number = -10
    var result = if (number > 0) {
        "Positive"
    } else {
        "Negative"
    }

    // Or write as follows, similar to ternary operator
    result = if (number > 0) "Positive" else "Negative"
}