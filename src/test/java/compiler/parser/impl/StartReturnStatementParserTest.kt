package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedReturnNode
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.parser.impl.internal.IReturnStatementParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class StartReturnStatementParserTest {

    private val returnStatementParser = Mockito.mock(IReturnStatementParser::class.java)

    private val startReturnStatementParser = StartReturnStatementParser(
        returnStatementParser
    )

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val tokenPosition = 0
        val stack = Stack<Int>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val returnStatement = Mockito.mock(ParsedReturnNode::class.java)
        val positionAfterReturn = 1
        Mockito.`when`(returnStatementParser.parse(tokens, tokenPosition)).thenReturn(Pair(returnStatement, positionAfterReturn))

        val actual = startReturnStatementParser.parse(
            tokens, tokenPosition, stack, resultStack, expressionStack, numberOfStatementsBlockStack
        )
        Assertions.assertEquals(positionAfterReturn, actual)

        val top = resultStack.pop()
        Assertions.assertEquals(returnStatement, top)
    }
}