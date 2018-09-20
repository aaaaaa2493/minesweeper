import java.util.*

fun main(args: Array<String>) {

    val fieldSizeX = 9
    val fieldSizeY = 9
    val pointsCount = 10

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
        if (index % fieldSizeY == 0 && index != 0) {
            println()
        }

        print(value)
    }

}