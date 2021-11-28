package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocations
import compiler.core.tokenizer.Token
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.IStatementParserOrchestrator

internal class StatementParserOrchestrator(
    private val locationToParserMap : Map<Int, IStatementParser>
): IStatementParserOrchestrator {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ParsedBasicBlockNode, Int> {

        val stack = Stack<Int>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        stack.push(StatementParserLocations.LOCATION_START)
        var tokenPosition = startingPosition
        val numberOfStatementsBlockStack = Stack<Int>()

        while(stack.isNotEmpty()) {
            val top = stack.pop()
            val parser = locationToParserMap.getValue(top)
            tokenPosition = parser.parse(tokens, tokenPosition, stack, resultStack, expressionStack, numberOfStatementsBlockStack)
        }
        return Pair(resultStack.pop() as ParsedBasicBlockNode, tokenPosition)
    }
}