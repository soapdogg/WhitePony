package compiler.parser.impl

import compiler.core.FakeExpressionNode
import compiler.core.IExpressionNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IExpressionParser

class FakeExpressionParser(

): IExpressionParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
        tokenTypeToLookFor: TokenType
    ): Pair<IExpressionNode, Int> {
        var result = ""
        var curr = startingPosition
        while (tokens[curr].type != tokenTypeToLookFor) {
            result += tokens[curr].value
            curr++
        }
        return Pair(FakeExpressionNode(result), curr)
    }
}