package compiler.frontend.tokenizer.impl

import compiler.core.tokenizer.TokenType
import compiler.core.tokenizer.TokenTypeRegexPattern
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MatchFinderTest {
    private val returnRegexPattern = Regex("\\breturn\\b")
    private val returnTokenTypeRegexPattern = TokenTypeRegexPattern(
        returnRegexPattern,
        TokenType.RETURN
    )

    private val tokenTypeRegexPatterns = listOf(returnTokenTypeRegexPattern)

    private val matchFinder = MatchFinder(
        tokenTypeRegexPatterns
    )

    @Test
    fun findsMatchTest() {
        val input = "return"
        val (token, remainingText) = matchFinder.findMatch(input)

        Assertions.assertEquals(input, token!!.value)
        Assertions.assertEquals(TokenType.RETURN, token.type)
        Assertions.assertTrue(remainingText.isEmpty())
    }

    @Test
    fun doesNotFindMatchTest() {
        val input = "input"
        val (token, remainingText) = matchFinder.findMatch(input)

        Assertions.assertNull(token)
        Assertions.assertEquals(input, remainingText)
    }
}