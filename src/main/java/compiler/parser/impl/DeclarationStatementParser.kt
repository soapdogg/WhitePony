package compiler.parser.impl

import compiler.core.IDeclarationStatementNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IDeclarationStatementParser
import compiler.parser.impl.internal.IFunctionDeclarationParser
import compiler.parser.impl.internal.IVariableDeclarationListParser

internal class DeclarationStatementParser(
    private val functionDeclarationParser: IFunctionDeclarationParser,
    private val variableDeclarationListParser: IVariableDeclarationListParser,
) : IDeclarationStatementParser{

    override fun parse(tokens: List<Token>, startingPosition: Int): Pair<IDeclarationStatementNode, Int> {
        val decidingToken = tokens[startingPosition + 2]

        return if (decidingToken.type == TokenType.LEFT_PARENTHESES) {
            functionDeclarationParser.parse(
                tokens,
                startingPosition,
            )
        } else {
            variableDeclarationListParser.parse(
                tokens,
                startingPosition,
            )
        }
    }
}