package compiler.parser.impl

import compiler.core.nodes.VariableDeclarationListNode
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.parser.impl.internal.IVariableDeclarationListParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class StartVariableDeclarationListStatementParserTest {
    private val variableDeclarationListParser = Mockito.mock(IVariableDeclarationListParser::class.java)

    private val startVariableDeclarationListStatementParser = StartVariableDeclarationListStatementParser(
        variableDeclarationListParser
    )

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val variableStatement = Mockito.mock(VariableDeclarationListNode::class.java)
        val positionAfterVariable = 1
        Mockito.`when`(variableDeclarationListParser.parse(
            tokens,
            tokenPosition
        )).thenReturn(Pair(variableStatement, positionAfterVariable))

        val actual = startVariableDeclarationListStatementParser.parse(
            tokens,
            tokenPosition,
            stack,
            resultStack,
            expressionStack,
            numberOfStatementsBlockStack
        )
        Assertions.assertEquals(positionAfterVariable, actual)

        val top = resultStack.pop()
        Assertions.assertEquals(variableStatement, top)
    }
}