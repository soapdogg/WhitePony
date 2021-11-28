package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedWhileNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class EndWhileStatementParserTest {
    private val endWhileStatementParser = EndWhileStatementParser()

    @Test
    fun parseTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        expressionStack.push(expression)

        val body = Mockito.mock(IParsedStatementNode::class.java)
        resultStack.push(body)

        val actual = endWhileStatementParser.parse(tokens, tokenPosition, stack, resultStack, expressionStack, numberOfStatementsBlockStack)
        Assertions.assertEquals(tokenPosition, actual)
        val top = resultStack.pop() as ParsedWhileNode
        Assertions.assertEquals(expression, top.expression)
        Assertions.assertEquals(body, top.body)
    }
}