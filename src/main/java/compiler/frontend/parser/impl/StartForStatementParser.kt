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

internal class StartForStatementParser(
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
        val (_, positionAfterFor) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.FOR)
        val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterFor, TokenType.LEFT_PARENTHESES)
        val (initExpression, positionAfterInitExpression) = shiftReduceExpressionParser.parse(tokens, positionAfterLeftParentheses)
        val (_, positionAfterFirstSemi) = tokenTypeAsserter.assertTokenType(tokens, positionAfterInitExpression, TokenType.SEMICOLON)
        val (testExpression, positionAfterTestExpression) = shiftReduceExpressionParser.parse(tokens, positionAfterFirstSemi)
        val (_, positionAfterSecondSemi) = tokenTypeAsserter.assertTokenType(tokens, positionAfterTestExpression, TokenType.SEMICOLON)
        val (incrementExpression, positionAfterIncrementExpression) = shiftReduceExpressionParser.parse(tokens, positionAfterSecondSemi)
        val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(tokens,  positionAfterIncrementExpression, TokenType.RIGHT_PARENTHESES)
        expressionStack.push(incrementExpression)
        expressionStack.push(testExpression)
        expressionStack.push(initExpression)
        stack.push(StatementParserLocation.LOCATION_FOR)
        stack.push(StatementParserLocation.LOCATION_START)
        return positionAfterRightParentheses
    }
}