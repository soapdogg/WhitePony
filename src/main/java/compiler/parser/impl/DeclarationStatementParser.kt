package compiler.parser.impl

import compiler.core.DeclarationStatementNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IDeclarationStatementParser
import compiler.parser.impl.internal.IFunctionDeclarationParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import compiler.parser.impl.internal.IVariableDeclarationListParser

internal class DeclarationStatementParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val functionDeclarationParser: IFunctionDeclarationParser,
    private val variableDeclarationListParser: IVariableDeclarationListParser,
) : IDeclarationStatementParser{

    override fun parse(tokens: List<Token>, startingPosition: Int): Pair<DeclarationStatementNode, Int> {
        val typeToken = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.TYPE)
        val identifierToken = tokenTypeAsserter.assertTokenType(tokens, startingPosition + 1, TokenType.IDENTIFIER)

        val currentToken = tokens[startingPosition + 2]

        val type = currentToken.type

        val (declarationStatementChild, currentPosition) = if (type == TokenType.LEFT_PARENTHESES) {
            functionDeclarationParser.parse(
                tokens,
                startingPosition,
                typeToken.value,
                identifierToken.value
            )
        } else {
            variableDeclarationListParser.parse(
                tokens,
                startingPosition,
                typeToken.value,
                identifierToken.value
            )
        }

        return Pair(DeclarationStatementNode(declarationStatementChild), currentPosition)
    }
}