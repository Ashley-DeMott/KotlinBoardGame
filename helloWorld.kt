abstract class Random

// Create Board class
// Board has a Character (class)?
// Character is moved given user input
// Board updates Character position

// THEN make objects that can be picked up
// If Character.position == Pickup.position
// UI <-- update score/counter, etc

// Game has a UI, board, etc
// 	UI has scores/counters
// 	Board has objects/characters

class Board(private val width: Int = 10, private val height: Int = 7) {

	// Initialize the 2D array of boardItems
	private var board: Array<Array<BoardItem>> = Array(height) {i-> Array(width) {i-> BoardItem()} }

	// Create a Player
	private var player: Player = Player('X')

	init {
		// Create a board of the given width/height
		board = this.createBoard(width, height)

		// Place the Player on the board
		replaceItem((width-1)/2,(height-1)/2, player)

		// Place Pickups on the board
	}

	// TODO: Direction or new Pos?
	fun movePlayer(xPos: Int, yPos: Int) {
		// Assert the player was correctly removed from previous location
		assert(player == removeItem(player.getPosX(), player.getPosY()))

		replaceItem(xPos, yPos, player)
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
				//board[row][0] = boardCorner
				replaceItem(board[row].size -1, row, boardCorner)
				//board[row][(board[row].size - 1)] = boardCorner

				// Fill in the space between
				for (col in 1..board[row].size - 2) {
					replaceItem(col, row, boardEdgeHorizontal)
					//board[row][col] = boardEdgeHorizontal
				}
			} else {
				// Create the borders for the left and right
				replaceItem(0, row, boardEdgeVertical)
				//board[row][0] = boardEdgeVertical
				replaceItem(board[row].size - 1, row, boardEdgeVertical)
				//board[row][(board[row].size - 1)] = boardEdgeVertical
			}
		}

		return board
	}

	// Replaces a boardItem at the specified location, where yPos is row, and xPos is column
	private fun replaceItem(xPos: Int, yPos: Int, newItem: BoardItem) {
		assertValidPos(xPos, yPos) // Location must be on the board

		// Only replace the item if it isn't permanent (a wall, etc)
		if (board[yPos][xPos].isOverridable())
		{
			// Update the position of the previous item
			var previous: BoardItem = board[yPos][xPos]
			previous.setPos(-1, -1) // No longer on the board

			// If the removed item is a Pickup,
			if (previous is Pickup) {
				// Add points to the player
				player.addPoints(previous.getPoints())
			}

			// Update the position of the newItem
			board[yPos][xPos] = newItem
			newItem.setPos(xPos, yPos)
		}
	}

	// Removes the item at a specified x,y position from the board,
	// returns an empty boardItem if nothing was removed
	private fun removeItem(xPos: Int, yPos: Int) : BoardItem {
		assertValidPos(xPos, yPos) // Must be on the board

		// If the item can be overridden,
		if (board[yPos][xPos].isOverridable()) {
			// Remove the item from the board and return what was removed
			var removeItem = board[yPos][xPos]

			// Tell the BoardItem it is no longer on the board
			removeItem.setPos(-1, -1)

			// The board now has an empty boardItem in its place
			board[yPos][xPos] = BoardItem()
			return removeItem
		}

		// Return an empty BoardItem, nothing was removed
		return BoardItem()
	}

	// Removes a specified BoardItem from the board,
	// returns an empty boardItem if nothing was removed
	private fun removeItem(item: BoardItem) : BoardItem {
		return removeItem(item.getPosX(), item.getPosY())
	}

	// Moves the BoardItem at the specified x,y to a new spot on the board
	private fun moveItem(xFrom: Int, yFrom: Int, xTo:Int, yTo: Int) {
		// Locations must both be on the board
		assertValidPos(xFrom, yFrom)
		assertValidPos(xTo, yTo)

		// Take the item and remove it from its previous location on the board
		val moveItem = removeItem(xFrom, yFrom)

		// Place the boardItem at the new location
		replaceItem(xTo, yTo, moveItem)
	}

	// Moves a specified item to a new x,y position on the board
	private fun moveItem(item: BoardItem, xTo: Int, yTo: Int) {
		moveItem(item.getPosX(), item.getPosY(), xTo, yTo)
	}

	// Asserts that the given x,y coordinate is within the bounds of the board
	private fun assertValidPos(x: Int, y:Int){
		// Location must be on the board
		assert(y >= 0)
		assert(y < height)
		assert(x >= 0)
		assert(x < width)
	}

	// Displays all BoardItems in the 2D array
	fun displayBoard() {
		for (row in board) {
			for (i: BoardItem in row) i.display()
			println()
		}
	}
}

open class BoardItem(private var symbol: Char = ' ', private var override: Boolean = true) {
	private var xPosition: Int = 0
	private var yPosition: Int = 0

	// Get and change the BoardItem's display
	fun getSymbol(): Char { return symbol }
	fun newSymbol(newSymbol: Char) { symbol = newSymbol	}

	// Returns if the BoardItem can be replaced by another
	fun isOverridable(): Boolean { return override }

	// Get where the BoardItem is (x and y)
	fun getPosX(): Int { return xPosition }
	fun getPosY(): Int { return yPosition }

	// Tell the BoardItem where it is
	fun setPos(xPos: Int, yPos: Int) {
		xPosition = xPos
		yPosition = yPos
	}

	// Override equals operator
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is BoardItem) return false

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

class Player(private var s: Char) : BoardItem(s, false) {
	var points: Int = 0
	fun addPoints(p: Int) {
		points += p
	}

	// override equals operator
	override fun equals(other: Any?): Boolean {
		if (other !is Player) return false
		return super.equals(other)
	}
}

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

fun main() {
    // Create a playground/sandbox area
	var board = Board(10, 5)

	// Create an item within the sandbox, has a position

	// allow user to move item with input, (can just use readline)
	
	board.displayBoard()


	//println("Take a color!")
    //val color = getColor()
    //println(color)
}

fun getUserInput() {
	// up
	// down
	// left
	// right

}











