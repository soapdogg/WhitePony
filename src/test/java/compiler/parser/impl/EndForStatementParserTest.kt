package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedForNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class EndForStatementParserTest {
    private val endForStatementParser = EndForStatementParser()

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val incrementExpression = Mockito.mock(IParsedExpressionNode::class.java)
        expressionStack.push(incrementExpression)

        val testExpression = Mockito.mock(IParsedExpressionNode::class.java)
        expressionStack.push(testExpression)

        val initExpression = Mockito.mock(IParsedExpressionNode::class.java)
        expressionStack.push(initExpression)

        val body = Mockito.mock(IParsedStatementNode::class.java)
        resultStack.push(body)

        val actual = endForStatementParser.parse(
            tokens,
            tokenPosition,
            stack,
            resultStack,
            expressionStack,
            numberOfStatementsBlockStack,
            false
        )
        Assertions.assertEquals(tokenPosition, actual)
        val top = resultStack.pop() as ParsedForNode
        Assertions.assertEquals(initExpression, top.initExpression)
        Assertions.assertEquals(testExpression, top.testExpression)
        Assertions.assertEquals(incrementExpression, top.incrementExpression)
        Assertions.assertEquals(body, top.body)
    }
}