package minesweeper
import kotlin.random.Random

// Minefield class
class Minefield(
    val width: Int,
    val height: Int,
    private val mines: Int
    ) {
    // Generate 2d array of cells for the minefield
    val minefieldArray: Array<Array<Cell>> = Array(height){
        y -> Array(width) {
            x -> Cell()
        }
    }
    // Plant mines on initialisation of the minefield
    init {
        plantMines()
    }
    // Plant mines in the cells
    private fun plantMines() {
        // loop selecting random cells to plant mines in until number of desired mines have been planted
        var minesToPlant = mines
        while(minesToPlant > 0) {
            val randomCell = minefieldArray[Random.nextInt(0, height)][Random.nextInt(0, width)]
            if (!randomCell.isMined) {
                randomCell.mineCell()
                minesToPlant--
            }
        }
    }
    // Detect mines in surrounding cells
    private fun detectMines(height: Int, width: Int): Int {
        var minesDetected = 0
        // loop through surrounding sell to the cell given in parameter
        // - if out of bound exception is caught just continue with loops as its due to reaching cells on the edge of the minefield
        for(j in -1..1 ) {
            for (i in -1..1) {
                try {
                    if(minefieldArray[j + height][i + width].isMined) minesDetected++
                } catch(e: Exception) {
                    continue
                }
            }
        }
        return minesDetected
    }
    // override toString method to print minefield with cells mined or their proximity to mines
    override fun toString(): String {
        // messy maybe could do with some re-thinking
        var str = " |"
        repeat(width) {str+= it + 1}
        str += "|\n—|${"—".repeat(width)}|\n"
        for(y in 0 until height) {
            str += "${y+1}|"
            for (i in 0 until width) {
                // If cell is not mined, detect mines in surrounding cells and set their proximity
                if(!minefieldArray[y][i].isMined) minefieldArray[y][i].setMinesInProximity(detectMines(y, i))
                str += minefieldArray[y][i]
            }
            str += "|\n"
        }
        str += "—|${"—".repeat(width)}|"
        return str
    }


}