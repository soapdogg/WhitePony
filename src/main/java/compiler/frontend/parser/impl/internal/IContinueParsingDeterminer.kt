package compiler.frontend.parser.impl.internal

import compiler.core.tokenizer.TokenType

internal interface IContinueParsingDeterminer {
    fun shouldContinueParsing(
        shouldBreak: Boolean,
        lookAheadType: TokenType,
        hasNotSeenParentheses: Boolean,
        leftRightParentheses: Int
    ): Boolean
}