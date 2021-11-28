package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocations
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class StartWhileStatementParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val expressionParser: IExpressionParser
): IStatementParser {
    override fun parse(
        tokens: List<Token>,
        tokenPosition: Int,
        stack: Stack<Int>,
        resultStack: Stack<IParsedStatementNode>,
        expressionStack: Stack<IParsedExpressionNode>,
        numberOfStatementsBlockStack: Stack<Int>
    ): Int {
        val (_, positionAfterWhile) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.WHILE)
        val (expression, positionAfterExpression) = expressionParser.parse(tokens, positionAfterWhile)
        expressionStack.push(expression)
        stack.push(StatementParserLocations.LOCATION_WHILE)
        stack.push(StatementParserLocations.LOCATION_START)
        return positionAfterExpression
    }
}