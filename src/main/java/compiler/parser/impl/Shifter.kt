package compiler.parser.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.ParsedConstantExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IShifter

internal class Shifter: IShifter {
    override fun shift(
        tokens: List<Token>,
        currentPosition: Int,
        parseStack: Stack<IShiftReduceStackItem>
    ): Int {
        val lookAhead = tokens[currentPosition]
        val stackItem = when (lookAhead.type) {
            TokenType.INTEGER, TokenType.FLOATING_POINT -> {
                val type = if (lookAhead.type == TokenType.INTEGER) PrinterConstants.INT else PrinterConstants.DOUBLE
                NodeShiftReduceStackItem(ParsedConstantExpressionNode(lookAhead.value, type))
            }
            TokenType.IDENTIFIER -> {
                NodeShiftReduceStackItem(ParsedVariableExpressionNode(lookAhead.value))
            }
            else -> {
                OperatorShiftReduceStackItem(lookAhead.value)
            }
        }

        parseStack.push(stackItem)

        return currentPosition + 1
    }
}