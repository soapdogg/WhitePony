package compiler.frontend.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.frontend.parser.impl.internal.IStatementParser
import compiler.frontend.parser.impl.internal.IVariableDeclarationListParser

internal class StartVariableDeclarationListStatementParser(
    private val variableDeclarationListParser: IVariableDeclarationListParser
): IStatementParser {
    override fun parse(
        tokens: List<Token>,
        tokenPosition: Int,
        stack: Stack<StatementParserLocation>,
        resultStack: Stack<IParsedStatementNode>,
        expressionStack: Stack<IParsedExpressionNode>,
        numberOfStatementsBlockStack: Stack<Int>
    ): Int {
        val (variableStatement, positionAfterVariable) = variableDeclarationListParser.parse(
            tokens,
            tokenPosition
        )
        resultStack.push(variableStatement)
        return positionAfterVariable
    }
}