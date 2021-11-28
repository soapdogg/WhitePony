package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.parser.impl.internal.IReturnStatementParser
import compiler.parser.impl.internal.IStatementParser

internal class StartReturnStatementParser(
    private val returnStatementParser: IReturnStatementParser
): IStatementParser {
    override fun parse(
        tokens: List<Token>,
        tokenPosition: Int,
        stack: Stack<Int>,
        resultStack: Stack<IParsedStatementNode>,
        expressionStack: Stack<IParsedExpressionNode>,
        numberOfStatementsBlockStack: Stack<Int>
    ): Int {
        val (resultStatement, positionAfterReturn) = returnStatementParser.parse(tokens, tokenPosition)
        resultStack.push(resultStatement)
        return  positionAfterReturn
    }
}