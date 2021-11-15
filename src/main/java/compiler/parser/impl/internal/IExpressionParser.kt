package compiler.parser.impl.internal

import compiler.core.IExpressionNode
import compiler.core.Token
import compiler.core.TokenType

internal interface IExpressionParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<IExpressionNode, Int>
}