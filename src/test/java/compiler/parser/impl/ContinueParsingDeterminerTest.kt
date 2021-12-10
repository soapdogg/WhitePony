package compiler.parser.impl

import compiler.core.tokenizer.TokenType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ContinueParsingDeterminerTest {
    private val acceptedTokenTypes = setOf(TokenType.PLUS_MINUS)

    private val continueParsingDeterminer = ContinueParsingDeterminer(acceptedTokenTypes)

    @Test
    fun shouldBreakTest() {
        val actual = continueParsingDeterminer.shouldContinueParsing(true, TokenType.TYPE, false, 0)

        Assertions.assertFalse(actual)
    }

    @Test
    fun doesNotContainTokenTest() {
        val actual = continueParsingDeterminer.shouldContinueParsing(false, TokenType.TYPE, false, 0)
        Assertions.assertFalse(actual)
    }

    @Test
    fun negativeNumberOfParenthesesTest() {
        val actual = continueParsingDeterminer.shouldContinueParsing(false, TokenType.PLUS_MINUS, false, 0)
        Assertions.assertFalse(actual)
    }

    @Test
    fun continueTest() {
        val actual = continueParsingDeterminer.shouldContinueParsing(false, TokenType.PLUS_MINUS, false, 1)
        Assertions.assertTrue(actual)
    }
}