package compiler.parser.impl

import compiler.core.ArrayNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IArrayParser
import compiler.parser.impl.internal.IExpressionParser

internal class ArrayParser(
    private val expressionParser: IExpressionParser
): IArrayParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ArrayNode, Int> {
        //LeftBracket
        val hasIndex = tokens[startingPosition + 1].type != TokenType.RIGHT_BRACKET
        val (expression, currentPosition) = if (hasIndex) {
            expressionParser.parse(tokens, startingPosition + 1, setOf(TokenType.RIGHT_BRACKET))
        } else {
            Pair(null, startingPosition + 1)
        }
        //RightBracket
        val arrayNode = ArrayNode(expression)
        return Pair(arrayNode, currentPosition + 1)
    }
}