package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.ReducerResult
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IContinueParsingDeterminer
import compiler.parser.impl.internal.IReducer
import compiler.parser.impl.internal.IShifter
import compiler.parser.impl.internal.IStackGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ShiftReduceExpressionParserTest {
    private val stackGenerator = Mockito.mock(IStackGenerator::class.java)
    private val shifter = Mockito.mock(IShifter::class.java)
    private val reducer = Mockito.mock(IReducer::class.java)
    private val continueParsingDeterminer = Mockito.mock(IContinueParsingDeterminer::class.java)

    private val shiftReduceExpressionParser = ShiftReduceExpressionParser(
        stackGenerator,
        shifter,
        reducer,
        continueParsingDeterminer
    )

    @Test
    fun parseTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)
        val startingPosition = 0

        val parseStack = Stack<IShiftReduceStackItem>()
        Mockito.`when`(stackGenerator.generateNewStack(IShiftReduceStackItem::class.java)).thenReturn(parseStack)

        Mockito.`when`(shifter.shift(tokens, startingPosition, parseStack)).thenReturn(startingPosition)

        val lookAheadValue = "lookAheadValue"
        Mockito.`when`(token.value).thenReturn(lookAheadValue)

        val result = Mockito.mock(NodeShiftReduceStackItem::class.java)
        val reducerResult = Mockito.mock(ReducerResult::class.java)
        Mockito.`when`(
            reducer.reduce(
                lookAheadValue,
                parseStack,
                0,
                0,
                true
            )
        ).then{
            parseStack.push(result)
            return@then reducerResult
        }

        val lrp = 9
        Mockito.`when`(reducerResult.leftRightParentheses).thenReturn(lrp)

        val lrb = 8
        Mockito.`when`(reducerResult.leftRightBracket).thenReturn(lrb)

        val noParentheses = true
        Mockito.`when`(reducerResult.hasNotSeenParentheses).thenReturn(noParentheses)

        val shouldBreak = true
        Mockito.`when`(reducerResult.shouldBreak).thenReturn(shouldBreak)

        val type = TokenType.TYPE
        Mockito.`when`(token.type).thenReturn(type)

        Mockito.`when`(continueParsingDeterminer.shouldContinueParsing(shouldBreak, type, noParentheses, lrp)).thenReturn(false)

        val node = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(result.node).thenReturn(node)

        val (actual, actualNextPosition) = shiftReduceExpressionParser.parse(tokens, startingPosition)
        Assertions.assertEquals(node, actual)
        Assertions.assertEquals(startingPosition - 1, actualNextPosition)
    }
}