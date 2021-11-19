package compiler.parser.impl

import compiler.core.IParsedExpressionNode
import compiler.core.Token
import compiler.parser.impl.internal.IExpressionParser

class ExpressionParserIterative: IExpressionParser {

    override fun parse(
            tokens: List<Token>,
            startingPosition: Int,
    ): Pair<IParsedExpressionNode, Int> {
        TODO("Not yet implemented")
    }
}