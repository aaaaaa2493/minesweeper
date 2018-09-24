import java.util.*

const val emptyCell = '.'
const val guaranteedEmpty = '/'
const val markedAsMine = '*'
const val mineCell = 'X'

const val fieldSizeX = 9
const val fieldSizeY = 9

fun addMines(field: CharArray, pointsCount: Int) {
    val random = Random()
    var points = 0
    while (points != pointsCount) {
        val index = random.nextInt(field.size)
        if (field[index] == emptyCell) {
            field[index] = mineCell
            points++
        }
    }
}

fun addNumbers(field: CharArray) {
    for ((index, value) in field.withIndex()) {
        if (value == emptyCell) {
            var minesAround = 0

            val x = index / fieldSizeY
            val y = index % fieldSizeY
            for (dx in intArrayOf(-1, 0, 1)) {
                for (dy in intArrayOf(-1, 0, 1)) {
                    val newX = x + dx
                    val newY = y + dy
                    if (newX in 0 until fieldSizeX &&
                            newY in 0 until fieldSizeY &&
                            field[newX * fieldSizeY + newY] == mineCell) {
                        minesAround++
                    }
                }
            }

            if (minesAround > 0) {
                field[index] = '0' + minesAround
            }
        }
    }
}

fun printField(field: CharArray) {
    println()
    println(" │123456789│")
    print("—│—————————")

    for ((index, value) in field.withIndex()) {
        if (index % fieldSizeY == 0) {
            println("│")
            val x = index / fieldSizeY + 1
            print("$x│")
        }

        print(value)
    }

    println("│")
    println("—│—————————│")
}

fun isFieldEnded(fieldToShow: CharArray, actualField: CharArray): Boolean {
    for (index in fieldToShow.indices) {
        if (fieldToShow[index] == emptyCell && actualField[index] != mineCell) {
            return false
        }
    }
    return true
}

fun isAllMinesMarked(fieldToShow: CharArray, actualField: CharArray): Boolean {
    for (index in fieldToShow.indices) {
        if (fieldToShow[index] == markedAsMine && actualField[index] != mineCell ||
            fieldToShow[index] != markedAsMine && actualField[index] == mineCell) {

            return false
        }
    }
    return true
}

fun copyNumbers(fieldToShow: CharArray, actualField: CharArray) {
    for (index in fieldToShow.indices) {
        if (actualField[index] in '0'..'9'){
            fieldToShow[index] = actualField[index]
        }
    }
}

fun markMine(fieldToShow: CharArray, x: Int, y: Int) {
    val index = (y - 1) * fieldSizeY + x - 1
    if (fieldToShow[index] == emptyCell) {
        fieldToShow[index] = markedAsMine
    }
    else if (fieldToShow[index] == markedAsMine){
        fieldToShow[index] = emptyCell
    }
}

fun main(args: Array<String>) {

    print("How many mines do you want on the field? ")
    val scanner = Scanner(System.`in`)
    val pointsCount = scanner.nextInt()

    val actualField = CharArray(fieldSizeX * fieldSizeY) {emptyCell}
    val fieldToShow = CharArray(fieldSizeX * fieldSizeY) {emptyCell}

    addMines(actualField, pointsCount)
    addNumbers(actualField)

    copyNumbers(fieldToShow, actualField)

    while (!isAllMinesMarked(fieldToShow, actualField)) {
        printField(fieldToShow)
        print("Set/delete mines marks (x and y coordinates): ")
        val x = scanner.nextInt()
        val y = scanner.nextInt()
        markMine(fieldToShow, x, y)
    }
    printField(fieldToShow)
    println("Congratulations! You founded all mines!")
}