package compiler.parser.impl.internal

import compiler.core.IExpressionNode
import compiler.core.Token

interface IExpressionParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int>
}