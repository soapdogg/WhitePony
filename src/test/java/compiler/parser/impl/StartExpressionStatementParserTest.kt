package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.parser.impl.internal.IExpressionStatementParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class StartExpressionStatementParserTest {
    private val expressionStatementParser = Mockito.mock(IExpressionStatementParser::class.java)
    private val startExpressionStatementParser = StartExpressionStatementParser(expressionStatementParser)

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val expressionStatement = Mockito.mock(ParsedExpressionStatementNode::class.java)
        val positionAfterExpression = 1
        Mockito.`when`(expressionStatementParser.parse(tokens, tokenPosition)).thenReturn(Pair(expressionStatement, positionAfterExpression))

        val actual = startExpressionStatementParser.parse(tokens, tokenPosition, stack, resultStack, expressionStack, numberOfStatementsBlockStack)
        Assertions.assertEquals(positionAfterExpression, actual)
        val top = resultStack.pop()
        Assertions.assertEquals(expressionStatement, top)
    }
}