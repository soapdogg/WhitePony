package compiler.parser.impl

import compiler.core.nodes.parsed.*
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.parser.impl.internal.*
import compiler.parser.impl.internal.IContinueParsingDeterminer
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IReducer
import compiler.parser.impl.internal.IShifter
import compiler.parser.impl.internal.IStackGenerator

internal class ShiftReduceExpressionParser(
    private val stackGenerator: IStackGenerator,
    private val shifter: IShifter,
    private val reducer: IReducer,
    private val continueParsingDeterminer: IContinueParsingDeterminer
): IExpressionParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val parseStack = stackGenerator.generateNewStack(IShiftReduceStackItem::class.java)
        var currentPosition = startingPosition
        var hasNotSeenParentheses = true
        var leftRightParentheses = 0
        var leftRightBracket = 0
        var shouldBreak: Boolean
        var continueParsing: Boolean

        do {
            currentPosition = shifter.shift(tokens, currentPosition, parseStack)
            val lookAhead = tokens[currentPosition]
            val reducerResult = reducer.reduce(lookAhead.value, parseStack, leftRightParentheses, leftRightBracket, hasNotSeenParentheses)
            leftRightParentheses = reducerResult.leftRightParentheses
            leftRightBracket = reducerResult.leftRightBracket
            hasNotSeenParentheses = reducerResult.hasNotSeenParentheses
            shouldBreak = reducerResult.shouldBreak
            continueParsing = continueParsingDeterminer.shouldContinueParsing(shouldBreak, lookAhead.type, hasNotSeenParentheses, leftRightParentheses)
        } while (continueParsing)
        
        if(shouldBreak) --currentPosition

        val resultStackItem = parseStack.pop() as NodeShiftReduceStackItem
        return Pair(resultStackItem.node, currentPosition)
    }
}