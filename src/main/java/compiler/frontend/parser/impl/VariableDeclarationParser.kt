package compiler.frontend.parser.impl

import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.core.nodes.VariableDeclarationNode
import compiler.frontend.parser.impl.internal.IArrayParser
import compiler.frontend.parser.impl.internal.IAssignParser
import compiler.frontend.parser.impl.internal.ITokenTypeAsserter
import compiler.frontend.parser.impl.internal.IVariableDeclarationParser

internal class VariableDeclarationParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val arrayParser: IArrayParser,
    private val assignParser: IAssignParser
): IVariableDeclarationParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<VariableDeclarationNode, Int> {
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

        val variableDeclarationNode = VariableDeclarationNode(
            identifierToken.value,
            arrayNode,
            assignNode
        )
        return Pair(variableDeclarationNode, positionAfterAssign)
    }
}