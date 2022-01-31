package compiler.frontend.parser.impl

import compiler.core.nodes.AssignNode
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.frontend.parser.impl.internal.IAssignParser
import compiler.frontend.parser.impl.internal.IExpressionParser
import compiler.frontend.parser.impl.internal.ITokenTypeAsserter

internal class AssignParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val shiftReduceExpressionParser: IExpressionParser
): IAssignParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<AssignNode, Int> {
        tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.BINARY_ASSIGN)
        val positionAfterAssign = startingPosition + 1
        val (expressionNode, positionAfterExpression) = shiftReduceExpressionParser.parse(tokens, positionAfterAssign)
        val assignNode = AssignNode(expressionNode)
        return Pair(assignNode, positionAfterExpression)
    }
}