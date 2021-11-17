package compiler.printer.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IExpressionPrinter

class ExpressionPrinterRecursive : IExpressionPrinter {
    override fun printParsedNode(node: IParsedExpressionNode): String {
        return when(node) {
            is ParsedBinaryArrayOperatorNode -> {
                val leftExpressionString = printParsedNode(node.variableExpression)
                val rightExpressionString = printParsedNode(node.insideExpression)
                leftExpressionString +
                        PrinterConstants.LEFT_BRACKET +
                        rightExpressionString +
                        PrinterConstants.RIGHT_BRACKET
            }
            is ParsedBinaryAssignNode -> {
                val leftExpressionString = printParsedNode(node.leftExpression)
                val rightExpressionString = printParsedNode(node.rightExpression)
                leftExpressionString +
                        PrinterConstants.SPACE +
                        PrinterConstants.EQUALS +
                        PrinterConstants.SPACE +
                        rightExpressionString
            }
            is ParsedBinaryAssignOperatorNode -> {
                val leftExpressionString = printParsedNode(node.leftExpression)
                val rightExpressionString = printParsedNode(node.rightExpression)
                leftExpressionString +
                        PrinterConstants.SPACE +
                        node.operator +
                        PrinterConstants.EQUALS +
                        PrinterConstants.SPACE +
                        rightExpressionString
            }
            is ParsedBinaryOperatorNode -> {
                val leftExpressionString = printParsedNode(node.leftExpression)
                val rightExpressionString = printParsedNode(node.rightExpression)
                leftExpressionString +
                        PrinterConstants.SPACE +
                        node.operator +
                        PrinterConstants.SPACE +
                        rightExpressionString
            }
            is ParsedBinaryAndOperatorNode -> {
                val leftExpressionString = printParsedNode(node.leftExpression)
                val rightExpressionString = printParsedNode(node.rightExpression)
                leftExpressionString +
                        PrinterConstants.SPACE +
                        PrinterConstants.AMPERSAND +
                        PrinterConstants.AMPERSAND +
                        PrinterConstants.SPACE +
                        rightExpressionString
            }
            is ParsedBinaryOrOperatorNode -> {
                val leftExpressionString = printParsedNode(node.leftExpression)
                val rightExpressionString = printParsedNode(node.rightExpression)
                leftExpressionString +
                        PrinterConstants.SPACE +
                        PrinterConstants.VERTICAL_BAR +
                        PrinterConstants.VERTICAL_BAR +
                        PrinterConstants.SPACE +
                        rightExpressionString
            }
            is ParsedBinaryRelationalOperatorNode -> {
                val leftExpressionString = printParsedNode(node.leftExpression)
                val rightExpressionString = printParsedNode(node.rightExpression)
                leftExpressionString +
                        PrinterConstants.SPACE +
                        node.operator +
                        PrinterConstants.SPACE +
                        rightExpressionString
            }
            is ParsedInnerExpression -> {
                val expressionString = printParsedNode(node.expression)
                PrinterConstants.LEFT_PARENTHESES +
                        expressionString +
                        PrinterConstants.RIGHT_PARENTHESES
            }
            is ParsedUnaryOperatorNode -> {
                val expressionString = printParsedNode(node.expression)
                node.operator + expressionString
            }
            is ParsedUnaryNotOperatorNode -> {
                val expressionString = printParsedNode(node.expression)
                PrinterConstants.EXCLAMATION_POINT + expressionString
            }
            is ParsedUnaryPreOperatorNode -> {
                val expressionString = printParsedNode(node.expression)
                node.operator + node.operator + expressionString
            }
            is ParsedUnaryPostOperatorNode -> {
                val expressionString = printParsedNode(node.expression)
                expressionString + node.operator + node.operator
            }
            is ParsedConstantNode -> {
                node.value
            }
            is ParsedVariableExpressionNode -> {
                node.variableValue
            }
            else -> {
                PrinterConstants.EMPTY
            }
        }
    }
}