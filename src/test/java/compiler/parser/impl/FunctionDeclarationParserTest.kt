package compiler.parser.impl

import compiler.core.BasicBlockNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class FunctionDeclarationParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val statementParser = Mockito.mock(IStatementParser::class.java)

    private val functionDeclarationParser = FunctionDeclarationParser(
        tokenTypeAsserter,
        statementParser
    )

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0
        val type = "type"
        val identifierValue = "value"

        val basicBlockNode = Mockito.mock(BasicBlockNode::class.java)
        val currentPosition = 3
        Mockito.`when`(
            statementParser.parse(
                tokens,
                startingPosition + 2
            )
        ).thenReturn(Pair(basicBlockNode, currentPosition))

        val (actualFunctionNode, actualCurrentPosition) = functionDeclarationParser.parse(
            tokens,
            startingPosition,
            type,
            identifierValue
        )

        Assertions.assertEquals(identifierValue, actualFunctionNode.functionName)
        Assertions.assertEquals(type, actualFunctionNode.type)
        Assertions.assertEquals(basicBlockNode, actualFunctionNode.basicBlockNode)
        Assertions.assertEquals(currentPosition, actualCurrentPosition)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, startingPosition, TokenType.LEFT_PARENTHESES)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, startingPosition + 1, TokenType.RIGHT_PARENTHESES)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, startingPosition + 2, TokenType.LEFT_BRACE)
    }
}