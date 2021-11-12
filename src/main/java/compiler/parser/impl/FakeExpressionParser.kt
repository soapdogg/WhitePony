package compiler.parser.impl

import compiler.core.FakeExpressionNode
import compiler.core.IExpressionNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IExpressionParser

internal class FakeExpressionParser(

): IExpressionParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
        tokenTypesToLookFor: Set<TokenType>
    ): Pair<IExpressionNode, Int> {
        var result = ""
        var curr = startingPosition
        while (!tokenTypesToLookFor.contains(tokens[curr].type)) {
            result += tokens[curr].value
            curr++
        }
        return Pair(FakeExpressionNode(result), curr)
    }
}