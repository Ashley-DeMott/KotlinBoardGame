abstract class Random

fun main() {
    println("Take a color!")
    val color = getColor()
    println(color)
}

enum class Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE, BLACK
}

fun getColor(): Color {
    val ranMin = 0
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