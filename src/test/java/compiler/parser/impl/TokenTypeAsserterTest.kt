package compiler.parser.impl

import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
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
}