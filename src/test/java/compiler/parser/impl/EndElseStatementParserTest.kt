package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedIfNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class EndElseStatementParserTest {
    private val endElseStatementParser = EndElseStatementParser()

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val booleanExpression = Mockito.mock(IParsedExpressionNode::class.java)
        expressionStack.push(booleanExpression)

        val ifBody = Mockito.mock(IParsedStatementNode::class.java)
        resultStack.push(ifBody)

        val elseBody = Mockito.mock(IParsedStatementNode::class.java)
        resultStack.push(elseBody)

        val actual = endElseStatementParser.parse(
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
        Assertions.assertEquals(elseBody, top.elseBody)
    }
}