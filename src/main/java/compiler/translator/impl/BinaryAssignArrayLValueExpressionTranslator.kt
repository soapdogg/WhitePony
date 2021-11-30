package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IArrayCodeGenerator
import compiler.translator.impl.internal.IAssignCodeGenerator
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IExpressionTranslatorStackPusher

internal class BinaryAssignArrayLValueExpressionTranslator(
    private val expressionTranslatorStackPusher: IExpressionTranslatorStackPusher,
    private val arrayCodeGenerator: IArrayCodeGenerator,
    private val assignCodeGenerator: IAssignCodeGenerator
): IExpressionTranslator {
    override fun translate(
        node: IParsedExpressionNode,
        location: ExpressionTranslatorLocation,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int,
        stack: Stack<ExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedExpressionNode>
    ): Int {
        node as ParsedBinaryAssignExpressionNode
        val lValueNode = node.leftExpression as ParsedBinaryArrayExpressionNode
        when (location) {
            ExpressionTranslatorLocation.START -> {
                expressionTranslatorStackPusher.push(
                    ExpressionTranslatorLocation.MIDDLE,
                    node,
                    lValueNode.rightExpression,
                    stack
                )
            }
            ExpressionTranslatorLocation.MIDDLE -> {
                expressionTranslatorStackPusher.push(
                    ExpressionTranslatorLocation.END,
                    node,
                    node.rightExpression,
                    stack
                )
            }
            else -> {
                val rightExpression = resultStack.pop()
                val insideArrayExpression = resultStack.pop()
                val lValueVariable = lValueNode.leftExpression.value
                val type = variableToTypeMap.getValue(lValueVariable)
                val arrayCode = arrayCodeGenerator.generateArrayCode(lValueVariable, insideArrayExpression.address)
                val rightExpressionAddress = rightExpression.address
                val assignCode = assignCodeGenerator.generateAssignCode(
                    arrayCode,
                    rightExpressionAddress
                )
                val code = insideArrayExpression.code +
                        rightExpression.code +
                        listOf(assignCode)
                val translatedExpressionNode = TranslatedExpressionNode(
                    rightExpressionAddress,
                    code,
                    type,
                )
                resultStack.push(translatedExpressionNode)
            }
        }
        return tempCounter
    }
}