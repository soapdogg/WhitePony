package compiler.frontend.tokenizer.impl

import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenTypeRegexPattern
import compiler.frontend.tokenizer.impl.internal.IMatchFinder

internal class MatchFinder (
    private val tokenTypeRegexPatterns: List<TokenTypeRegexPattern>
): IMatchFinder {

    override fun findMatch(word: String): Pair<Token?, String> {
        for(it in tokenTypeRegexPatterns) {
            val matchResult = it.regex.find(word)
            if (matchResult != null) {
                val matchingValue = matchResult.value
                val token = Token(
                    matchingValue,
                    it.type
                )
                val remainingText = word.substring(matchingValue.length).trim()
                return Pair(token, remainingText)
            }
        }
        return Pair(null, word)
    }
}