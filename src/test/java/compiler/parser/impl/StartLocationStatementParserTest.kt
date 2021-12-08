package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IStatementParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class StartLocationStatementParserTest {
    private val tokenType = TokenType.RETURN
    private val parser = Mockito.mock(IStatementParser::class.java)
    private val tokenTypeToParserMap = mapOf(tokenType to parser)
    private val startExpressionStatementParser = Mockito.mock(IStatementParser::class.java)

    private val startLocationStatementParser = StartLocationStatementParser(
        tokenTypeToParserMap,
        startExpressionStatementParser
    )

    @Test
    fun mapContainsTest() {
        val token = Mockito.mock(Token::class.java)
        Mockito.`when`(token.type).thenReturn(tokenType)
        val tokens = listOf(token)
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val expected = 23
        Mockito.`when`(parser.parse(
            tokens,
            tokenPosition,
            stack,
            resultStack,
            expressionStack,
            numberOfStatementsBlockStack
        )).thenReturn(expected)

        val actual = startLocationStatementParser.parse(
            tokens,
            tokenPosition,
            stack,
            resultStack,
            expressionStack,
            numberOfStatementsBlockStack
        )
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun mapDoesNotContainTest() {
        val token = Mockito.mock(Token::class.java)
        Mockito.`when`(token.type).thenReturn(TokenType.TYPE)
        val tokens = listOf(token)
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val expected = 233
        Mockito.`when`(startExpressionStatementParser.parse(
            tokens,
            tokenPosition,
            stack,
            resultStack,
            expressionStack,
            numberOfStatementsBlockStack
        )).thenReturn(expected)

        val actual = startLocationStatementParser.parse(
            tokens,
            tokenPosition,
            stack,
            resultStack,
            expressionStack,
            numberOfStatementsBlockStack
        )
        Assertions.assertEquals(expected, actual)
    }
}