package compiler.parser.impl

import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class TokenTypeAsserter : ITokenTypeAsserter {
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

    override fun assertTokenType(
        tokens: List<Token>,
        position: Int,
        expectedTypes: Set<TokenType>,
    ): Pair<Token, Int> {
        val token = tokens[position]
        if (!expectedTypes.contains(token.type)) {
            throw Exception("Unexpected TokenType!")
        }
        return Pair(token, position + 1)
    }
}