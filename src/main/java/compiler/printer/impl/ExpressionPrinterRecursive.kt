package compiler.printer.impl

import compiler.core.*
import compiler.printer.impl.internal.IExpressionPrinter

class ExpressionPrinterRecursive : IExpressionPrinter {
    override fun printParsedNode(node: IParsedExpressionNode): String {
        return when(node) {
            is ParsedBinaryArrayOperatorNode -> {
                printParsedNode(node.variableExpression) + "[" + printParsedNode(node.insideExpression) + "]"
            }
            is ParsedBinaryAssignNode -> {
                printParsedNode(node.leftExpression) + " = " + printParsedNode(node.rightExpression)
            }
            is ParsedBinaryAssignOperatorNode -> {
                printParsedNode(node.leftExpression) + " " + node.operator + "= " + printParsedNode(node.rightExpression)
            }
            is ParsedBinaryOperatorNode -> {
                printParsedNode(node.leftExpression) + " " + node.operator +  " " + printParsedNode(node.rightExpression)
            }
            is ParsedBinaryAndOperatorNode -> {
                printParsedNode(node.leftExpression) + " && " + printParsedNode(node.rightExpression)
            }
            is ParsedBinaryOrOperatorNode -> {
                printParsedNode(node.leftExpression) + " || " + printParsedNode(node.rightExpression)
            }
            is ParsedBinaryRelationalOperatorNode -> {
                printParsedNode(node.leftExpression) + " " + node.operator + " " + printParsedNode(node.rightExpression)
            }
            is ParsedInnerExpression -> {
                "(" + printParsedNode(node.expression) + ")"
            }
            is ParsedUnaryOperatorNode -> {
                node.operator + printParsedNode(node.expression)
            }
            is ParsedUnaryNotOperatorNode -> {
                "!" + printParsedNode(node.expression)
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