package compiler.parser.impl.internal

import compiler.core.ExpressionStatementNode
import compiler.core.Token

internal interface IExpressionStatementParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ExpressionStatementNode, Int>
}