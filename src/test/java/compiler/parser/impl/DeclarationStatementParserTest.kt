package compiler.parser.impl

import compiler.core.*
import compiler.parser.impl.internal.IDeclarationChildParser
import compiler.parser.impl.internal.IFunctionDeclarationParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import compiler.parser.impl.internal.IVariableDeclarationListParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class DeclarationStatementParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val functionDeclarationParser = Mockito.mock(IFunctionDeclarationParser::class.java)
    private val variableDeclarationListParser = Mockito.mock(IVariableDeclarationListParser::class.java)

    private val declarationStatementParser = DeclarationStatementParser(
        tokenTypeAsserter,
        functionDeclarationParser,
        variableDeclarationListParser
    )

    @Test
    fun parseFunctionDeclarationTest() {
        val token0 = Mockito.mock(Token::class.java)
        val token1 = Mockito.mock(Token::class.java)
        val token2 = Mockito.mock(Token::class.java)
        val tokens = listOf<Token>(token0, token1, token2)
        val startingPosition = 0

        val tokenType = TokenType.LEFT_PARENTHESES
        Mockito.`when`(token2.type).thenReturn(tokenType)

        val currentPosition = 1
        val functionDeclarationNode = Mockito.mock(FunctionDeclarationNode::class.java)
        Mockito.`when`(
            functionDeclarationParser.parse(
                tokens,
                startingPosition,
            )
        ).thenReturn(Pair(functionDeclarationNode, currentPosition))

        val (actualDeclarationStatement, actualPosition) = declarationStatementParser.parse(tokens, startingPosition)

        Assertions.assertEquals(functionDeclarationNode, actualDeclarationStatement.declarationChild)
        Assertions.assertEquals(currentPosition, actualPosition)
    }

    @Test
    fun parseVariableListTest() {
        val token0 = Mockito.mock(Token::class.java)
        val token1 = Mockito.mock(Token::class.java)
        val token2 = Mockito.mock(Token::class.java)
        val tokens = listOf<Token>(token0, token1, token2)
        val startingPosition = 0

        val typeToken = Mockito.mock(Token::class.java)
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.TYPE)).thenReturn(typeToken)

        val identifierToken = Mockito.mock(Token::class.java)
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, startingPosition + 1, TokenType.IDENTIFIER)).thenReturn(identifierToken)

        val typeValue = "typeValue"
        Mockito.`when`(typeToken.value).thenReturn(typeValue)

        val identifierValue = "identifierValue"
        Mockito.`when`(identifierToken.value).thenReturn(identifierValue)

        val tokenType = TokenType.LEFT_BRACE
        Mockito.`when`(token2.type).thenReturn(tokenType)

        val currentPosition = 1
        val variableDeclarationListNode = Mockito.mock(VariableDeclarationListNode::class.java)
        Mockito.`when`(
            variableDeclarationListParser.parse(
                tokens,
                startingPosition,
                typeValue,
                identifierValue
            )
        ).thenReturn(Pair(variableDeclarationListNode, currentPosition))

        val (actualDeclarationStatement, actualPosition) = declarationStatementParser.parse(tokens, startingPosition)

        Assertions.assertEquals(variableDeclarationListNode, actualDeclarationStatement.declarationChild)
        Assertions.assertEquals(currentPosition, actualPosition)
    }
}