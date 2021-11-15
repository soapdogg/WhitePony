package compiler.parser.impl.internal

import compiler.core.IParsedExpressionNode
import compiler.core.Token

internal interface IExpressionParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<IParsedExpressionNode, Int>
}