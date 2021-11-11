package compiler.parser.impl

import compiler.core.IDeclarationChildNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IDeclarationChildParser
import compiler.parser.impl.internal.IFunctionDeclarationParser
import compiler.parser.impl.internal.IVariableDeclarationListParser

internal class DeclarationChildParser (
    private val functionDeclarationParser : IFunctionDeclarationParser,
    private val variableDeclarationListParser: IVariableDeclarationListParser
) : IDeclarationChildParser {

    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
        type: String,
        identifierValue: String
    ): Pair<IDeclarationChildNode, Int> {
        val currentToken = tokens[startingPosition]

        return if (currentToken.type == TokenType.LEFT_PARENTHESES) {
            functionDeclarationParser.parse(
                tokens,
                startingPosition,
                type,
                identifierValue
            )
        } else {
            variableDeclarationListParser.parse(
                tokens,
                startingPosition,
                type,
                identifierValue
            )
        }
    }
}