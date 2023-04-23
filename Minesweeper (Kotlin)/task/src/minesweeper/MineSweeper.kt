package minesweeper
import java.util.Scanner

// Game Class
class MineSweeper {
    // Game over boolean for game loop
    private var gameOver = false
    // Correct Detections
    private var correctDetections = 0
    // False Detections
    private var falseDetections = 0
    // Run the game function
    private val mines = demandAmountOfMines()
    fun run() {
        if (mines > 0) {
            val minefield = Minefield(9, 9, mines)
            do {
                println(minefield)
                setDeleteMineMarks(minefield)
                gameOverCheck()
            } while(!gameOver)

        }
    }
    // Ask for amount of mines desired for the game
    private fun demandAmountOfMines(): Int {
        println("How many mines do you want on the field?")
        try {
            return readln().trim().toInt()
        } catch(e: Exception) {
            println("Invalid Input!")
        }
        return 0
    }

    // Function to set or delete marks on cells based on coordinated given
    private fun setDeleteMineMarks(minefield: Minefield) {
        println("Set/delete mine marks or claim a cell as free(eg; 3 4 free; 2 5 mine):")
        val input = Scanner(System.`in`)
        val x = input.nextInt()
        val y = input.nextInt()
        val command = input.next().trim().lowercase()
        when {
            (x > minefield.width || x < 1) -> return println("Invalid input!")
            (y > minefield.height || y < 1) -> return println("Invalid input!")
            (command == "free") -> return freeCellSelection(y-1, x-1, minefield)
            (command == "mine") -> return markMine(y-1, x-1, minefield)
            else -> println("invalid input")
        }
    }

    // Free cell selection
    private fun freeCellSelection(height: Int, width: Int, minefield: Minefield) {
        when {
            // When player selects cell that already has number return with error message
            (minefield.minefieldArray[height][width].hasNumber && minefield.minefieldArray[height][width].revealOnPrint) -> return println("There is a number here!")
            // When player steps on a mine, game over
            (minefield.minefieldArray[height][width].isMined) -> {
                return steppedOnMine(minefield)
            }
            else -> {
                setCellsToReveal(height, width, minefield)
            }
        }
    }

    // Mark mine selection
    private fun markMine(height: Int, width: Int, minefield: Minefield) {
        when {
            // When player wants to un mark a mine
            minefield.minefieldArray[height][width].playerMarked -> {
                minefield.minefieldArray[height][width].playerMarked = false
                when {
                    minefield.minefieldArray[height][width].isMined -> correctDetections--
                    else -> falseDetections--
                }
            }
            // When play wants to mark a mine
            else -> {
                when {
                    // Already shown as cell with number
                    (minefield.minefieldArray[height][width].hasNumber && minefield.minefieldArray[height][width].revealOnPrint) -> return println("There is a number there!")
                    // Already shown as a free cell
                    (minefield.minefieldArray[height][width].revealOnPrint) -> return println("The cell is shown as empty!")
                    else -> {
                        minefield.minefieldArray[height][width].playerMarked = true
                        when {
                            minefield.minefieldArray[height][width].isMined -> correctDetections++
                            else -> falseDetections++
                        }

                    }
                }
            }
        }
    }

    // Stepped on a mine function
    private fun steppedOnMine(minefield: Minefield) {
        // loop through minefield array to set all mines to reveal on print
        for(y in 0 until minefield.height) {
            for(i in 0 until minefield.width) {
                if (minefield.minefieldArray[y][i].isMined) minefield.minefieldArray[y][i].revealOnPrint = true
            }
        }
        println(minefield)
        println("You stepped on a mine and failed!")
        gameOver = true

    }

    // Set cells to reveal on print
    private fun setCellsToReveal(height: Int, width: Int, minefield: Minefield) {
        // If cell is marked incorrectly by player as mine
        if (minefield.minefieldArray[height][width].playerMarked && !minefield.minefieldArray[height][width].isMined) {
            printOverCell(height, width, minefield)
        }
        // if selected cell is already set to reveal, return
        if (minefield.minefieldArray[height][width].revealOnPrint) return
        // if selected cell is a mine, return
        if (minefield.minefieldArray[height][width].isMined) return
        // if selected cell has a number, set it to be revealed on print, then return
        if (minefield.minefieldArray[height][width].hasNumber) {
            minefield.minefieldArray[height][width].revealOnPrint = true
            return
        }
        // if selected cell has no number,
        // set it to be revealed then loop to call this function recursively on surrounding cells
        if (!minefield.minefieldArray[height][width].hasNumber) {
            minefield.minefieldArray[height][width].revealOnPrint = true
            for (y in -1..1) {
                for (i in -1..1) {
                    // if out of bounds exception is found due to hitting edge of field, set loop to continue
                    try {
                        setCellsToReveal(y + height, i + width, minefield )
                    } catch (e: Exception) {
                        continue
                    }
                }
            }
        }
    }

    // Print over cell marked incorrectly by player
    private fun printOverCell(height: Int, width: Int, minefield: Minefield) {
        minefield.minefieldArray[height][width].playerMarked = false
        falseDetections--
        correctDetections
    }

    // Check to see if game has been won
    private fun gameOverCheck() {
        if (correctDetections == mines && falseDetections == 0) {
            println("Congratulations! You found all the mines!")
            gameOver = true
        }
    }
}