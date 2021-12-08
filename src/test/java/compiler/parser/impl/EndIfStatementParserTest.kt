package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedIfNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class EndIfStatementParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)

    private val endIfStatementParser = EndIfStatementParser(tokenTypeAsserter)

    @Test
    fun nextTokenNotElseTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val tokenType = TokenType.DO
        Mockito.`when`(token.type).thenReturn(tokenType)

        val booleanExpression = Mockito.mock(IParsedExpressionNode::class.java)
        expressionStack.push(booleanExpression)

        val ifBody = Mockito.mock(IParsedStatementNode::class.java)
        resultStack.push(ifBody)

        val actual = endIfStatementParser.parse(
            tokens,
            tokenPosition,
            stack,
            resultStack,
            expressionStack,
            numberOfStatementsBlockStack
        )
        Assertions.assertEquals(tokenPosition, actual)
        val top = resultStack.pop() as ParsedIfNode
        Assertions.assertEquals(booleanExpression, top.booleanExpression)
        Assertions.assertEquals(ifBody, top.ifBody)
        Assertions.assertNull(top.elseBody)
    }

    @Test
    fun nextTokenElseTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val tokenType = TokenType.ELSE
        Mockito.`when`(token.type).thenReturn(tokenType)

        val positionAfterElse = 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.ELSE)).thenReturn(Pair(Mockito.mock(Token::class.java), positionAfterElse))
        val actual = endIfStatementParser.parse(
            tokens,
            tokenPosition,
            stack,
            resultStack,
            expressionStack,
            numberOfStatementsBlockStack
        )
        Assertions.assertEquals(positionAfterElse, actual)
        val start = stack.pop()
        Assertions.assertEquals(StatementParserLocation.LOCATION_START, start)
        val elseLocation = stack.pop()
        Assertions.assertEquals(StatementParserLocation.LOCATION_ELSE, elseLocation)
    }
}