package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.parser.impl.internal.IStackGenerator
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.IStatementParserOrchestrator

internal class StatementParserOrchestrator(
    private val stackGenerator: IStackGenerator,
    private val locationToParserMap : Map<StatementParserLocation, IStatementParser>
): IStatementParserOrchestrator {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
        useShiftReduce: Boolean
    ): Pair<ParsedBasicBlockNode, Int> {
        val stack = stackGenerator.generateNewStack(StatementParserLocation::class.java)
        val resultStack = stackGenerator.generateNewStack(IParsedStatementNode::class.java)
        val expressionStack = stackGenerator.generateNewStack(IParsedExpressionNode::class.java)
        stack.push(StatementParserLocation.LOCATION_START)
        var tokenPosition = startingPosition
        val numberOfStatementsBlockStack = stackGenerator.generateNewStack(Int::class.java)

        while(stack.isNotEmpty()) {
            val top = stack.pop()
            val parser = locationToParserMap.getValue(top)
            tokenPosition = parser.parse(
                tokens,
                tokenPosition,
                stack,
                resultStack,
                expressionStack,
                numberOfStatementsBlockStack,
                useShiftReduce
            )
        }
        return Pair(resultStack.pop() as ParsedBasicBlockNode, tokenPosition)
    }
}