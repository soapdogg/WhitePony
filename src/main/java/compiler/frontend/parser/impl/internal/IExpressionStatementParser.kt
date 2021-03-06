package compiler.frontend.parser.impl.internal

import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.tokenizer.Token

internal interface IExpressionStatementParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ParsedExpressionStatementNode, Int>
}