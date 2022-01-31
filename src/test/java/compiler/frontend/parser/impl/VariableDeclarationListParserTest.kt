package compiler.frontend.parser.impl

import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.core.nodes.VariableDeclarationNode
import compiler.frontend.parser.impl.internal.ITokenTypeAsserter
import compiler.frontend.parser.impl.internal.IVariableDeclarationParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class VariableDeclarationListParserTest {

    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val variableDeclarationParser = Mockito.mock(IVariableDeclarationParser::class.java)

    private val variableDeclarationListParser = VariableDeclarationListParser(
        tokenTypeAsserter,
        variableDeclarationParser
    )

    @Test
    fun parseTest() {
        val typeToken = Mockito.mock(Token::class.java)
        val variableDeclarationToken1 = Mockito.mock(Token::class.java)
        val commaToken = Mockito.mock(Token::class.java)
        Mockito.`when`(commaToken.type).thenReturn(TokenType.COMMA)
        val variableDeclarationToken2 = Mockito.mock(Token::class.java)
        val semicolonToken = Mockito.mock(Token::class.java)
        val tokens = listOf(
            typeToken,
            variableDeclarationToken1,
            commaToken,
            variableDeclarationToken2,
            semicolonToken
        )
        val startingPosition = 0

        val positionAfterType = startingPosition + 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.TYPE)).thenReturn(Pair(typeToken, positionAfterType))

        val variableDeclaration1 = Mockito.mock(VariableDeclarationNode::class.java)
        val positionAfterDeclaration1 = positionAfterType + 1
        Mockito.`when`(variableDeclarationParser.parse(
            tokens,
            positionAfterType
        )).thenReturn(Pair(variableDeclaration1, positionAfterDeclaration1))

        val positionAfterComma = positionAfterDeclaration1 + 1
        val variableDeclaration2 = Mockito.mock(VariableDeclarationNode::class.java)
        val positionAfterDeclaration2 = positionAfterComma + 1
        Mockito.`when`(variableDeclarationParser.parse(
            tokens,
            positionAfterComma
        )).thenReturn(Pair(variableDeclaration2, positionAfterDeclaration2))

        val positionAfterSemicolon = positionAfterDeclaration2 + 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, positionAfterDeclaration2, TokenType.SEMICOLON)).thenReturn(Pair(semicolonToken, positionAfterSemicolon))

        val typeTokenValue = "value"
        Mockito.`when`(typeToken.value).thenReturn(typeTokenValue)

        val (actualVariableDeclarationListNode, actualFinalPosition) = variableDeclarationListParser.parse(
            tokens,
            startingPosition
        )
        Assertions.assertEquals(typeTokenValue, actualVariableDeclarationListNode.type)
        Assertions.assertEquals(variableDeclaration1, actualVariableDeclarationListNode.variableDeclarations[0])
        Assertions.assertEquals(variableDeclaration2, actualVariableDeclarationListNode.variableDeclarations[1])
        Assertions.assertEquals(positionAfterSemicolon, actualFinalPosition)
    }
}