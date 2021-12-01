package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.*
import compiler.core.nodes.parsed.*
import compiler.core.stack.ExpressionPrinterStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.printer.impl.internal.IExpressionPrinter

internal class ExpressionPrinter: IExpressionPrinter {

    override fun printNode(node: IExpressionNode): String {
        return printParsedNode(node)
    }

    private fun printParsedNode(node: IExpressionNode): String {
        val stack = Stack<ExpressionPrinterStackItem>()
        val resultStack = Stack<String>()
        stack.push(ExpressionPrinterStackItem(node, LocationConstants.LOCATION_1))

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            when(top.node) {
                is IParsedBinaryExpressionNode -> {
                    when(top.location) {
                        LocationConstants.LOCATION_1 -> {
                            stack.push(ExpressionPrinterStackItem(top.node, LocationConstants.LOCATION_2))
                            stack.push(ExpressionPrinterStackItem(top.node.leftExpression, LocationConstants.LOCATION_1))
                        }
                        LocationConstants.LOCATION_2 -> {
                            stack.push(ExpressionPrinterStackItem(top.node, LocationConstants.LOCATION_3))
                            stack.push(ExpressionPrinterStackItem(top.node.rightExpression, LocationConstants.LOCATION_1))
                        }
                        LocationConstants.LOCATION_3 -> {
                            val result = when(top.node) {
                                is ParsedBinaryAssignExpressionNode -> {
                                    val rightExpressionString = resultStack.pop()
                                    val leftExpressionString = resultStack.pop()
                                    leftExpressionString +
                                            PrinterConstants.SPACE +
                                            PrinterConstants.EQUALS +
                                            PrinterConstants.SPACE +
                                            rightExpressionString
                                }
                                is ParsedBinaryAssignOperatorExpressionNode -> {
                                    val rightExpressionString = resultStack.pop()
                                    val leftExpressionString = resultStack.pop()
                                    leftExpressionString +
                                            PrinterConstants.SPACE +
                                            top.node.operator +
                                            PrinterConstants.EQUALS +
                                            PrinterConstants.SPACE +
                                            rightExpressionString
                                }
                                is ParsedBinaryAndOperatorExpressionNode -> {
                                    val rightExpressionString = resultStack.pop()
                                    val leftExpressionString = resultStack.pop()
                                    leftExpressionString +
                                            PrinterConstants.SPACE +
                                            PrinterConstants.AMPERSAND +
                                            PrinterConstants.AMPERSAND +
                                            PrinterConstants.SPACE +
                                            rightExpressionString
                                }
                                is ParsedBinaryOrOperatorExpressionNode -> {
                                    val rightExpressionString = resultStack.pop()
                                    val leftExpressionString = resultStack.pop()
                                    leftExpressionString +
                                            PrinterConstants.SPACE +
                                            PrinterConstants.VERTICAL_BAR +
                                            PrinterConstants.VERTICAL_BAR +
                                            PrinterConstants.SPACE +
                                            rightExpressionString
                                }
                                is ParsedBinaryRelationalOperatorExpressionNode -> {
                                    val rightExpressionString = resultStack.pop()
                                    val leftExpressionString = resultStack.pop()
                                    leftExpressionString +
                                            PrinterConstants.SPACE +
                                            top.node.operator +
                                            PrinterConstants.SPACE +
                                            rightExpressionString
                                }
                                is ParsedBinaryOperatorExpressionNode -> {
                                    val rightExpressionString = resultStack.pop()
                                    val leftExpressionString = resultStack.pop()
                                    leftExpressionString +
                                            PrinterConstants.SPACE +
                                            top.node.operator +
                                            PrinterConstants.SPACE +
                                            rightExpressionString
                                }
                                is ParsedBinaryArrayExpressionNode -> {
                                    val rightExpressionString = resultStack.pop()
                                    val leftExpressionString = resultStack.pop()
                                    leftExpressionString +
                                            PrinterConstants.LEFT_BRACKET +
                                            rightExpressionString +
                                            PrinterConstants.RIGHT_BRACKET

                                }
                                else -> {
                                    PrinterConstants.EMPTY
                                }
                            }
                            resultStack.push(result)
                        }
                    }
                }
                is IParsedUnaryExpressionNode -> {
                    when(top.location) {
                        LocationConstants.LOCATION_1 -> {
                            stack.push(ExpressionPrinterStackItem(top.node, LocationConstants.LOCATION_2))
                            stack.push(ExpressionPrinterStackItem(top.node.expression, LocationConstants.LOCATION_1))
                        }
                        LocationConstants.LOCATION_2 -> {
                            val result = when(top.node) {
                                is ParsedInnerExpressionNode -> {
                                    val expressionString = resultStack.pop()
                                    PrinterConstants.LEFT_PARENTHESES +
                                            expressionString +
                                            PrinterConstants.RIGHT_PARENTHESES
                                }
                                is ParsedUnaryExpressionNode -> {
                                    val expressionString = resultStack.pop()
                                    top.node.operator + expressionString
                                }
                                is ParsedUnaryNotOperatorExpressionNode -> {
                                    val expressionString = resultStack.pop()
                                    PrinterConstants.EXCLAMATION_POINT + expressionString
                                }
                                is ParsedUnaryPreOperatorExpressionNode -> {
                                    val expressionString = resultStack.pop()
                                    top.node.operator + top.node.operator + expressionString
                                }
                                is ParsedUnaryPostOperatorExpressionNode -> {
                                    val expressionString = resultStack.pop()
                                    expressionString + top.node.operator + top.node.operator
                                }
                                else -> {
                                    PrinterConstants.EMPTY
                                }
                            }
                            resultStack.push(result)
                        }
                    }
                }
                is IParsedValueExpressionNode -> {
                    resultStack.push(top.node.value)
                }
            }
        }
        return resultStack.pop()
    }
}