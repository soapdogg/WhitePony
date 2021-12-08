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

class StartForStatementParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val recursiveExpressionParser = Mockito.mock(IExpressionParser::class.java)
    private val shiftReduceExpressionParser = Mockito.mock(IExpressionParser::class.java)

    private val startForStatementParser = StartForStatementParser(
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

        val positionAfterFor = 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.FOR)).thenReturn(Pair(token, positionAfterFor))

        val positionAfterLeftParentheses = 2
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, positionAfterFor, TokenType.LEFT_PARENTHESES)).thenReturn(Pair(token, positionAfterLeftParentheses))

        val initExpression = Mockito.mock(IParsedExpressionNode::class.java)
        val positionAfterInitExpression = 3
        Mockito.`when`(shiftReduceExpressionParser.parse(tokens, positionAfterLeftParentheses)).thenReturn(Pair(initExpression, positionAfterInitExpression))

        val positionAfterFirstSemi = 4
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, positionAfterInitExpression, TokenType.SEMICOLON)).thenReturn(Pair(token, positionAfterFirstSemi))

        val testExpression = Mockito.mock(IParsedExpressionNode::class.java)
        val positionAfterTestExpression = 5
        Mockito.`when`(shiftReduceExpressionParser.parse(tokens, positionAfterFirstSemi)).thenReturn(Pair(testExpression, positionAfterTestExpression))

        val positionAfterSecondSemi = 6
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, positionAfterTestExpression, TokenType.SEMICOLON)).thenReturn(Pair(token, positionAfterSecondSemi))

        val incrementExpression = Mockito.mock(IParsedExpressionNode::class.java)
        val positionAfterIncrementExpression = 7
        Mockito.`when`(shiftReduceExpressionParser.parse(tokens, positionAfterSecondSemi)).thenReturn(Pair(incrementExpression, positionAfterIncrementExpression))

        val positionAfterRightParentheses = 8
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, positionAfterIncrementExpression, TokenType.RIGHT_PARENTHESES)).thenReturn(Pair(token, positionAfterRightParentheses))

        val actual = startForStatementParser.parse(
            tokens,
            tokenPosition,
            stack,
            resultStack,
            expressionStack,
            numberOfStatementsBlockStack
        )
        Assertions.assertEquals(positionAfterRightParentheses, actual)

        val startLocation = stack.pop()
        Assertions.assertEquals(StatementParserLocation.LOCATION_START, startLocation)
        val forLocation = stack.pop()
        Assertions.assertEquals(StatementParserLocation.LOCATION_FOR, forLocation)

        val init = expressionStack.pop()
        Assertions.assertEquals(initExpression, init)
        val test = expressionStack.pop()
        Assertions.assertEquals(testExpression, test)
        val increment = expressionStack.pop()
        Assertions.assertEquals(incrementExpression, increment)
    }
}