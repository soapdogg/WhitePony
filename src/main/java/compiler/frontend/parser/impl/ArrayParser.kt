package compiler.frontend.parser.impl

import compiler.core.nodes.ArrayNode
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.frontend.parser.impl.internal.IArrayParser
import compiler.frontend.parser.impl.internal.IExpressionParser
import compiler.frontend.parser.impl.internal.ITokenTypeAsserter

internal class ArrayParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val shiftReduceExpressionParser: IExpressionParser
): IArrayParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ArrayNode, Int> {
        tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.LEFT_BRACKET)
        val positionAfterLeftBracket = startingPosition + 1
        val hasIndex = tokens[positionAfterLeftBracket].type != TokenType.RIGHT_BRACKET
        val (expressionNode, positionAfterExpression) = if (hasIndex) {
            shiftReduceExpressionParser.parse(tokens, positionAfterLeftBracket)
        } else {
            Pair(null, positionAfterLeftBracket)
        }
        tokenTypeAsserter.assertTokenType(tokens, positionAfterExpression, TokenType.RIGHT_BRACKET)
        val positionAfterRightBracket = positionAfterExpression + 1
        val arrayNode = ArrayNode(expressionNode)
        return Pair(arrayNode, positionAfterRightBracket)
    }
}