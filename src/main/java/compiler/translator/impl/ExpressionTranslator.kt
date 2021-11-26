package compiler.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.core.constants.TokenizerConstants
import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.*
import compiler.translator.impl.internal.IConstantExpressionTranslator
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IInnerExpressionTranslator
import compiler.translator.impl.internal.ITempGenerator
import compiler.translator.impl.internal.ITypeDeterminer
import compiler.translator.impl.internal.IVariableExpressionTranslator

internal class ExpressionTranslator(
    private val innerExpressionTranslator: IInnerExpressionTranslator,
    private val variableExpressionTranslator: IVariableExpressionTranslator,
    private val constantExpressionTranslator: IConstantExpressionTranslator,
    private val tempGenerator: ITempGenerator,
    private val typeDeterminer: ITypeDeterminer,
    private val tempDeclarationCodeGenerator: ITempDeclarationCodeGenerator
): IExpressionTranslator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int
    ): Pair<TranslatedExpressionNode, Int> {
        val stack = Stack<ExpressionTranslatorStackItem>()
        stack.push(ExpressionTranslatorStackItem(1, expressionNode))
        val resultStack = Stack<TranslatedExpressionNode>()
        var t = tempCounter

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            when (top.node) {
                is ParsedBinaryAssignNode -> {
                    if (top.node.leftExpression is ParsedVariableExpressionNode) {
                        when (top.location) {
                            1 -> {
                                stack.push(ExpressionTranslatorStackItem(2, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.rightExpression))
                            }
                            2 -> {
                                val rightExpression = resultStack.pop()
                                val address = rightExpression.address
                                val type = variableToTypeMap.getValue(top.node.leftExpression.value)
                                val code = rightExpression.code +
                                        listOf(
                                            top.node.leftExpression.value +
                                            PrinterConstants.SPACE +
                                            PrinterConstants.EQUALS +
                                            PrinterConstants.SPACE +
                                            rightExpression.address
                                        )
                                val translatedBinaryOperatorNode = TranslatedExpressionNode(
                                    address,
                                    code,
                                    type
                                )
                                resultStack.push(translatedBinaryOperatorNode)
                            }
                        }
                    }
                    else if(top.node.leftExpression is ParsedBinaryArrayOperatorNode) {
                        when (top.location) {
                            1 -> {
                                stack.push(ExpressionTranslatorStackItem(2, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.leftExpression.rightExpression))
                            }
                            2 -> {
                                stack.push(ExpressionTranslatorStackItem(3, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.rightExpression))
                            }
                            3 -> {
                                val rightExpression = resultStack.pop()
                                val insideArrayExpression = resultStack.pop()
                                val type = variableToTypeMap.getValue(top.node.leftExpression.leftExpression.value)
                                val code = insideArrayExpression.code +
                                        rightExpression.code +
                                        listOf(top.node.leftExpression.leftExpression.value +
                                                PrinterConstants.LEFT_BRACKET +
                                                insideArrayExpression.address +
                                                PrinterConstants.RIGHT_BRACKET +
                                                PrinterConstants.SPACE +
                                                PrinterConstants.EQUALS +
                                                PrinterConstants.SPACE +
                                                rightExpression.address
                                        )
                                val translatedExpressionNode = TranslatedExpressionNode(
                                    rightExpression.address,
                                    code,
                                    type,
                                )
                                resultStack.push(translatedExpressionNode)
                            }
                        }
                    }
                }
                is ParsedBinaryAssignOperatorNode -> {
                    if (top.node.leftExpression is ParsedVariableExpressionNode) {
                        when (top.location) {
                            1 -> {
                                stack.push(ExpressionTranslatorStackItem(2, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.rightExpression))
                            }
                            2 -> {
                                val rightExpression = resultStack.pop()
                                val (address, tc) = tempGenerator.generateTempVariable(t)
                                t = tc
                                val type = variableToTypeMap.getValue(top.node.leftExpression.value)
                                val rValue = top.node.leftExpression.value +
                                        PrinterConstants.SPACE +
                                        top.node.operator +
                                        PrinterConstants.SPACE +
                                        rightExpression.address
                                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                                    type,
                                    address,
                                    rValue
                                )
                                val code = rightExpression.code +
                                        listOf(
                                            tempDeclarationCode,
                                            top.node.leftExpression.value +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address
                                        )
                                val translatedExpressionNode = TranslatedExpressionNode(
                                    address,
                                    code,
                                    type
                                )
                                resultStack.push(translatedExpressionNode)
                            }
                        }
                    } else if (top.node.leftExpression is ParsedBinaryArrayOperatorNode){
                        when (top.location) {
                            1 -> {
                                stack.push(ExpressionTranslatorStackItem(2, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.leftExpression.rightExpression))
                            }
                            2 -> {
                                stack.push(ExpressionTranslatorStackItem(3, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.rightExpression))
                            }
                            3 -> {
                                val rightExpression = resultStack.pop()
                                val insideArrayExpression = resultStack.pop()
                                val (address, tc) = tempGenerator.generateTempVariable(t)
                                t = tc
                                val type = variableToTypeMap.getValue(top.node.leftExpression.leftExpression.value)
                                val rValue = top.node.leftExpression.leftExpression.value +
                                        PrinterConstants.LEFT_BRACKET +
                                        insideArrayExpression.address +
                                        PrinterConstants.RIGHT_BRACKET
                                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                                    type,
                                    address,
                                    rValue
                                )
                                val code = insideArrayExpression.code +
                                        listOf(tempDeclarationCode) +
                                    rightExpression.code +
                                        listOf(address +
                                            PrinterConstants.SPACE +
                                                PrinterConstants.EQUALS +
                                                PrinterConstants.SPACE +
                                                address +
                                                PrinterConstants.SPACE +
                                                top.node.operator +
                                                PrinterConstants.SPACE +
                                                rightExpression.address,
                                            top.node.leftExpression.leftExpression.value +
                                                    PrinterConstants.LEFT_BRACKET +
                                                    insideArrayExpression.address +
                                                    PrinterConstants.RIGHT_BRACKET +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address
                                        )

                                val translatedExpressionNode = TranslatedExpressionNode(
                                    address,
                                    code,
                                    type
                                )
                                resultStack.push(translatedExpressionNode)
                            }
                        }
                    }
                }
                is ParsedBinaryOperatorNode -> {
                    when (top.location) {
                        1 -> {
                            stack.push(ExpressionTranslatorStackItem(2, top.node))
                            stack.push(ExpressionTranslatorStackItem(1, top.node.leftExpression))
                        }
                        2 -> {
                            stack.push(ExpressionTranslatorStackItem(3, top.node))
                            stack.push(ExpressionTranslatorStackItem(1, top.node.rightExpression))
                        }
                        3 -> {
                            val rightExpression = resultStack.pop()
                            val leftExpression = resultStack.pop()
                            val (address, tc) = tempGenerator.generateTempVariable(t)
                            t = tc
                            val type = typeDeterminer.determineType(leftExpression.type, rightExpression.type)
                            val rValue = leftExpression.address +
                                    PrinterConstants.SPACE +
                                    top.node.operator +
                                    PrinterConstants.SPACE +
                                    rightExpression.address
                            val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                                type,
                                address,
                                rValue
                            )

                            val code = leftExpression.code +
                                rightExpression.code +
                                listOf(tempDeclarationCode)

                            val translatedBinaryOperatorNode = TranslatedExpressionNode(
                                address,
                                code,
                                type
                            )
                            resultStack.push(translatedBinaryOperatorNode)
                        }
                    }
                }
                is ParsedBinaryArrayOperatorNode -> {
                    when(top.location) {
                        1 -> {
                            stack.push(ExpressionTranslatorStackItem(2, top.node))
                            stack.push(ExpressionTranslatorStackItem(1, top.node.rightExpression))
                        }
                        2 -> {
                            val rightExpression = resultStack.pop()
                            val (address, tc) = tempGenerator.generateTempVariable(t)
                            t = tc
                            val type = variableToTypeMap.getValue(top.node.leftExpression.value)
                            val rValue = top.node.leftExpression.value +
                                    PrinterConstants.LEFT_BRACKET +
                                    rightExpression.address +
                                    PrinterConstants.RIGHT_BRACKET
                            val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                                type,
                                address,
                                rValue
                            )
                            val code = rightExpression.code +
                                    listOf(tempDeclarationCode)
                            val translatedExpressionNode = TranslatedExpressionNode(
                                address,
                                code,
                                type
                            )
                            resultStack.push(translatedExpressionNode)
                        }
                    }
                }
                is ParsedUnaryOperatorNode -> {
                    when (top.location) {
                        1 -> {
                            stack.push(ExpressionTranslatorStackItem(2, top.node))
                            stack.push(ExpressionTranslatorStackItem(1, top.node.expression))
                        }
                        2 -> {
                            val expression = resultStack.pop()
                            if (top.node.operator == TokenizerConstants.PLUS_OPERATOR) {
                                resultStack.push(expression)
                            } else {
                                val (address, tc) = tempGenerator.generateTempVariable(t)
                                t = tc
                                val rValue = top.node.operator +
                                        expression.address
                                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                                    expression.type,
                                    address,
                                    rValue
                                )
                                val code = expression.code +
                                        listOf(tempDeclarationCode)
                                val translatedExpressionNode = TranslatedExpressionNode(
                                    address,
                                    code,
                                    expression.type
                                )
                                resultStack.push(translatedExpressionNode)
                            }
                        }
                    }
                }
                is ParsedUnaryPreOperatorNode -> {
                    if (top.node.expression is ParsedVariableExpressionNode) {
                        val code = listOf(
                            top.node.expression.value +
                                    PrinterConstants.SPACE +
                                    PrinterConstants.EQUALS +
                                    PrinterConstants.SPACE +
                                    top.node.expression.value +
                                    PrinterConstants.SPACE +
                                    top.node.operator +
                                    PrinterConstants.SPACE +
                                    PrinterConstants.ONE
                        )
                        val type = variableToTypeMap.getValue(top.node.expression.value)
                        val translatedExpressionNode = TranslatedExpressionNode(
                            top.node.expression.value,
                            code,
                            type
                        )
                        resultStack.push(translatedExpressionNode)
                    } else if (top.node.expression is ParsedBinaryArrayOperatorNode) {
                        when (top.location) {
                            1 -> {
                                stack.push(ExpressionTranslatorStackItem(2, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.expression.rightExpression))
                            }
                            2 -> {
                                val insideExpression = resultStack.pop()
                                val (address, tc) = tempGenerator.generateTempVariable(t)
                                t = tc
                                val type = variableToTypeMap.getValue(top.node.expression.leftExpression.value)
                                val rValue = top.node.expression.leftExpression.value +
                                        PrinterConstants.LEFT_BRACKET +
                                        insideExpression.address +
                                        PrinterConstants.RIGHT_BRACKET
                                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                                    type,
                                    address,
                                    rValue
                                )
                                val code = insideExpression.code +
                                        listOf(
                                            tempDeclarationCode,
                                            address +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address +
                                                    PrinterConstants.SPACE +
                                                    top.node.operator +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.ONE,
                                            top.node.expression.leftExpression.value +
                                                    PrinterConstants.LEFT_BRACKET +
                                                    insideExpression.address +
                                                    PrinterConstants.RIGHT_BRACKET +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address
                                        )
                                val translatedExpressionNode = TranslatedExpressionNode(
                                    address,
                                    code,
                                    type
                                )
                                resultStack.push(translatedExpressionNode)
                            }
                        }
                    }
                }
                is ParsedUnaryPostOperatorNode -> {
                    if (top.node.expression is ParsedVariableExpressionNode) {
                        val (address, tc) = tempGenerator.generateTempVariable(t)
                        t = tc
                        val type = variableToTypeMap.getValue(top.node.expression.value)
                        val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                            type,
                            address,
                            top.node.expression.value
                        )
                        val code = listOf(
                            tempDeclarationCode,
                            top.node.expression.value +
                                    PrinterConstants.SPACE +
                                    PrinterConstants.EQUALS +
                                    PrinterConstants.SPACE +
                                    top.node.expression.value +
                                    PrinterConstants.SPACE +
                                    top.node.operator +
                                    PrinterConstants.SPACE +
                                    PrinterConstants.ONE
                        )
                        val translatedExpressionNode = TranslatedExpressionNode(
                            address,
                            code,
                            type,
                        )
                        resultStack.push(translatedExpressionNode)
                    } else if (top.node.expression is ParsedBinaryArrayOperatorNode) {
                        when (top.location) {
                            1 -> {
                                stack.push(ExpressionTranslatorStackItem(2, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.expression.rightExpression))
                            }
                            2 -> {
                                val insideExpression = resultStack.pop()
                                val (address, tc) = tempGenerator.generateTempVariable(t)
                                t = tc
                                val type = variableToTypeMap.getValue(top.node.expression.leftExpression.value)
                                val rValue = top.node.expression.leftExpression.value +
                                        PrinterConstants.LEFT_BRACKET +
                                        insideExpression.address +
                                        PrinterConstants.RIGHT_BRACKET
                                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                                    type,
                                    address,
                                    rValue
                                )
                                val code = insideExpression.code +
                                        listOf(
                                            tempDeclarationCode,
                                            address +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address +
                                                    PrinterConstants.SPACE +
                                                    top.node.operator +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.ONE,
                                            top.node.expression.leftExpression.value +
                                                    PrinterConstants.LEFT_BRACKET +
                                                    insideExpression.address +
                                                    PrinterConstants.RIGHT_BRACKET +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address,
                                            address +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address +
                                                    PrinterConstants.SPACE +
                                                    top.node.oppositeOperator +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.ONE
                                        )
                                val translatedExpressionNode = TranslatedExpressionNode(
                                    address,
                                    code,
                                    type,
                                )
                                resultStack.push(translatedExpressionNode)
                            }
                        }
                    }
                }
                is ParsedInnerExpressionNode -> {
                    innerExpressionTranslator.translate(top.node, stack)
                }
                is ParsedVariableExpressionNode -> {
                    t = variableExpressionTranslator.translate(
                        top.node,
                        variableToTypeMap,
                        t,
                        resultStack
                    )
                }
                is ParsedConstantExpressionNode -> {
                    constantExpressionTranslator.translate(top.node, resultStack)
                }
            }
        }
        return Pair(resultStack.pop(), t)
    }
}