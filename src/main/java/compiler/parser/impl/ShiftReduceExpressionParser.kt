package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.tokenizer.Token
import compiler.parser.impl.internal.IExpressionParser

internal class ShiftReduceExpressionParser: IExpressionParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        TODO("Not yet implemented")
    }
}