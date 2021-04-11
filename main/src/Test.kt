import java.io.File

object Tokenizer {
    private val doubleQuotes = "\"[^\"]*\""
    private val oneQuotes = "'[^']*'"
    private val generalRegex = Regex("($oneQuotes|$doubleQuotes)")
    var dictWordLines = HashMap<String, MutableSet<Int>>()

    private fun clearExternalQuotes(matchList: Sequence<MatchResult>): MutableList<String> {
        val tokensWithoutQuotes = mutableListOf<String>()
        val iter2 = matchList.iterator()
        while (iter2.hasNext()) {
            val token = iter2.next().value
            tokensWithoutQuotes += when {
                token[0] == '"' -> {
                    token.substring(1, token.length - 1)
                }
                token[0] == '\'' -> {
                    token.substring(1, token.length - 1)
                }
                else -> {
                    token
                }
            }
        }
        return tokensWithoutQuotes
    }

    fun matchIntoLine(line: String, countLine: Int) {
        val matchList = generalRegex.findAll(line)
        val listEl = clearExternalQuotes(matchList)
        for (el in listEl) {
            if (!dictWordLines.containsKey(el)) {
                dictWordLines[el] = mutableSetOf()
            }
            dictWordLines[el]?.add(countLine)
        }
    }

    fun beatyPrint(d: Map<String, Set<Int>>) {
        for (strCounter in d.entries) {
            var curSize = 0
            val sizeList = strCounter.value.size
            print("Lines with '${strCounter.key}': ")
            for (value in strCounter.value) {
                print("$value")
                if (curSize != sizeList - 1) {
                    print(", ")
                } else {
                    print("\n")
                }
                curSize += 1
            }
        }
    }
}


fun main() {
    val filename = "pythonfile"
    val file = File(filename)
    var countLine = 0
    file.useLines { lines ->
        lines.forEach { line ->
            Tokenizer.matchIntoLine(line, countLine)
            countLine += 1
        }
    }
    Tokenizer.beatyPrint(Tokenizer.dictWordLines.filter { it.value.size > 1 })
}
