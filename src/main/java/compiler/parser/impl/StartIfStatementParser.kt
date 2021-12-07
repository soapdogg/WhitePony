package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class StartIfStatementParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val shiftReduceExpressionParser: IExpressionParser
): IStatementParser {
    override fun parse(
        tokens: List<Token>,
        tokenPosition: Int,
        stack: Stack<StatementParserLocation>,
        resultStack: Stack<IParsedStatementNode>,
        expressionStack: Stack<IParsedExpressionNode>,
        numberOfStatementsBlockStack: Stack<Int>,
        useShiftReduce: Boolean
    ): Int {
        val (_, positionAfterIf) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.IF)
        val (booleanExpression, positionAfterBooleanExpression) = shiftReduceExpressionParser.parse(tokens, positionAfterIf)
        expressionStack.push(booleanExpression)
        stack.push(StatementParserLocation.LOCATION_IF)
        stack.push(StatementParserLocation.LOCATION_START)
        return positionAfterBooleanExpression
    }
}