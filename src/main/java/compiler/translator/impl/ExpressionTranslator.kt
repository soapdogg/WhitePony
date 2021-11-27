package compiler.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.*
import compiler.translator.impl.internal.IConstantExpressionTranslator
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IInnerExpressionTranslator
import compiler.translator.impl.internal.ITempGenerator
import compiler.translator.impl.internal.ITypeDeterminer
import compiler.translator.impl.internal.IVariableExpressionTranslator

internal class ExpressionTranslator(
    private val binaryAssignExpressionTranslator: IBinaryAssignExpressionTranslator,
    private val binaryOperatorExpressionTranslator: IBinaryOperatorExpressionTranslator,
    private val binaryArrayExpressionTranslator: IBinaryArrayExpressionTranslator,
    private val unaryExpressionTranslator: IUnaryExpressionTranslator,
    private val innerExpressionTranslator: IInnerExpressionTranslator,
    private val variableExpressionTranslator: IVariableExpressionTranslator,
    private val constantExpressionTranslator: IConstantExpressionTranslator,
    private val tempGenerator: ITempGenerator,
    private val tempDeclarationCodeGenerator: ITempDeclarationCodeGenerator,
    private val expressionTranslatorStackPusher: IExpressionTranslatorStackPusher,
    private val assignCodeGenerator: IAssignCodeGenerator,
    private val arrayCodeGenerator: IArrayCodeGenerator
): IExpressionTranslator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int
    ): Pair<TranslatedExpressionNode, Int> {
        val stack = Stack<ExpressionTranslatorStackItem>()
        stack.push(ExpressionTranslatorStackItem(LocationConstants.LOCATION_1, expressionNode))
        val resultStack = Stack<TranslatedExpressionNode>()
        var t = tempCounter

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            when (top.node) {
                is ParsedBinaryAssignExpressionNode -> {
                    binaryAssignExpressionTranslator.translate(
                        top.node,
                        top.location,
                        variableToTypeMap,
                        stack,
                        resultStack
                    )
                }
                is ParsedBinaryAssignOperatorNode -> {
                    if (top.node.leftExpression is ParsedVariableExpressionNode) {
                        when (top.location) {
                            LocationConstants.LOCATION_1 -> {
                                expressionTranslatorStackPusher.push(
                                    LocationConstants.LOCATION_2,
                                    top.node,
                                    top.node.rightExpression,
                                    stack
                                )
                            }
                            LocationConstants.LOCATION_2 -> {
                                val rightExpression = resultStack.pop()
                                val (address, tc) = tempGenerator.generateTempVariable(t)
                                t = tc
                                val type = variableToTypeMap.getValue(top.node.leftExpression.value)
                                val tempDeclarationRValue = top.node.leftExpression.value +
                                        PrinterConstants.SPACE +
                                        top.node.operator +
                                        PrinterConstants.SPACE +
                                        rightExpression.address
                                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                                    type,
                                    address,
                                    tempDeclarationRValue
                                )
                                val assignCode = assignCodeGenerator.generateAssignCode(
                                    top.node.leftExpression.value,
                                    address
                                )
                                val code = rightExpression.code + listOf(tempDeclarationCode, assignCode)
                                val translatedExpressionNode = TranslatedExpressionNode(
                                    address,
                                    code,
                                    type
                                )
                                resultStack.push(translatedExpressionNode)
                            }
                        }
                    } else if (top.node.leftExpression is ParsedBinaryArrayExpressionNode){
                        when (top.location) {
                            LocationConstants.LOCATION_1 -> {
                                expressionTranslatorStackPusher.push(
                                    LocationConstants.LOCATION_2,
                                    top.node,
                                    top.node.leftExpression.rightExpression,
                                    stack
                                )
                            }
                            LocationConstants.LOCATION_2 -> {
                                expressionTranslatorStackPusher.push(
                                    LocationConstants.LOCATION_3,
                                    top.node,
                                    top.node.rightExpression,
                                    stack
                                )
                            }
                            LocationConstants.LOCATION_3 -> {
                                val rightExpression = resultStack.pop()
                                val insideArrayExpression = resultStack.pop()
                                val (address, tc) = tempGenerator.generateTempVariable(t)
                                t = tc
                                val type = variableToTypeMap.getValue(top.node.leftExpression.leftExpression.value)
                                val arrayCode = arrayCodeGenerator.generateArrayCode(top.node.leftExpression.leftExpression.value, insideArrayExpression.address)

                                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                                    type,
                                    address,
                                    arrayCode
                                )

                                val operationCode = address +
                                        PrinterConstants.SPACE +
                                        top.node.operator +
                                        PrinterConstants.SPACE +
                                        rightExpression.address
                                val addressAssignCode = assignCodeGenerator.generateAssignCode(address, operationCode)
                                val arrayAssignCode = assignCodeGenerator.generateAssignCode(arrayCode, address)
                                val code = insideArrayExpression.code + listOf(tempDeclarationCode) + rightExpression.code + listOf(addressAssignCode, arrayAssignCode)

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
                is ParsedBinaryOperatorExpressionNode -> {
                    t = binaryOperatorExpressionTranslator.translate(
                        top.node,
                        top.location,
                        t,
                        variableToTypeMap,
                        stack,
                        resultStack
                    )
                }
                is ParsedBinaryArrayExpressionNode -> {
                    t = binaryArrayExpressionTranslator.translate(
                        top.node,
                        top.location,
                        t,
                        variableToTypeMap,
                        stack,
                        resultStack
                    )
                }
                is ParsedUnaryExpressionNode -> {
                    t = unaryExpressionTranslator.translate(
                        top.node,
                        top.location,
                        t,
                        stack,
                        resultStack
                    )
                }
                is ParsedUnaryPreOperatorNode -> {
                    if (top.node.expression is ParsedVariableExpressionNode) {
                        val operationCode = top.node.expression.value +
                                PrinterConstants.SPACE +
                                top.node.operator +
                                PrinterConstants.SPACE +
                                PrinterConstants.ONE
                        val assignCode = assignCodeGenerator.generateAssignCode(
                            top.node.expression.value,
                            operationCode
                        )
                        val code = listOf(assignCode)
                        val type = variableToTypeMap.getValue(top.node.expression.value)
                        val translatedExpressionNode = TranslatedExpressionNode(
                            top.node.expression.value,
                            code,
                            type
                        )
                        resultStack.push(translatedExpressionNode)
                    } else if (top.node.expression is ParsedBinaryArrayExpressionNode) {
                        when (top.location) {
                            LocationConstants.LOCATION_1 -> {
                                expressionTranslatorStackPusher.push(
                                    LocationConstants.LOCATION_2,
                                    top.node,
                                    top.node.expression.rightExpression,
                                    stack
                                )
                            }
                            LocationConstants.LOCATION_2 -> {
                                val insideExpression = resultStack.pop()
                                val (address, tc) = tempGenerator.generateTempVariable(t)
                                t = tc
                                val type = variableToTypeMap.getValue(top.node.expression.leftExpression.value)
                                val arrayCode = arrayCodeGenerator.generateArrayCode(
                                    top.node.expression.leftExpression.value,
                                    insideExpression.address
                                )
                                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                                    type,
                                    address,
                                    arrayCode
                                )

                                val operationCode = address +
                                        PrinterConstants.SPACE +
                                        top.node.operator +
                                        PrinterConstants.SPACE +
                                        PrinterConstants.ONE
                                val operationAssignCode = assignCodeGenerator.generateAssignCode(address, operationCode)

                                val arrayAssignCode = assignCodeGenerator.generateAssignCode(arrayCode, address)
                                val code = insideExpression.code +
                                        listOf(
                                            tempDeclarationCode,
                                            operationAssignCode,
                                            arrayAssignCode
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
                        val operationCode = top.node.expression.value +
                                PrinterConstants.SPACE +
                                top.node.operator +
                                PrinterConstants.SPACE +
                                PrinterConstants.ONE
                        val assignCode = assignCodeGenerator.generateAssignCode(
                            top.node.expression.value,
                            operationCode
                        )
                        val code = listOf(
                            tempDeclarationCode,
                            assignCode
                        )
                        val translatedExpressionNode = TranslatedExpressionNode(
                            address,
                            code,
                            type,
                        )
                        resultStack.push(translatedExpressionNode)
                    } else if (top.node.expression is ParsedBinaryArrayExpressionNode) {
                        when (top.location) {
                            LocationConstants.LOCATION_1 -> {
                                expressionTranslatorStackPusher.push(
                                    LocationConstants.LOCATION_2,
                                    top.node,
                                    top.node.expression.rightExpression,
                                    stack
                                )
                            }
                            LocationConstants.LOCATION_2 -> {
                                val insideExpression = resultStack.pop()
                                val (address, tc) = tempGenerator.generateTempVariable(t)
                                t = tc
                                val type = variableToTypeMap.getValue(top.node.expression.leftExpression.value)
                                val arrayCode = arrayCodeGenerator.generateArrayCode(
                                    top.node.expression.leftExpression.value,
                                    insideExpression.address
                                )
                                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                                    type,
                                    address,
                                    arrayCode
                                )
                                val operationCode =  address +
                                        PrinterConstants.SPACE +
                                        top.node.operator +
                                        PrinterConstants.SPACE +
                                        PrinterConstants.ONE
                                val operationAssignCode = assignCodeGenerator.generateAssignCode(address, operationCode)
                                val arrayAssignCode = assignCodeGenerator.generateAssignCode(arrayCode, address)
                                val oppositeOperationCode = address +
                                        PrinterConstants.SPACE +
                                        top.node.oppositeOperator +
                                        PrinterConstants.SPACE +
                                        PrinterConstants.ONE
                                val oppositeOperationAssignCode = assignCodeGenerator.generateAssignCode(address, oppositeOperationCode)
                                val code = insideExpression.code +
                                        listOf(
                                            tempDeclarationCode,
                                            operationAssignCode,
                                            arrayAssignCode,
                                            oppositeOperationAssignCode
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