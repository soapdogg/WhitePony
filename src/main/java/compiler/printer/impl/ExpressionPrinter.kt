package compiler.printer.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IExpressionPrinter

internal class ExpressionPrinter: IExpressionPrinter {
    override fun printParsedNode(node: IParsedExpressionNode): String {
        val stack = Stack<ExpressionPrinterStackItem>()
        val resultStack = Stack<String>()
        stack.push(ExpressionPrinterStackItem(node, PrinterConstants.LOCATION_1))

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            when(top.node) {
                is ParsedBinaryArrayOperatorNode -> {
                    when(top.location) {
                        PrinterConstants.LOCATION_1 -> {
                            stack.push(ExpressionPrinterStackItem(top.node, PrinterConstants.LOCATION_2))
                            stack.push(ExpressionPrinterStackItem(top.node.variableExpression, PrinterConstants.LOCATION_1))
                        }
                        PrinterConstants.LOCATION_2 -> {
                            stack.push(ExpressionPrinterStackItem(top.node, PrinterConstants.LOCATION_3))
                            stack.push(ExpressionPrinterStackItem(top.node.insideExpression, PrinterConstants.LOCATION_1))
                        }
                        PrinterConstants.LOCATION_3 -> {
                            val rightExpressionString = resultStack.pop()
                            val leftExpressionString = resultStack.pop()
                            val result = leftExpressionString +
                                    PrinterConstants.LEFT_BRACKET +
                                    rightExpressionString +
                                    PrinterConstants.RIGHT_BRACKET
                            resultStack.push(result)
                        }
                    }
                }
                is ParsedBinaryAssignNode -> {
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
                            val rightExpressionString = resultStack.pop()
                            val leftExpressionString = resultStack.pop()
                            val result = leftExpressionString +
                                    PrinterConstants.SPACE +
                                    PrinterConstants.EQUALS +
                                    PrinterConstants.SPACE +
                                    rightExpressionString
                            resultStack.push(result)
                        }
                    }
                }
                is ParsedBinaryAssignOperatorNode -> {
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
                            val rightExpressionString = resultStack.pop()
                            val leftExpressionString = resultStack.pop()
                            val result = leftExpressionString +
                                    PrinterConstants.SPACE +
                                    top.node.operator +
                                    PrinterConstants.EQUALS +
                                    PrinterConstants.SPACE +
                                    rightExpressionString
                            resultStack.push(result)
                        }
                    }
                }
                is ParsedBinaryOperatorNode -> {
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
                            val rightExpressionString = resultStack.pop()
                            val leftExpressionString = resultStack.pop()
                            val result = leftExpressionString +
                                    PrinterConstants.SPACE +
                                    top.node.operator +
                                    PrinterConstants.SPACE +
                                    rightExpressionString
                            resultStack.push(result)
                        }
                    }
                }
                is ParsedBinaryAndOperatorNode -> {
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
                            val rightExpressionString = resultStack.pop()
                            val leftExpressionString = resultStack.pop()
                            val result = leftExpressionString +
                                    PrinterConstants.SPACE +
                                    PrinterConstants.AMPERSAND +
                                    PrinterConstants.AMPERSAND +
                                    PrinterConstants.SPACE +
                                    rightExpressionString
                            resultStack.push(result)
                        }
                    }
                }
                is ParsedBinaryOrOperatorNode -> {
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
                            val rightExpressionString = resultStack.pop()
                            val leftExpressionString = resultStack.pop()
                            val result = leftExpressionString +
                                    PrinterConstants.SPACE +
                                    PrinterConstants.VERTICAL_BAR +
                                    PrinterConstants.VERTICAL_BAR +
                                    PrinterConstants.SPACE +
                                    rightExpressionString
                            resultStack.push(result)
                        }
                    }
                }
                is ParsedBinaryRelationalOperatorNode -> {
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
                            val rightExpressionString = resultStack.pop()
                            val leftExpressionString = resultStack.pop()

                            val result = leftExpressionString +
                                    PrinterConstants.SPACE +
                                    top.node.operator +
                                    PrinterConstants.SPACE +
                                    rightExpressionString
                            resultStack.push(result)
                        }
                    }
                }
                is ParsedInnerExpression -> {
                    when(top.location) {
                        PrinterConstants.LOCATION_1 -> {
                            stack.push(ExpressionPrinterStackItem(top.node, PrinterConstants.LOCATION_2))
                            stack.push(ExpressionPrinterStackItem(top.node.expression, PrinterConstants.LOCATION_1))
                        }
                        PrinterConstants.LOCATION_2 -> {
                            val expressionString = resultStack.pop()
                            val result = PrinterConstants.LEFT_PARENTHESES +
                                    expressionString +
                                    PrinterConstants.RIGHT_PARENTHESES
                            resultStack.push(result)
                        }
                    }
                }
                is ParsedUnaryOperatorNode -> {
                    when(top.location) {
                        PrinterConstants.LOCATION_1 -> {
                            stack.push(ExpressionPrinterStackItem(top.node, PrinterConstants.LOCATION_2))
                            stack.push(ExpressionPrinterStackItem(top.node.expression, PrinterConstants.LOCATION_1))
                        }
                        PrinterConstants.LOCATION_2 -> {
                            val expressionString = resultStack.pop()
                            val result = top.node.operator + expressionString
                            resultStack.push(result)
                        }
                    }
                }
                is ParsedUnaryNotOperatorNode -> {
                    when(top.location) {
                        PrinterConstants.LOCATION_1 -> {
                            stack.push(ExpressionPrinterStackItem(top.node, PrinterConstants.LOCATION_2))
                            stack.push(ExpressionPrinterStackItem(top.node.expression, PrinterConstants.LOCATION_1))
                        }
                        PrinterConstants.LOCATION_2 -> {
                            val expressionString = resultStack.pop()
                            val result = PrinterConstants.EXCLAMATION_POINT + expressionString
                            resultStack.push(result)
                        }
                    }
                }
                is ParsedUnaryPreOperatorNode -> {
                    when(top.location) {
                        PrinterConstants.LOCATION_1 -> {
                            stack.push(ExpressionPrinterStackItem(top.node, PrinterConstants.LOCATION_2))
                            stack.push(ExpressionPrinterStackItem(top.node.expression, PrinterConstants.LOCATION_1))
                        }
                        PrinterConstants.LOCATION_2 -> {
                            val expressionString = resultStack.pop()
                            val result = top.node.operator + top.node.operator + expressionString
                            resultStack.push(result)
                        }
                    }
                }
                is ParsedUnaryPostOperatorNode -> {
                    when(top.location) {
                        PrinterConstants.LOCATION_1 -> {
                            stack.push(ExpressionPrinterStackItem(top.node, PrinterConstants.LOCATION_2))
                            stack.push(ExpressionPrinterStackItem(top.node.expression, PrinterConstants.LOCATION_1))
                        }
                        PrinterConstants.LOCATION_2 -> {
                            val expressionString = resultStack.pop()
                            val result = expressionString + top.node.operator + top.node.operator
                            resultStack.push(result)
                        }
                    }
                }
                is ParsedConstantNode -> {
                    resultStack.push(top.node.value)
                }
                is ParsedVariableExpressionNode -> {
                    resultStack.push(top.node.variableValue)
                }
            }
        }
        return resultStack.pop()
    }
}