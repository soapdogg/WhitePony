package compiler.frontend.tokenizer.impl

import compiler.core.tokenizer.Token
import compiler.frontend.tokenizer.impl.internal.IMatchFinder
import compiler.frontend.tokenizer.impl.internal.IWordTokenizer
import java.lang.Exception

internal class WordTokenizer(
    private val matchFinder: IMatchFinder
) : IWordTokenizer {
    override fun tokenizeWord(word: String): List<Token> {
        val result = mutableListOf<Token>()
        var remainingText = word.trim()
        while (remainingText.isNotEmpty()) {

            val match = matchFinder.findMatch(remainingText)
            if (match.first != null) {
                result.add(match.first!!)
                remainingText = match.second
            } else {
                throw Exception("No matching token types found!")
            }
        }
        return result
    }
}