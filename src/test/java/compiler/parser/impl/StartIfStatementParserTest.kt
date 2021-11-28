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

class StartIfStatementParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val expressionParser = Mockito.mock(IExpressionParser::class.java)

    private val startIfStatementParser = StartIfStatementParser(
        tokenTypeAsserter,
        expressionParser
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

        val positionAfterIf = 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.IF)).thenReturn(Pair(token, positionAfterIf))

        val booleanExpression = Mockito.mock(IParsedExpressionNode::class.java)
        val positionAfterBooleanExpression = 2
        Mockito.`when`(expressionParser.parse(tokens, positionAfterIf)).thenReturn(Pair(booleanExpression, positionAfterBooleanExpression))

        val actual = startIfStatementParser.parse(tokens, tokenPosition, stack, resultStack, expressionStack, numberOfStatementsBlockStack)
        Assertions.assertEquals(positionAfterBooleanExpression, actual)

        val startLocation = stack.pop()
        Assertions.assertEquals(StatementParserLocation.LOCATION_START, startLocation)
        val ifLocation = stack.pop()
        Assertions.assertEquals(StatementParserLocation.LOCATION_IF, ifLocation)

        val bool = expressionStack.pop()
        Assertions.assertEquals(booleanExpression, bool)
    }
}