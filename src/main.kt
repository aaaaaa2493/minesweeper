import java.util.*

fun main(args: Array<String>) {

    val fieldSizeX = 9
    val fieldSizeY = 9

    print("How many mines do you want on the field? ")
    val scanner = Scanner(System.`in`)
    val pointsCount = scanner.nextInt()

    val field = CharArray(fieldSizeX * fieldSizeY) {'.'}

    val random = Random()
    var points = 0
    while (points != pointsCount) {

        val x = random.nextInt(fieldSizeX)
        val y = random.nextInt(fieldSizeY)

        if (field[x * fieldSizeY + y] == '.') {
            field[x * fieldSizeY + y] = 'X'
            points++
        }

    }

    for ((index, value) in field.withIndex()) {
        if (value == '.') {
            var minesAround = 0

            val x = index / fieldSizeY
            val y = index % fieldSizeY
            for (dx in intArrayOf(-1, 0, 1)) {
                for (dy in intArrayOf(-1, 0, 1)) {
                    val newX = x + dx
                    val newY = y + dy
                    if (newX in 0 until fieldSizeX &&
                        newY in 0 until fieldSizeY &&
                            field[newX * fieldSizeY + newY] == 'X') {
                        minesAround++
                    }
                }
            }

            if (minesAround > 0) {
                field[index] = '0' + minesAround
            }
        }
    }

    for ((index, value) in field.withIndex()) {
        if (index % fieldSizeY == 0) {
            println()
        }

        print(value)
    }

}