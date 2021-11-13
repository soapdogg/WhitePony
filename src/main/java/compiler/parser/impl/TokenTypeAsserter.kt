package compiler.parser.impl

import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class TokenTypeAsserter(

): ITokenTypeAsserter {
    override fun assertTokenType(
        tokens: List<Token>,
        position: Int,
        expectedType: TokenType
    ): Pair<Token, Int> {
        val token = tokens[position]
        if (token.type != expectedType) {
            throw Exception("Unexpected TokenType!")
        }
        return Pair(token, position + 1)
    }
}