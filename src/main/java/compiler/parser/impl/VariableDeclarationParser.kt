package compiler.parser.impl

import compiler.core.Token
import compiler.core.TokenType
import compiler.core.ParsedVariableDeclarationNode
import compiler.parser.impl.internal.IArrayParser
import compiler.parser.impl.internal.IAssignParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import compiler.parser.impl.internal.IVariableDeclarationParser

internal class VariableDeclarationParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val arrayParser: IArrayParser,
    private val assignParser: IAssignParser
): IVariableDeclarationParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ParsedVariableDeclarationNode, Int> {
        val (identifierToken, positionAfterIdentifier) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.IDENTIFIER)

        val (arrayNode, positionAfterArray) = if (tokens[positionAfterIdentifier].type == TokenType.LEFT_BRACKET) {
            arrayParser.parse(tokens, positionAfterIdentifier)
        } else {
            Pair(null, positionAfterIdentifier)
        }

        val (assignNode, positionAfterAssign) = if(tokens[positionAfterArray].type == TokenType.BINARY_ASSIGN) {
            assignParser.parse(tokens, positionAfterArray)
        } else {
            Pair(null, positionAfterArray)
        }

        val variableDeclarationNode = ParsedVariableDeclarationNode(
            identifierToken.value,
            arrayNode,
            assignNode
        )
        return Pair(variableDeclarationNode, positionAfterAssign)
    }
}