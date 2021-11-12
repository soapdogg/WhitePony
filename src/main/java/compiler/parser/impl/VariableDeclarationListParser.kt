package compiler.parser.impl

import compiler.core.Token
import compiler.core.TokenType
import compiler.core.VariableDeclarationListNode
import compiler.parser.impl.internal.ITokenTypeAsserter
import compiler.parser.impl.internal.IVariableDeclarationListParser

internal class VariableDeclarationListParser(
    private val tokenTypeAsserter: ITokenTypeAsserter
): IVariableDeclarationListParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<VariableDeclarationListNode, Int> {
        val typeToken = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.TYPE)
        val identifierToken = tokenTypeAsserter.assertTokenType(tokens, startingPosition + 1, TokenType.IDENTIFIER)

        TODO("Not yet implemented")
    }
}