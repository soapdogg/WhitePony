package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedForNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.parser.impl.internal.IStatementParser

internal class EndForStatementParser: IStatementParser{
    override fun parse(
        tokens: List<Token>,
        tokenPosition: Int,
        stack: Stack<StatementParserLocation>,
        resultStack: Stack<IParsedStatementNode>,
        expressionStack: Stack<IParsedExpressionNode>,
        numberOfStatementsBlockStack: Stack<Int>,
        useShiftReduce: Boolean
    ): Int {
        val initExpression = expressionStack.pop()
        val testExpression = expressionStack.pop()
        val incrementExpression = expressionStack.pop()
        val body = resultStack.pop()
        val forNode = ParsedForNode(
            initExpression,
            testExpression,
            incrementExpression,
            body
        )
        resultStack.push(forNode)
        return tokenPosition
    }
}