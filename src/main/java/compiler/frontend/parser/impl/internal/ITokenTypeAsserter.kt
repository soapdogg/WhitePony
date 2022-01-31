package compiler.frontend.parser.impl.internal

import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType

internal interface ITokenTypeAsserter {
    fun assertTokenType(tokens: List<Token>, position: Int, expectedType: TokenType): Pair<Token, Int>
    fun assertTokenType(tokens: List<Token>, position: Int, expectedTypes: Set<TokenType>): Pair<Token, Int>
}