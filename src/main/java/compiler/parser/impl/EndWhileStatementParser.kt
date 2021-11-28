package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedWhileNode
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.parser.impl.internal.IStatementParser

internal class EndWhileStatementParser: IStatementParser {
    override fun parse(
        tokens: List<Token>,
        tokenPosition: Int,
        stack: Stack<Int>,
        resultStack: Stack<IParsedStatementNode>,
        expressionStack: Stack<IParsedExpressionNode>,
        numberOfStatementsBlockStack: Stack<Int>
    ): Int {
        val expression = expressionStack.pop()
        val body = resultStack.pop()
        val whileNode = ParsedWhileNode(expression, body)
        resultStack.push(whileNode)
        return tokenPosition
    }
}