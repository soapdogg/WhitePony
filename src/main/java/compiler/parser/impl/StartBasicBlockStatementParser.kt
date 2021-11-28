package compiler.parser.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class StartBasicBlockStatementParser(
    private val tokenTypeAsserter: ITokenTypeAsserter
): IStatementParser {
    override fun parse(
        tokens: List<Token>,
        tokenPosition: Int,
        stack: Stack<StatementParserLocation>,
        resultStack: Stack<IParsedStatementNode>,
        expressionStack: Stack<IParsedExpressionNode>,
        numberOfStatementsBlockStack: Stack<Int>
    ): Int {
        val (_, positionAfterLeftBrace) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.LEFT_BRACE)
        if (tokens[positionAfterLeftBrace].type != TokenType.RIGHT_BRACE) {
            numberOfStatementsBlockStack.push(PrinterConstants.ONE.toInt())
            stack.push(StatementParserLocation.LOCATION_BASIC_BLOCK)
            stack.push(StatementParserLocation.LOCATION_START)
            return positionAfterLeftBrace
        }
        val (_, positionAfterRightBrace) = tokenTypeAsserter.assertTokenType(tokens, positionAfterLeftBrace, TokenType.RIGHT_BRACE)
        val basicBlockNode = ParsedBasicBlockNode(listOf())
        resultStack.push(basicBlockNode)
        return positionAfterRightBrace
    }
}