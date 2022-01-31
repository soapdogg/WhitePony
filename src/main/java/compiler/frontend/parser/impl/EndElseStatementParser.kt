package compiler.frontend.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedIfNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.frontend.parser.impl.internal.IStatementParser

internal class EndElseStatementParser: IStatementParser {
    override fun parse(
        tokens: List<Token>,
        tokenPosition: Int,
        stack: Stack<StatementParserLocation>,
        resultStack: Stack<IParsedStatementNode>,
        expressionStack: Stack<IParsedExpressionNode>,
        numberOfStatementsBlockStack: Stack<Int>
    ): Int {
        val booleanExpression = expressionStack.pop()
        val elseBody = resultStack.pop()
        val ifBody = resultStack.pop()
        val elseNode = ParsedIfNode(
            booleanExpression,
            ifBody,
            elseBody
        )
        resultStack.push(elseNode)
        return tokenPosition
    }
}