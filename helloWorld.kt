
import java.util.Scanner
abstract class Random


fun main() {
    // Create a playground/sandbox area
	// Create an item within the sandbox, has a position
	// allow user to move item with input, (can just use readline)
	
	displayBoard(createBoard())
	
	//println("Take a color!")
    //val color = getColor()
    //println(color)
}

// Creates a 2D array board
fun createBoard() : Array<Array<String>> {
	//var arr = IntArray(4)
	val boardWidth = 15
	val boardHeight = 7

	val boardEdgeHorizontal = "-"
	val boardCorner = "+"
	val boardEdgeVertical = "|"
	val characterChar = "X"

	// Creates an empty 2D array of Strings TODO: Change to chars?
	var board = Array(boardHeight) {i-> Array(boardWidth) {i-> " "} }

	// For every row in the board, fill it in
	for (row in 0..board.size - 1) {
		// Create the top and bottom edges of the board
		if (row == 0 || row == board.size - 1) {
			// The corners of the board
			board[row][0] = boardCorner
			board[row][(board[row].size - 1)] = boardCorner

			// Fill in the space between
			for (col in 1..board[row].size - 2) {
				board[row][col] = boardEdgeHorizontal
			}
		} else {
			// Create the borders for the left and right
			board[row][0] = boardEdgeVertical
			board[row][(board[row].size - 1)] = boardEdgeVertical

			// For the middle row,
			if (row == (board.size / 2)) {
				// Place an X in the middle column
				board[row][board[row].size / 2] = characterChar
			}
		}
	}

	return board
}

// Prints the given 2D array board
fun displayBoard(board : Array<Array<String>>) {
	for (row in board) {
		for (col in row) print(col)
		println()
	}
}

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













