package compiler.parser.impl

import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IExpressionStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class ExpressionStatementParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val recursiveExpressionParser: IExpressionParser,
    private val shiftReduceExpressionParser: IExpressionParser,
): IExpressionStatementParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
        useShiftReduce: Boolean
    ): Pair<ParsedExpressionStatementNode, Int> {
        val (expressionNode, positionAfterExpression) = if(useShiftReduce){
            shiftReduceExpressionParser.parse(tokens, startingPosition)
        } else {
            recursiveExpressionParser.parse(tokens, startingPosition)
        }
        tokenTypeAsserter.assertTokenType(tokens, positionAfterExpression, TokenType.SEMICOLON)
        val positionAfterSemicolon = positionAfterExpression + 1
        val expressionStatementNode = ParsedExpressionStatementNode(expressionNode)
        return Pair(expressionStatementNode, positionAfterSemicolon)
    }
}