package compiler.printer.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito
import java.util.stream.Stream

class ExpressionPrinterTest {

    private val expressionPrinter = ExpressionPrinter()

    @ParameterizedTest
    @MethodSource("inputData")
    fun printExpressionTest(arguments: ArgumentsAccessor) {
        val pair = arguments.get(0) as Pair<*, *>
        val input = pair.first as IParsedExpressionNode
        val expected = pair.second as String

        val actual = expressionPrinter.printNode(input)
        Assertions.assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        fun inputData(): Stream<Pair<IParsedExpressionNode, String>> {

            val parsedValueExpressionNode = Mockito.mock(ParsedVariableExpressionNode::class.java)
            val value = "value"
            Mockito.`when`(parsedValueExpressionNode.value).thenReturn(value)

            val parsedInnerExpression = Mockito.mock(ParsedInnerExpression::class.java)
            Mockito.`when`(parsedInnerExpression.expression).thenReturn(parsedValueExpressionNode)
            val expectedInnerExpressionString = PrinterConstants.LEFT_PARENTHESES +
                    value +
                    PrinterConstants.RIGHT_PARENTHESES

            val parsedUnaryOperator = Mockito.mock(ParsedUnaryOperatorNode::class.java)
            Mockito.`when`(parsedUnaryOperator.expression).thenReturn(parsedValueExpressionNode)
            val operator = "operator"
            Mockito.`when`(parsedUnaryOperator.operator).thenReturn(operator)
            val expectedUnaryOperatorString = operator + value

            val parsedUnaryNotOperator = Mockito.mock(ParsedUnaryNotOperatorNode::class.java)
            Mockito.`when`(parsedUnaryNotOperator.expression).thenReturn(parsedValueExpressionNode)
            val expectedUnaryNotOperatorString = PrinterConstants.EXCLAMATION_POINT + value

            val parsedUnaryPreOperator = Mockito.mock(ParsedUnaryPreOperatorNode::class.java)
            Mockito.`when`(parsedUnaryPreOperator.expression).thenReturn(parsedValueExpressionNode)
            Mockito.`when`(parsedUnaryPreOperator.operator).thenReturn(operator)
            val expectedUnaryPreOperatorString = operator + operator + value

            val parsedUnaryPostOperator = Mockito.mock(ParsedUnaryPostOperatorNode::class.java)
            Mockito.`when`(parsedUnaryPostOperator.expression).thenReturn(parsedValueExpressionNode)
            Mockito.`when`(parsedUnaryPostOperator.operator).thenReturn(operator)
            val expectedUnaryPostOperatorString = value + operator + operator

            val parsedUnaryExpression = Mockito.mock(IParsedUnaryExpressionNode::class.java)
            Mockito.`when`(parsedUnaryExpression.expression).thenReturn(parsedValueExpressionNode)
            val expectedUnaryExpressionString = PrinterConstants.EMPTY

            val parsedBinaryAssign = Mockito.mock(ParsedBinaryAssignNode::class.java)
            Mockito.`when`(parsedBinaryAssign.leftExpression).thenReturn(parsedValueExpressionNode)
            Mockito.`when`(parsedBinaryAssign.rightExpression).thenReturn(parsedValueExpressionNode)
            val expectedBinaryAssignString = value + PrinterConstants.SPACE + PrinterConstants.EQUALS + PrinterConstants.SPACE + value

            val parsedBinaryAssignOperator = Mockito.mock(ParsedBinaryAssignOperatorNode::class.java)
            Mockito.`when`(parsedBinaryAssignOperator.leftExpression).thenReturn(parsedValueExpressionNode)
            Mockito.`when`(parsedBinaryAssignOperator.rightExpression).thenReturn(parsedValueExpressionNode)
            Mockito.`when`(parsedBinaryAssignOperator.operator).thenReturn(operator)
            val expectedBinaryAssignOperatorString = value + PrinterConstants.SPACE + operator + PrinterConstants.EQUALS + PrinterConstants.SPACE + value

            val parsedBinaryAndOperator = Mockito.mock(ParsedBinaryAndOperatorNode::class.java)
            Mockito.`when`(parsedBinaryAndOperator.leftExpression).thenReturn(parsedValueExpressionNode)
            Mockito.`when`(parsedBinaryAndOperator.rightExpression).thenReturn(parsedValueExpressionNode)
            val expectedBinaryAndOperatorString = value + PrinterConstants.SPACE + PrinterConstants.AMPERSAND + PrinterConstants.AMPERSAND + PrinterConstants.SPACE + value

            val parsedBinaryOrOperator = Mockito.mock(ParsedBinaryOrOperatorNode::class.java)
            Mockito.`when`(parsedBinaryOrOperator.leftExpression).thenReturn(parsedValueExpressionNode)
            Mockito.`when`(parsedBinaryOrOperator.rightExpression).thenReturn(parsedValueExpressionNode)
            val expectedBinaryOrOperatorString = value + PrinterConstants.SPACE + PrinterConstants.VERTICAL_BAR + PrinterConstants.VERTICAL_BAR + PrinterConstants.SPACE + value

            val parsedBinaryRelationalOperator = Mockito.mock(ParsedBinaryRelationalOperatorNode::class.java)
            Mockito.`when`(parsedBinaryRelationalOperator.leftExpression).thenReturn(parsedValueExpressionNode)
            Mockito.`when`(parsedBinaryRelationalOperator.rightExpression).thenReturn(parsedValueExpressionNode)
            Mockito.`when`(parsedBinaryRelationalOperator.operator).thenReturn(operator)
            val expectedBinaryRelationalOperatorString = value + PrinterConstants.SPACE + operator + PrinterConstants.SPACE + value

            val parsedBinaryOperator = Mockito.mock(ParsedBinaryOperatorNode::class.java)
            Mockito.`when`(parsedBinaryOperator.leftExpression).thenReturn(parsedValueExpressionNode)
            Mockito.`when`(parsedBinaryOperator.rightExpression).thenReturn(parsedValueExpressionNode)
            Mockito.`when`(parsedBinaryOperator.operator).thenReturn(operator)
            val expectedBinaryOperatorString = value + PrinterConstants.SPACE + operator + PrinterConstants.SPACE + value

            val parsedBinaryArrayOperator = Mockito.mock(ParsedBinaryArrayOperatorNode::class.java)
            Mockito.`when`(parsedBinaryArrayOperator.leftExpression).thenReturn(parsedValueExpressionNode)
            Mockito.`when`(parsedBinaryArrayOperator.rightExpression).thenReturn(parsedValueExpressionNode)
            val expectedBinaryArrayOperatorString = value + PrinterConstants.LEFT_BRACKET + value + PrinterConstants.RIGHT_BRACKET

            val parsedBinaryExpression = Mockito.mock(IParsedBinaryExpressionNode::class.java)
            Mockito.`when`(parsedBinaryExpression.leftExpression).thenReturn(parsedValueExpressionNode)
            Mockito.`when`(parsedBinaryExpression.rightExpression).thenReturn(parsedValueExpressionNode)
            val expectedBinaryExpressionString = PrinterConstants.EMPTY

            return Stream.of(
                Pair(parsedValueExpressionNode, value),
                Pair(parsedInnerExpression, expectedInnerExpressionString),
                Pair(parsedUnaryOperator, expectedUnaryOperatorString),
                Pair(parsedUnaryNotOperator, expectedUnaryNotOperatorString),
                Pair(parsedUnaryPreOperator, expectedUnaryPreOperatorString),
                Pair(parsedUnaryPostOperator, expectedUnaryPostOperatorString),
                Pair(parsedUnaryExpression, expectedUnaryExpressionString),
                Pair(parsedBinaryAssign, expectedBinaryAssignString),
                Pair(parsedBinaryAssignOperator, expectedBinaryAssignOperatorString),
                Pair(parsedBinaryAndOperator, expectedBinaryAndOperatorString),
                Pair(parsedBinaryOrOperator, expectedBinaryOrOperatorString),
                Pair(parsedBinaryRelationalOperator, expectedBinaryRelationalOperatorString),
                Pair(parsedBinaryOperator, expectedBinaryOperatorString),
                Pair(parsedBinaryArrayOperator, expectedBinaryArrayOperatorString),
                Pair(parsedBinaryExpression, expectedBinaryExpressionString)
            )
        }
    }
}