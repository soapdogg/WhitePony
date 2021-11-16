package compiler.parser.impl

import compiler.core.ParsedReturnNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IExpressionStatementParser
import compiler.parser.impl.internal.IReturnStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class ReturnStatementParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val expressionStatementParser: IExpressionStatementParser
) : IReturnStatementParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ParsedReturnNode, Int> {
        tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.RETURN)
        val positionAfterReturn = startingPosition + 1
        val (expressionStatementNode, positionAfterExpressionStatement) = expressionStatementParser.parse(tokens, positionAfterReturn)
        val returnNode = ParsedReturnNode(expressionStatementNode)
        return Pair(returnNode, positionAfterExpressionStatement)
    }
}