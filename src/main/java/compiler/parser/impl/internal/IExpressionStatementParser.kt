package compiler.parser.impl.internal

import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.tokenizer.Token

internal interface IExpressionStatementParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int,
        useShiftReduce: Boolean
    ): Pair<ParsedExpressionStatementNode, Int>
}