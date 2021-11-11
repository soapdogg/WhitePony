package compiler.parser.impl

import compiler.core.FunctionDeclarationNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.core.VariableDeclarationListNode
import compiler.parser.impl.internal.IFunctionDeclarationParser
import compiler.parser.impl.internal.IVariableDeclarationListParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class DeclarationChildParserTest {

    private val functionDeclarationParser = Mockito.mock(IFunctionDeclarationParser::class.java)
    private val variableDeclarationListParser = Mockito.mock(IVariableDeclarationListParser::class.java)

    private val declarationChildParser = DeclarationChildParser(functionDeclarationParser, variableDeclarationListParser)

    @Test
    fun parseFunctionChildTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)
        val startingPosition = 0
        val type = "type"
        val identifierValue = "identifier"

        val tokenType = TokenType.LEFT_PARENTHESES
        Mockito.`when`(token.type).thenReturn(tokenType)

        val currentPosition = 1
        val functionDeclarationNode = Mockito.mock(FunctionDeclarationNode::class.java)
        Mockito.`when`(
            functionDeclarationParser.parse(
                tokens,
                startingPosition,
                type,
                identifierValue
            )
        ).thenReturn(Pair(functionDeclarationNode, currentPosition))

        val (actualChildNode, actualCurrentPosition) = declarationChildParser.parse(
            tokens,
            startingPosition,
            type,
            identifierValue
        )

        Assertions.assertEquals(functionDeclarationNode, actualChildNode)
        Assertions.assertEquals(currentPosition, actualCurrentPosition)
    }

    @Test
    fun parseVariableListChildTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)
        val startingPosition = 0
        val type = "type"
        val identifierValue = "identifier"

        val tokenType = TokenType.PRE_POST
        Mockito.`when`(token.type).thenReturn(tokenType)

        val currentPosition = 1
        val variableDeclarationListNode = Mockito.mock(VariableDeclarationListNode::class.java)
        Mockito.`when`(
            variableDeclarationListParser.parse(
                tokens,
                startingPosition,
                type,
                identifierValue
            )
        ).thenReturn(Pair(variableDeclarationListNode, currentPosition))

        val (actualChildNode, actualCurrentPosition) = declarationChildParser.parse(
            tokens,
            startingPosition,
            type,
            identifierValue
        )

        Assertions.assertEquals(variableDeclarationListNode, actualChildNode)
        Assertions.assertEquals(currentPosition, actualCurrentPosition)
    }
}