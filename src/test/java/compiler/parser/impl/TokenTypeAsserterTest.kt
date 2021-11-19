package compiler.parser.impl

import compiler.core.Token
import compiler.core.TokenType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class TokenTypeAsserterTest {

    private val tokenTypeAsserter = TokenTypeAsserter()

    @Test
    fun throwsExceptionTest() {
        val token = Mockito.mock(Token::class.java)

        val tokens = listOf(token)

        val position = 0
        val expectedType = TokenType.TYPE
        val differentType = TokenType.BINARY_ASSIGN
        Mockito.`when`(token.type).thenReturn(differentType)

        Assertions.assertThrows(
            Exception::class.java
        ){
            tokenTypeAsserter.assertTokenType(tokens, position, expectedType)
        }
    }

    @Test
    fun doesNotThrowExceptionTest() {
        val token = Mockito.mock(Token::class.java)

        val tokens = listOf(token)

        val position = 0
        val expectedType = TokenType.TYPE
        Mockito.`when`(token.type).thenReturn(expectedType)

        Assertions.assertDoesNotThrow{
            tokenTypeAsserter.assertTokenType(tokens, position, expectedType)
        }
    }

    @Test
    fun setThrowsExceptionTest() {
        val token = Mockito.mock(Token::class.java)

        val tokens = listOf(token)

        val position = 0
        val expectedType = TokenType.TYPE
        val differentType = TokenType.BINARY_ASSIGN
        Mockito.`when`(token.type).thenReturn(differentType)

        Assertions.assertThrows(
            Exception::class.java
        ){
            tokenTypeAsserter.assertTokenType(tokens, position, setOf(expectedType))
        }
    }

    @Test
    fun setDoesNotThrowExceptionTest() {
        val token = Mockito.mock(Token::class.java)

        val tokens = listOf(token)

        val position = 0
        val expectedType = TokenType.TYPE
        Mockito.`when`(token.type).thenReturn(expectedType)

        Assertions.assertDoesNotThrow{
            tokenTypeAsserter.assertTokenType(tokens, position, setOf(expectedType))
        }
    }

    @Test
    fun valueThrowsExceptionTest() {
        val token = Mockito.mock(Token::class.java)

        val tokens = listOf(token)

        val position = 0
        val expectedValue = "expected"
        val differentValue = "different"
        Mockito.`when`(token.value).thenReturn(differentValue)

        Assertions.assertThrows(
            Exception::class.java
        ){
            tokenTypeAsserter.assertTokenValue(tokens, position, setOf(expectedValue))
        }
    }

    @Test
    fun valueDoesNotThrowExceptionTest() {
        val token = Mockito.mock(Token::class.java)

        val tokens = listOf(token)

        val position = 0
        val expectedValue = "expected"
        Mockito.`when`(token.value).thenReturn(expectedValue)

        Assertions.assertDoesNotThrow{
            tokenTypeAsserter.assertTokenValue(tokens, position, setOf(expectedValue))
        }
    }
}