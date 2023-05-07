import java.lang.AssertionError

// The Game which the user will play and interact with
class Game(private val width: Int, private val height: Int) {

	// Initialize the board, a 2D array of BoardItems
	private var board: Array<Array<BoardItem>> = Array(height) {i-> Array(width) {i-> BoardItem()} }

	// Create a Player, shown as an 'X'
	private var player: Player = Player('X')

	// If the board's axis should be labeled
	private val displayLabel = false

	// If the Game's UI should be displayed
	private val displayUI = true

	// The initialization of the Game object
	init {
		// Create a board of the given width/height
		board = this.createBoard(width, height)

		// Place the Player in the center of the board
		replaceItem((width-1)/2,(height-1)/2, player)

		// TODO: Generate based on board size, avoid player spawn
		// Place Pickups on the board
		if (width > 3 && height > 3) {
			replaceItem(1, 1, Pickup('*', 5))
			replaceItem(2, 2, Pickup('*', 5))
		}
	}

	// Moves the player to a new location on the board
	fun movePlayer(xPos: Int, yPos: Int) {
		moveItem(player, xPos, yPos)
		//player.addPoints(5) // testing points and UI
	}

	// Creates a 2D array board
	private fun createBoard(boardWidth: Int, boardHeight: Int) : Array<Array<BoardItem>> {
		// All board edges cannot be overridden
		val boardEdgeHorizontal = BoardItem('-', false)
		val boardCorner = BoardItem('+', false)
		val boardEdgeVertical = BoardItem('|', false)

		// For every row in the board, fill it in
		for (row in 0..board.size - 1) {
			// Create the top and bottom edges of the board
			if (row == 0 || row == board.size - 1) {
				// The corners of the board
				replaceItem(0, row, boardCorner)
				replaceItem(board[row].size -1, row, boardCorner)

				// Fill in the space between
				for (col in 1..board[row].size - 2) {
					replaceItem(col, row, boardEdgeHorizontal)
				}
			} else {
				// Create the borders for the left and right
				replaceItem(0, row, boardEdgeVertical)
				replaceItem(board[row].size - 1, row, boardEdgeVertical)
			}
		}
		return board
	}

	// Replaces a BoardItem at the specified location, where yPos is row, and xPos is column
	private fun replaceItem(xPos: Int, yPos: Int, newItem: BoardItem) : Boolean {
		assertValidPos(xPos, yPos) // Location must be on the board

		// Only replace the item if it isn't permanent (a wall, etc)
		if (board[yPos][xPos].isMovable())
		{
			// Remove the previous item from the board
			var removed: BoardItem = removeItem(xPos, yPos)

			// If the removed item is a Pickup,
			if (removed is Pickup) {
				// Add points to the player
				player.addPoints(removed.getPoints())
			}

			// Update the position of the newItem
			board[yPos][xPos] = newItem
			newItem.setPos(xPos, yPos)

			return true // Replaced piece successfully
		}

		// Nothing was moved/replaced
		return false
	}

	// Removes the item at a specified x,y position from the board,
	// returns the BoardItem that was removed
	private fun removeItem(xPos: Int, yPos: Int) : BoardItem {
		assertValidPos(xPos, yPos) // Must be on the board

		// If the item can be overridden,
		if (board[yPos][xPos].isMovable()) {
			// Remove the item from the board and return what was removed

			// Tell the BoardItem it is no longer on the board
			var removeItem = board[yPos][xPos]
			removeItem.setPos(-1, -1)

			// The board now has an empty BoardItem in its place
			board[yPos][xPos] = BoardItem()

			return removeItem
		}

		// Return an empty BoardItem, nothing was removed
		return BoardItem()
	}

	// Removes a specified BoardItem from the board,
	// returns an empty BoardItem if nothing was removed
	private fun removeItem(item: BoardItem) : BoardItem {
		return removeItem(item.getPosX(), item.getPosY())
	}

	// Moves the BoardItem at the specified x,y to a new spot on the board
	private fun moveItem(xFrom: Int, yFrom: Int, xTo:Int, yTo: Int) : Boolean {
		// Locations must both be on the board
		assertValidPos(xFrom, yFrom)
		assertValidPos(xTo, yTo)

		// BoardItems at both locations need to be movable
		if (board[yFrom][xFrom].isMovable() && board[yTo][xTo].isMovable()) {
			// Take the moving BoardItem and remove it from its previous location on the board
			val moveItem = removeItem(xFrom, yFrom)

			// Place the BoardItem at the new location, replacing previous item
			replaceItem(xTo, yTo, moveItem)

			return true // Move was successful
		}

		// One or move BoardItems cannot move, no changes
		return false
	}

	// Moves a specified item to a new x,y position on the board
	private fun moveItem(item: BoardItem, xTo: Int, yTo: Int) {
		moveItem(item.getPosX(), item.getPosY(), xTo, yTo)
	}

	// Asserts that the given x,y coordinate is within the bounds of the board
	// TODO: Assert failures should halt program execution/exit
	private fun assertValidPos(x: Int, y:Int){
		// Location must be on the board
		try {
			assert(y >= 0)
			assert(y < height)
			assert(x >= 0)
			assert(x < width)
		} catch (e: AssertionError) {
			print("Invalid board position")
		}
	}

	// Displays the Game's UI, including the score
	private fun displayUI() {
		print("Score: ${player.getPoints()}")
		println()
	}

	// Displays all BoardItems in the 2D array
	// TODO: Adjust spacing for height > 9
	fun displayBoard() {
		// Display the Game's UI
		if (displayUI) { displayUI() }
		var rowLabel = 0 // Starting Y label at 0

		for (row in board) {
			if (displayLabel) {
				print(rowLabel)
				rowLabel++
				print(" ")
			}

			// Display the BoardItems in the 2D array
			for (item: BoardItem in row) {
				item.display()
				print (" ")
			}
			println()
		}
		// Only display label if true
		if (displayLabel) {
			print("  ") // corner
			var rowMin: Int = minOf(9, width)
			for (i in 0..rowMin) print("$i ")

			// If the width goes into the double digits,
			if (width > 10) {
				for (i in 10 until width) {
					// Print the first digit
					val tens = i / 10
					print("$tens ")
				}

				println() // Move down a line

				// Spacing
				for (i in 0..10) print("  ")
				for (i in 10 until width) {
					// Print the next digit
					val remainder = i % 10
					print("$remainder ")
				}
			}
		}

		println()
	}
}

// An item that is displayed at a specific position on the board
// Some BoardItems are unmovable/irreplaceable, such as the board's borders
open class BoardItem(private var symbol: Char = ' ', private var movable: Boolean = true) {
	private var xPosition: Int = 0
	private var yPosition: Int = 0

	// Get and change the BoardItem's display
	fun getSymbol(): Char { return symbol }
	fun newSymbol(newSymbol: Char) { symbol = newSymbol	}

	// Returns if the BoardItem can be moved
	fun isMovable(): Boolean { return movable }

	// Get where the BoardItem is (x and y)
	fun getPosX(): Int { return xPosition }
	fun getPosY(): Int { return yPosition }

	// Update the BoardItem on where it is
	fun setPos(xPos: Int, yPos: Int) {
		xPosition = xPos
		yPosition = yPos
	}

	// Override equals operator
	override fun equals(other: Any?): Boolean {
		if (this === other) return true // Same memory address
		if (other !is BoardItem) return false // Not the same object type

		// If same position, same BoardItem
		// Assumes only one BoardItem per x,y position
		if (xPosition != other.xPosition) return false
		if (yPosition != other.yPosition) return false

		return true
	}

	// Displays the item on the board
	open fun display() {
		print(symbol)
	}
}

// A type of BoardItem that the user can control
// Can also collect points
class Player(private var s: Char) : BoardItem(s) {
	private var points: Int = 0
	fun addPoints(p: Int) {
		points += p
	}

	// Returns the player's point total
	fun getPoints(): Int { return points }

	// override equals operator
	override fun equals(other: Any?): Boolean {
		if (other !is Player) return false
		return super.equals(other)
	}
}

// A type of BoardItem worth a certain number of points
class Pickup(private var s: Char, private var points: Int) : BoardItem(s) {
	fun getPoints() : Int {
		return points
	}

	// Override equals operator
	override fun equals(other: Any?): Boolean {
		if (other !is Pickup) return false
		return super.equals(other)
	}
}

// Prompts the user for valid integer within a given range,
//  but will allow the user to exit the program with a given escape key (or 'q')
fun getUserInt(message: String, min: Int = 0, max: Int = 100, escape: String = "q") : Int? {
	while (true) {
		print(message)
		// Prompt user for a month (cast to int)
		val input = readLine()

		// Check for exit case
		if (input == escape)
			return null

		// Cast string into integer (or null if unable)
		val integer: Int? = input?.toIntOrNull()

		// Return int if it fits within the range
		if (integer != null && integer in min..max) {
			return integer
		} else {
			println("Please enter an integer between $min and $max.")
		}
	}
	return 0
}

fun main() {
	// Prompt the user for a board size (can exit by entering 'q')
	val boardWidth: Int = getUserInt("Enter a board width: ", 1, 50) ?: return
	val boardHeight: Int = getUserInt("Enter a board height: ", 1, 50) ?: return

	// Create a board of a given width and height
	var board = Game(boardWidth, boardHeight)

	// Repeats until user chooses to exit the program
	while (true) {
		// display the board
		board.displayBoard()

		// Prompt the user to move the Player to a new space, (can exit by entering 'q')
		val newX = getUserInt("Enter an x position: ", 0, boardWidth - 1) ?: return
		val newY = getUserInt("Enter a y position: ", 0, boardHeight - 1) ?: return

		// Move the player to the specified x,y position
		board.movePlayer(newX, newY)
	}
}