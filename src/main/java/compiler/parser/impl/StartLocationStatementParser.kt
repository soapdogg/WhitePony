package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IStatementParser

internal class StartLocationStatementParser(
    private val tokenTypeToParserMap: Map<TokenType, IStatementParser>,
    private val startExpressionStatementParser: IStatementParser,
): IStatementParser {
    override fun parse(
        tokens: List<Token>,
        tokenPosition: Int,
        stack: Stack<StatementParserLocation>,
        resultStack: Stack<IParsedStatementNode>,
        expressionStack: Stack<IParsedExpressionNode>,
        numberOfStatementsBlockStack: Stack<Int>
    ): Int {
        val parser = tokenTypeToParserMap.getOrDefault(tokens[tokenPosition].type, startExpressionStatementParser)
        return parser.parse(tokens, tokenPosition, stack, resultStack, expressionStack, numberOfStatementsBlockStack)
    }
}