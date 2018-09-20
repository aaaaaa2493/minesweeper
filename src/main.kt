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
        if (index % fieldSizeY == 0) {
            println()
        }

        print(value)
    }

}