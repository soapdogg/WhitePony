package compiler.parser.impl

import compiler.core.ArrayNode
import compiler.core.AssignNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IArrayParser
import compiler.parser.impl.internal.IAssignParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class VariableDeclarationParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val arrayParser = Mockito.mock(IArrayParser::class.java)
    private val assignParser = Mockito.mock(IAssignParser::class.java)

    private val variableDeclarationParser = VariableDeclarationParser(
        tokenTypeAsserter,
        arrayParser,
        assignParser
    )

    @Test
    fun hasArrayAndAssignTest() {
        val identifierToken = Mockito.mock(Token::class.java)
        val leftBracketToken = Mockito.mock(Token::class.java)
        Mockito.`when`(leftBracketToken.type).thenReturn(TokenType.LEFT_BRACKET)
        val rightBracketToken = Mockito.mock(Token::class.java)
        val assignToken = Mockito.mock(Token::class.java)
        Mockito.`when`(assignToken.type).thenReturn(TokenType.BINARY_ASSIGN)
        val tokens = listOf(
            identifierToken,
            leftBracketToken,
            rightBracketToken,
            assignToken,
        )
        val startingPosition = 0

        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.IDENTIFIER)).thenReturn(identifierToken)

        val positionAfterIdentifier = startingPosition + 1
        val arrayNode = Mockito.mock(ArrayNode::class.java)
        val positionAfterArray = positionAfterIdentifier + 2
        Mockito.`when`(arrayParser.parse(tokens, positionAfterIdentifier)).thenReturn(Pair(arrayNode, positionAfterArray))

        val assignNode = Mockito.mock(AssignNode::class.java)
        val positionAfterAssign = positionAfterArray + 1
        Mockito.`when`(assignParser.parse(tokens, positionAfterArray)).thenReturn(Pair(assignNode, positionAfterAssign))

        val id = "id"
        Mockito.`when`(identifierToken.value).thenReturn(id)

        val (actualVariableDeclarationNode, actualFinalPosition) = variableDeclarationParser.parse(tokens, startingPosition)
        Assertions.assertEquals(id, actualVariableDeclarationNode.id)
        Assertions.assertEquals(arrayNode, actualVariableDeclarationNode.arrayNode)
        Assertions.assertEquals(assignNode, actualVariableDeclarationNode.assignNode)
        Assertions.assertEquals(positionAfterAssign, actualFinalPosition)
    }

    @Test
    fun doesNotHaveArrayAndAssignTest() {
        val identifierToken = Mockito.mock(Token::class.java)
        val nextToken = Mockito.mock(Token::class.java)
        val tokens = listOf(
            identifierToken,
            nextToken
        )
        val startingPosition = 0
        val positionAfterIdentifier = startingPosition + 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.IDENTIFIER)).thenReturn(identifierToken)

        val id = "id"
        Mockito.`when`(identifierToken.value).thenReturn(id)

        val (actualVariableDeclarationNode, actualFinalPosition) = variableDeclarationParser.parse(tokens, startingPosition)
        Assertions.assertEquals(id, actualVariableDeclarationNode.id)
        Assertions.assertNull(actualVariableDeclarationNode.arrayNode)
        Assertions.assertNull(actualVariableDeclarationNode.assignNode)
        Assertions.assertEquals(positionAfterIdentifier, actualFinalPosition)
    }
}