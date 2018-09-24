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
    if (isAllMinesMarked(fieldToShow, actualField)) {
        return true
    }
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
        if (actualField[index] in '1'..'9'){
            fieldToShow[index] = actualField[index]
        }
    }
}

fun copyMines(fieldToShow: CharArray, actualField: CharArray) {
    for (index in fieldToShow.indices) {
        if (actualField[index] == mineCell){
            fieldToShow[index] = mineCell
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

fun markFree(fieldToShow: CharArray, actualField: CharArray, x: Int, y: Int): Boolean {
    val index = (y - 1) * fieldSizeY + x - 1
    if (actualField[index] == mineCell) {
        return false
    }
    when (fieldToShow[index]) {
        in '1'..'9', guaranteedEmpty -> return true
        markedAsMine -> fieldToShow[index] = emptyCell
    }
    when (actualField[index]) {
        in '1'..'9' -> fieldToShow[index] = actualField[index]
        emptyCell -> {
            fieldToShow[index] = guaranteedEmpty
            spreadEptiness(fieldToShow, actualField)
        }
    }
    return true
}

fun spreadEptiness(fieldToShow: CharArray, actualField: CharArray) {
    var needContinueSpread = false
    for ((index, value) in fieldToShow.withIndex()) {
        if (value == guaranteedEmpty) {
            val x = index / fieldSizeY
            val y = index % fieldSizeY
            for (dx in intArrayOf(-1, 0, 1)) {
                for (dy in intArrayOf(-1, 0, 1)) {
                    val newX = x + dx
                    val newY = y + dy
                    val newIndex = newX * fieldSizeY + newY
                    if (newX in 0 until fieldSizeX &&
                        newY in 0 until fieldSizeY &&
                            fieldToShow[newIndex] == emptyCell) {
                        when (actualField[newIndex]) {
                            emptyCell -> {
                                fieldToShow[newIndex] = guaranteedEmpty
                                needContinueSpread = true
                            }
                            in '1'..'9' -> fieldToShow[newIndex] = actualField[newIndex]
                            mineCell -> throw IllegalArgumentException("Wrong cell")
                        }
                    }
                }
            }
        }
    }
    if (needContinueSpread) {
        spreadEptiness(fieldToShow, actualField)
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

    while (!isFieldEnded(fieldToShow, actualField)) {
        printField(fieldToShow)
        print("Set/unset mines marks or claim a cell as free: ")
        val x = scanner.nextInt()
        val y = scanner.nextInt()
        val action = scanner.nextLine().trim()
        when (action) {
            "mine" -> markMine(fieldToShow, x, y)
            "free" -> {
                if (!markFree(fieldToShow, actualField, x, y)) {
                    copyMines(fieldToShow, actualField)
                    printField(fieldToShow)
                    println("You stepped on a mine and failed!")
                    return
                }
            }
        }
    }
    printField(fieldToShow)
    println("Congratulations! You founded all mines!")
}