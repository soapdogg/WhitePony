package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedDoWhileNode
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class EndDoStatementParser(
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
        val body = resultStack.pop()
        val (_, positionAfterWhile) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.WHILE)
        val (expression, positionAfterExpression) = expressionParser.parse(tokens, positionAfterWhile)
        val (_, positionAfterSemicolon) = tokenTypeAsserter.assertTokenType(tokens, positionAfterExpression, TokenType.SEMICOLON)
        val doStatement = ParsedDoWhileNode(
            expression,
            body
        )
        resultStack.push(doStatement)
        return positionAfterSemicolon
    }
}