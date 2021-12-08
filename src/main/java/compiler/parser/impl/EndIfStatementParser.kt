package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedIfNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class EndIfStatementParser(
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
        if (tokens[tokenPosition].type != TokenType.ELSE) {
            val booleanExpression = expressionStack.pop()
            val ifBody = resultStack.pop()
            val ifNode = ParsedIfNode(
                booleanExpression,
                ifBody,
                null
            )
            resultStack.push(ifNode)
            return tokenPosition
        } else {
            val (_, positionAfterElse) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.ELSE)
            stack.push(StatementParserLocation.LOCATION_ELSE)
            stack.push(StatementParserLocation.LOCATION_START)
            return positionAfterElse
        }
    }
}