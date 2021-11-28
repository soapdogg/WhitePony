package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IStackGenerator
import compiler.parser.impl.internal.IStatementParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class StatementParserOrchestratorTest {
    private val stackGenerator = Mockito.mock(IStackGenerator::class.java)
    private val location = StatementParserLocation.LOCATION_START
    private val parser = Mockito.mock(IStatementParser::class.java)
    private val locationToParserMap = mapOf(
        location to parser
    )

    private val statementParserOrchestrator = StatementParserOrchestrator(
        stackGenerator,
        locationToParserMap
    )

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0

        val stack = Stack<StatementParserLocation>()
        Mockito.`when`(stackGenerator.generateNewStack(StatementParserLocation::class.java)).thenReturn(stack)

        val resultStack = Stack<IParsedStatementNode>()
        Mockito.`when`(stackGenerator.generateNewStack(IParsedStatementNode::class.java)).thenReturn(resultStack)

        val expressionStack = Stack<IParsedExpressionNode>()
        Mockito.`when`(stackGenerator.generateNewStack(IParsedExpressionNode::class.java)).thenReturn(expressionStack)

        val numberOfStatementsBlockStack = Stack<Int>()
        Mockito.`when`(stackGenerator.generateNewStack(Int::class.java)).thenReturn(numberOfStatementsBlockStack)

        val result = Mockito.mock(ParsedBasicBlockNode::class.java)
        val positionAfterStatement = 1
        Mockito.`when`(
            parser.parse(
                tokens,
                startingPosition,
                stack,
                resultStack,
                expressionStack,
                numberOfStatementsBlockStack
            )
        ).then{
            resultStack.push(result)
            return@then positionAfterStatement
        }

        val (actual, actualPosition) = statementParserOrchestrator.parse(tokens, startingPosition)
        Assertions.assertEquals(result, actual)
        Assertions.assertEquals(positionAfterStatement, actualPosition)
    }
}