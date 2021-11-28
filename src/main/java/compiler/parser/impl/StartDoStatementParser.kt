package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocations
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class StartDoStatementParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
): IStatementParser {
    override fun parse(
        tokens: List<Token>,
        tokenPosition: Int,
        stack: Stack<Int>,
        resultStack: Stack<IParsedStatementNode>,
        expressionStack: Stack<IParsedExpressionNode>,
        numberOfStatementsBlockStack: Stack<Int>
    ): Int {
        val (_, positionAfterDo) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.DO)
        stack.push(StatementParserLocations.LOCATION_DO)
        stack.push(StatementParserLocations.LOCATION_START)
        return positionAfterDo
    }
}