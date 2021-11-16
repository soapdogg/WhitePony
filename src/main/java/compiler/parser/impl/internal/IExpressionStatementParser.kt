package compiler.parser.impl.internal

import compiler.core.ParsedExpressionStatementNode
import compiler.core.Token

internal interface IExpressionStatementParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ParsedExpressionStatementNode, Int>
}