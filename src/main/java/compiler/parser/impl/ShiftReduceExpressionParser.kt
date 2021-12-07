package compiler.parser.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedConstantExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser

internal class ShiftReduceExpressionParser(
    private val acceptedTokenTypes: Set<TokenType>
): IExpressionParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val parseStack = Stack<IShiftReduceStackItem>()
        var currentPosition = startingPosition
        var lookAhead = tokens[currentPosition]

        do {

            if (lookAhead.type == TokenType.INTEGER || lookAhead.type == TokenType.FLOATING_POINT) {
                val type = if (lookAhead.type == TokenType.INTEGER) PrinterConstants.INT else PrinterConstants.DOUBLE
                parseStack.push(NodeShiftReduceStackItem(ParsedConstantExpressionNode(lookAhead.value, type)))
            } else if (lookAhead.type == TokenType.IDENTIFIER) {
                parseStack.push(NodeShiftReduceStackItem(ParsedVariableExpressionNode(lookAhead.value)))
            }

            ++currentPosition
            lookAhead = tokens[currentPosition]

        } while (acceptedTokenTypes.contains(lookAhead.type))

        val resultStackItem = parseStack.pop() as NodeShiftReduceStackItem
        return Pair(resultStackItem.node, currentPosition)
    }
}