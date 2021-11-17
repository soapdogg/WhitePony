package compiler.printer.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IExpressionPrinter

class ExpressionPrinterRecursive : IExpressionPrinter {
    override fun printParsedNode(node: IParsedExpressionNode): String {
        return when(node) {
            is ParsedBinaryArrayOperatorNode -> {
                printParsedNode(node.variableExpression) + PrinterConstants.LEFT_BRACKET + printParsedNode(node.insideExpression) + PrinterConstants.RIGHT_BRACKET
            }
            is ParsedBinaryAssignNode -> {
                printParsedNode(node.leftExpression) + PrinterConstants.SPACE + PrinterConstants.EQUALS + PrinterConstants.SPACE + printParsedNode(node.rightExpression)
            }
            is ParsedBinaryAssignOperatorNode -> {
                printParsedNode(node.leftExpression) + PrinterConstants.SPACE + node.operator + PrinterConstants.EQUALS + PrinterConstants.SPACE + printParsedNode(node.rightExpression)
            }
            is ParsedBinaryOperatorNode -> {
                printParsedNode(node.leftExpression) + PrinterConstants.SPACE + node.operator +  PrinterConstants.SPACE + printParsedNode(node.rightExpression)
            }
            is ParsedBinaryAndOperatorNode -> {
                printParsedNode(node.leftExpression) + PrinterConstants.SPACE + PrinterConstants.AMPERSAND + PrinterConstants.AMPERSAND + PrinterConstants.SPACE + printParsedNode(node.rightExpression)
            }
            is ParsedBinaryOrOperatorNode -> {
                printParsedNode(node.leftExpression) + PrinterConstants.SPACE + PrinterConstants.VERTICAL_BAR + PrinterConstants.VERTICAL_BAR + PrinterConstants.SPACE + printParsedNode(node.rightExpression)
            }
            is ParsedBinaryRelationalOperatorNode -> {
                printParsedNode(node.leftExpression) + PrinterConstants.SPACE + node.operator + PrinterConstants.SPACE + printParsedNode(node.rightExpression)
            }
            is ParsedInnerExpression -> {
                PrinterConstants.LEFT_PARENTHESES + printParsedNode(node.expression) + PrinterConstants.RIGHT_PARENTHESES
            }
            is ParsedUnaryOperatorNode -> {
                node.operator + printParsedNode(node.expression)
            }
            is ParsedUnaryNotOperatorNode -> {
                PrinterConstants.EXCLAMATION_POINT + printParsedNode(node.expression)
            }
            is ParsedUnaryPreOperatorNode -> {
                node.operator + node.operator + printParsedNode(node.expression)
            }
            is ParsedUnaryPostOperatorNode -> {
                printParsedNode(node.expression) + node.operator + node.operator
            }
            is ParsedConstantNode -> {
                node.value
            }
            is ParsedVariableExpressionNode -> {
                node.variableValue
            }
            else -> {
                node.toString()
            }
        }
    }
}