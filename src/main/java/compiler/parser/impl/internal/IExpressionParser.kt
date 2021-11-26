package compiler.parser.impl.internal

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.tokenizer.Token

internal interface IExpressionParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<IParsedExpressionNode, Int>
}