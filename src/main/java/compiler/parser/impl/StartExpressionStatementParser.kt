package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.parser.impl.internal.IExpressionStatementParser
import compiler.parser.impl.internal.IStatementParser

internal class StartExpressionStatementParser(
    private val expressionStatementParser: IExpressionStatementParser
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
        val (expressionStatement, positionAfterExpression) = expressionStatementParser.parse(
            tokens,
            tokenPosition,
            false
        )
        resultStack.push(expressionStatement)
        return positionAfterExpression
    }
}