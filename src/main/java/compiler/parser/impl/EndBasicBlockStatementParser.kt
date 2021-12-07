package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class EndBasicBlockStatementParser(
    private val tokenTypeAsserter: ITokenTypeAsserter
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
        if (tokens[tokenPosition].type != TokenType.RIGHT_BRACE) {
            val numberOfStatementsInBlock = numberOfStatementsBlockStack.pop()
            numberOfStatementsBlockStack.push(numberOfStatementsInBlock + 1)
            stack.push(StatementParserLocation.LOCATION_BASIC_BLOCK)
            stack.push(StatementParserLocation.LOCATION_START)
            return tokenPosition
        }
        val (_, positionAfterRightBrace) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.RIGHT_BRACE)
        val statements = mutableListOf<IParsedStatementNode>()
        val numberOfStatementsInBlock = numberOfStatementsBlockStack.pop()
        for (i in 0 until numberOfStatementsInBlock) {
            statements.add(resultStack.pop())
        }
        statements.reverse()
        val basicBlockNode = ParsedBasicBlockNode(statements)
        resultStack.push(basicBlockNode)
        return positionAfterRightBrace
    }
}