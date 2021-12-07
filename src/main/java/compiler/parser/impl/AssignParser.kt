package compiler.parser.impl

import compiler.core.nodes.AssignNode
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IAssignParser
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class AssignParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val recursiveExpressionParser: IExpressionParser,
    private val shiftReduceExpressionParser: IExpressionParser
): IAssignParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<AssignNode, Int> {
        tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.BINARY_ASSIGN)
        val positionAfterAssign = startingPosition + 1
        val (expressionNode, positionAfterExpression) = recursiveExpressionParser.parse(tokens, positionAfterAssign)
        val assignNode = AssignNode(expressionNode)
        return Pair(assignNode, positionAfterExpression)
    }
}