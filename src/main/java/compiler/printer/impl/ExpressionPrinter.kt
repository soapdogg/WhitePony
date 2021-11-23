package compiler.printer.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IExpressionPrinter

internal class ExpressionPrinter: IExpressionPrinter {

    override fun printNode(node: IExpressionNode): String {
        return if (node is IParsedExpressionNode) {
            printParsedNode(node)
        } else {
            PrinterConstants.EMPTY
        }
    }

    private fun printParsedNode(node: IParsedExpressionNode): String {
        val stack = Stack<ExpressionPrinterStackItem>()
        val resultStack = Stack<String>()
        stack.push(ExpressionPrinterStackItem(node, PrinterConstants.LOCATION_1))

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            when(top.node) {
                is IParsedBinaryExpressionNode -> {
                    when(top.location) {
                        PrinterConstants.LOCATION_1 -> {
                            stack.push(ExpressionPrinterStackItem(top.node, PrinterConstants.LOCATION_2))
                            stack.push(ExpressionPrinterStackItem(top.node.leftExpression, PrinterConstants.LOCATION_1))
                        }
                        PrinterConstants.LOCATION_2 -> {
                            stack.push(ExpressionPrinterStackItem(top.node, PrinterConstants.LOCATION_3))
                            stack.push(ExpressionPrinterStackItem(top.node.rightExpression, PrinterConstants.LOCATION_1))
                        }
                        PrinterConstants.LOCATION_3 -> {
                            val result = when(top.node) {
                                is ParsedBinaryAssignNode -> {
                                    val rightExpressionString = resultStack.pop()
                                    val leftExpressionString = resultStack.pop()
                                    leftExpressionString +
                                            PrinterConstants.SPACE +
                                            PrinterConstants.EQUALS +
                                            PrinterConstants.SPACE +
                                            rightExpressionString
                                }
                                is ParsedBinaryAssignOperatorNode -> {
                                    val rightExpressionString = resultStack.pop()
                                    val leftExpressionString = resultStack.pop()
                                    leftExpressionString +
                                            PrinterConstants.SPACE +
                                            top.node.operator +
                                            PrinterConstants.EQUALS +
                                            PrinterConstants.SPACE +
                                            rightExpressionString
                                }
                                is ParsedBinaryAndOperatorNode -> {
                                    val rightExpressionString = resultStack.pop()
                                    val leftExpressionString = resultStack.pop()
                                    leftExpressionString +
                                            PrinterConstants.SPACE +
                                            PrinterConstants.AMPERSAND +
                                            PrinterConstants.AMPERSAND +
                                            PrinterConstants.SPACE +
                                            rightExpressionString
                                }
                                is ParsedBinaryOrOperatorNode -> {
                                    val rightExpressionString = resultStack.pop()
                                    val leftExpressionString = resultStack.pop()
                                    leftExpressionString +
                                            PrinterConstants.SPACE +
                                            PrinterConstants.VERTICAL_BAR +
                                            PrinterConstants.VERTICAL_BAR +
                                            PrinterConstants.SPACE +
                                            rightExpressionString
                                }
                                is ParsedBinaryRelationalOperatorNode -> {
                                    val rightExpressionString = resultStack.pop()
                                    val leftExpressionString = resultStack.pop()
                                    leftExpressionString +
                                            PrinterConstants.SPACE +
                                            top.node.operator +
                                            PrinterConstants.SPACE +
                                            rightExpressionString
                                }
                                is ParsedBinaryOperatorNode -> {
                                    val rightExpressionString = resultStack.pop()
                                    val leftExpressionString = resultStack.pop()
                                    leftExpressionString +
                                            PrinterConstants.SPACE +
                                            top.node.operator +
                                            PrinterConstants.SPACE +
                                            rightExpressionString
                                }
                                is ParsedBinaryArrayOperatorNode -> {
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
                        PrinterConstants.LOCATION_1 -> {
                            stack.push(ExpressionPrinterStackItem(top.node, PrinterConstants.LOCATION_2))
                            stack.push(ExpressionPrinterStackItem(top.node.expression, PrinterConstants.LOCATION_1))
                        }
                        PrinterConstants.LOCATION_2 -> {
                            val result = when(top.node) {
                                is ParsedInnerExpression -> {
                                    val expressionString = resultStack.pop()
                                    PrinterConstants.LEFT_PARENTHESES +
                                            expressionString +
                                            PrinterConstants.RIGHT_PARENTHESES
                                }
                                is ParsedUnaryOperatorNode -> {
                                    val expressionString = resultStack.pop()
                                    top.node.operator + expressionString
                                }
                                is ParsedUnaryNotOperatorNode -> {
                                    val expressionString = resultStack.pop()
                                    PrinterConstants.EXCLAMATION_POINT + expressionString
                                }
                                is ParsedUnaryPreOperatorNode -> {
                                    val expressionString = resultStack.pop()
                                    top.node.operator + top.node.operator + expressionString
                                }
                                is ParsedUnaryPostOperatorNode -> {
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