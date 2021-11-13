package compiler.parser.impl

import compiler.core.ExpressionStatementNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IExpressionStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ReturnStatementParserTest {

    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val expressionStatementParser = Mockito.mock(IExpressionStatementParser::class.java)

    private val returnStatementParser = ReturnStatementParser(
        tokenTypeAsserter,
        expressionStatementParser
    )

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0

        val positionAfterReturn = startingPosition + 1
        val expressionStatementNode = Mockito.mock(ExpressionStatementNode::class.java)
        val positionAfterExpressionStatement = positionAfterReturn + 1
        Mockito.`when`(expressionStatementParser.parse(tokens, positionAfterReturn)).thenReturn(Pair(expressionStatementNode, positionAfterExpressionStatement))

        val(actualReturnNode, actualFinalPosition) = returnStatementParser.parse(tokens, startingPosition)
        Assertions.assertEquals(expressionStatementNode, actualReturnNode.expressionStatement)
        Assertions.assertEquals(positionAfterExpressionStatement, actualFinalPosition)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, startingPosition, TokenType.RETURN)
    }
}