package compiler.parser.impl

import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IContinueParsingDeterminer

internal class ContinueParsingDeterminer(
    private val acceptedTokenTypes: Set<TokenType>
): IContinueParsingDeterminer {
    override fun shouldContinueParsing(
        shouldBreak: Boolean,
        lookAheadType: TokenType,
        hasNotSeenParentheses: Boolean,
        leftRightParentheses: Int
    ): Boolean {
        return !shouldBreak
                && acceptedTokenTypes.contains(lookAheadType)
                && (hasNotSeenParentheses || leftRightParentheses > 0)
    }
}