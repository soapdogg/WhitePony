package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class StartWhileStatementParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val shiftReduceExpressionParser = Mockito.mock(IExpressionParser::class.java)

    private val startWhileStatementParser = StartWhileStatementParser(
        tokenTypeAsserter,
        shiftReduceExpressionParser
    )

    @Test
    fun parseTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val positionAfterWhile = 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.WHILE)).thenReturn(Pair(token, positionAfterWhile))

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        val positionAfterExpression = 2
        Mockito.`when`(shiftReduceExpressionParser.parse(tokens, positionAfterWhile)).thenReturn(Pair(expression, positionAfterExpression))

        val actual = startWhileStatementParser.parse(
            tokens,
            tokenPosition,
            stack,
            resultStack,
            expressionStack,
            numberOfStatementsBlockStack
        )
        Assertions.assertEquals(positionAfterExpression, actual)

        val locationStart = stack.pop()
        Assertions.assertEquals(StatementParserLocation.LOCATION_START, locationStart)
        val locationWhile = stack.pop()
        Assertions.assertEquals(StatementParserLocation.LOCATION_WHILE, locationWhile)

        val exp = expressionStack.pop()
        Assertions.assertEquals(expression, exp)
    }
}