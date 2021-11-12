package compiler.parser.impl.internal

import compiler.core.IExpressionNode
import compiler.core.Token
import compiler.core.TokenType

interface IExpressionParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int,
        tokenTypeToLookFor: TokenType //TODO this is temp -- used to validate statement parsing logic correctness
    ): Pair<IExpressionNode, Int>
}