package minesweeper

class Cell(
    var isMined: Boolean = false,
    var hasNumber: Boolean = false,
    var playerMarked: Boolean = false,
    var revealOnPrint: Boolean = false,
    private var minesInProximity : Int = 0
    ) {
    // Plant mine in the cell
    fun mineCell() {
        isMined = true
    }
    // Set cells proximity to mines
    fun setMinesInProximity(num: Int) {
        if(num != 0) {
            hasNumber = true
            minesInProximity = num
        }

    }
    // Override toString for printing cell state
    override fun toString(): String {
        return when {
            (revealOnPrint && isMined) -> "X"
            (playerMarked) -> "*"
            (revealOnPrint && hasNumber) -> minesInProximity.toString()
            (revealOnPrint) -> "/"
            else -> "."
        }
    }



}