package compiler.frontend.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.frontend.parser.impl.internal.IExpressionParser
import compiler.frontend.parser.impl.internal.IStatementParser
import compiler.frontend.parser.impl.internal.ITokenTypeAsserter

internal class StartWhileStatementParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val shiftReduceExpressionParser: IExpressionParser
): IStatementParser {
    override fun parse(
        tokens: List<Token>,
        tokenPosition: Int,
        stack: Stack<StatementParserLocation>,
        resultStack: Stack<IParsedStatementNode>,
        expressionStack: Stack<IParsedExpressionNode>,
        numberOfStatementsBlockStack: Stack<Int>
    ): Int {
        val (_, positionAfterWhile) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.WHILE)
        val (expression, positionAfterExpression) = shiftReduceExpressionParser.parse(tokens, positionAfterWhile)

        expressionStack.push(expression)
        stack.push(StatementParserLocation.LOCATION_WHILE)
        stack.push(StatementParserLocation.LOCATION_START)
        return positionAfterExpression
    }
}