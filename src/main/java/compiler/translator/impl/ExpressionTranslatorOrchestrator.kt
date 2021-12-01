package compiler.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.*
import compiler.translator.impl.internal.IExpressionTranslatorOrchestrator
import compiler.translator.impl.internal.ITempGenerator

internal class ExpressionTranslatorOrchestrator(
    private val translatorMap: Map<Class<out IParsedExpressionNode>, IExpressionTranslator>,
    private val tempGenerator: ITempGenerator,
    private val tempDeclarationCodeGenerator: ITempDeclarationCodeGenerator,
    private val expressionTranslatorStackPusher: IExpressionTranslatorStackPusher,
    private val assignCodeGenerator: IAssignCodeGenerator,
    private val arrayCodeGenerator: IArrayCodeGenerator
): IExpressionTranslatorOrchestrator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int
    ): Pair<TranslatedExpressionNode, Int> {
        val stack = Stack<ExpressionTranslatorStackItem>()
        stack.push(ExpressionTranslatorStackItem(ExpressionTranslatorLocation.START, expressionNode))
        val resultStack = Stack<TranslatedExpressionNode>()
        var localTemp = tempCounter

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            if (translatorMap.containsKey(top.node.javaClass)) {
                val translator = translatorMap.getValue(top.node.javaClass)
                localTemp = translator.translate(
                    top.node,
                    top.location,
                    variableToTypeMap,
                    localTemp,
                    stack,
                    resultStack
                )
            }
            else {
                when (top.node) {
                    is ParsedUnaryPostOperatorNode -> {
                        if (top.node.expression is ParsedVariableExpressionNode) {
                            val (address, tc) = tempGenerator.generateTempVariable(localTemp)
                            localTemp = tc
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
                                ExpressionTranslatorLocation.START -> {
                                    expressionTranslatorStackPusher.push(
                                        ExpressionTranslatorLocation.END,
                                        top.node,
                                        top.node.expression.rightExpression,
                                        stack
                                    )
                                }
                                else -> {
                                    val insideExpression = resultStack.pop()
                                    val (address, tc) = tempGenerator.generateTempVariable(localTemp)
                                    localTemp = tc
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
                                    val operationAssignCode = assignCodeGenerator.generateAssignCode(
                                        address,
                                        operationCode
                                    )
                                    val arrayAssignCode = assignCodeGenerator.generateAssignCode(
                                        arrayCode,
                                        address
                                    )
                                    val oppositeOperationCode = address +
                                            PrinterConstants.SPACE +
                                            top.node.oppositeOperator +
                                            PrinterConstants.SPACE +
                                            PrinterConstants.ONE
                                    val oppositeOperationAssignCode = assignCodeGenerator.generateAssignCode(
                                        address,
                                        oppositeOperationCode
                                    )
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
                }
            }
        }
        return Pair(resultStack.pop(), localTemp)
    }
}