package compiler.parser.impl.internal

import compiler.core.Token
import compiler.core.TokenType

internal interface ITokenTypeAsserter {
    fun assertTokenType(tokens: List<Token>, position: Int, expectedType: TokenType): Pair<Token, Int>
    fun assertTokenType(tokens: List<Token>, position: Int, expectedTypes: Set<TokenType>): Pair<Token, Int>
}