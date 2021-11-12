package compiler.parser.impl

import compiler.core.Token
import compiler.core.TokenType
import compiler.core.VariableDeclarationNode
import compiler.parser.impl.internal.IArrayParser
import compiler.parser.impl.internal.IAssignParser
import compiler.parser.impl.internal.IVariableDeclarationParser

internal class VariableDeclarationParser(
    private val arrayParser: IArrayParser,
    private val assignParser: IAssignParser
): IVariableDeclarationParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<VariableDeclarationNode, Int> {
        //assertIdentifier
        val id = tokens[startingPosition].value
        val (arrayNode, currentPosition) = if (tokens[startingPosition + 1].type == TokenType.LEFT_BRACKET) {
            arrayParser.parse(tokens, startingPosition + 1)
        } else {
            Pair(null, startingPosition + 1)
        }

        val (assignNode, currentPosition1) = if(tokens[currentPosition].type == TokenType.BINARY_ASSIGN) {
            assignParser.parse(tokens, currentPosition)
        } else {
            Pair(null, currentPosition)
        }

        val variableDeclarationNode = VariableDeclarationNode(
            id,
            arrayNode,
            assignNode
        )
        return Pair(variableDeclarationNode, currentPosition1)
    }
}