package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedDoWhileNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class EndDoStatementParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val shiftReduceExpressionParser = Mockito.mock(IExpressionParser::class.java)

    private val endDoStatementParser = EndDoStatementParser(
        tokenTypeAsserter,
        shiftReduceExpressionParser
    )

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val body = Mockito.mock(IParsedStatementNode::class.java)
        resultStack.push(body)

        val positionAfterWhile = 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens,tokenPosition, TokenType.WHILE)).thenReturn(Pair(Mockito.mock(Token::class.java), positionAfterWhile))

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        val positionAfterExpression = 2
        Mockito.`when`(shiftReduceExpressionParser.parse(tokens, positionAfterWhile)).thenReturn(Pair(expression, positionAfterExpression))

        val positionAfterSemiColon = 3
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, positionAfterExpression, TokenType.SEMICOLON)).thenReturn(Pair(Mockito.mock(Token::class.java), positionAfterSemiColon))

        val actual = endDoStatementParser.parse(
            tokens,
            tokenPosition,
            stack,
            resultStack,
            expressionStack,
            numberOfStatementsBlockStack,
            false
        )

        Assertions.assertEquals(positionAfterSemiColon, actual)
        val top = resultStack.pop() as ParsedDoWhileNode
        Assertions.assertEquals(expression, top.expression)
        Assertions.assertEquals(body, top.body)
    }
}