package compiler.frontend.parser.impl

import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.frontend.parser.impl.internal.IStatementParserOrchestrator
import compiler.frontend.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class FunctionDeclarationParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val statementParser = Mockito.mock(IStatementParserOrchestrator::class.java)

    private val functionDeclarationParser = FunctionDeclarationParser(
        tokenTypeAsserter,
        statementParser
    )

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0

        val typeToken = Mockito.mock(Token::class.java)
        val positionAfterType = startingPosition + 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.TYPE)).thenReturn(Pair(typeToken, positionAfterType))

        val type = "type"
        Mockito.`when`(typeToken.value).thenReturn(type)

        val identifierToken = Mockito.mock(Token::class.java)
        val positionAfterIdentifier = positionAfterType + 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, positionAfterType, TokenType.IDENTIFIER)).thenReturn(Pair(identifierToken, positionAfterIdentifier))

        val positionAfterLeftParentheses = positionAfterIdentifier + 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, positionAfterIdentifier, TokenType.LEFT_PARENTHESES)).thenReturn(Pair(Mockito.mock(
            Token::class.java), positionAfterLeftParentheses))

        val positionAfterRightParentheses = positionAfterLeftParentheses + 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, positionAfterLeftParentheses, TokenType.RIGHT_PARENTHESES)).thenReturn(Pair(Mockito.mock(
            Token::class.java), positionAfterRightParentheses))

        val identifierValue = "value"
        Mockito.`when`(identifierToken.value).thenReturn(identifierValue)

        val basicBlockNode = Mockito.mock(ParsedBasicBlockNode::class.java)
        val finalPosition = 10
        Mockito.`when`(
            statementParser.parse(
                tokens,
                positionAfterRightParentheses
            )
        ).thenReturn(Pair(basicBlockNode, finalPosition))

        val (actualFunctionNode, actualFinalPosition) = functionDeclarationParser.parse(
            tokens,
            startingPosition,
        )

        Assertions.assertEquals(identifierValue, actualFunctionNode.functionName)
        Assertions.assertEquals(type, actualFunctionNode.type)
        Assertions.assertEquals(basicBlockNode, actualFunctionNode.basicBlockNode)
        Assertions.assertEquals(finalPosition, actualFinalPosition)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, startingPosition + 2, TokenType.LEFT_PARENTHESES)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, startingPosition + 3, TokenType.RIGHT_PARENTHESES)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, startingPosition + 4, TokenType.LEFT_BRACE)
    }
}