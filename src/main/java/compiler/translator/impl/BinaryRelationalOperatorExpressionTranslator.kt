package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedBinaryRelationalOperatorExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IBinaryRelationalOperatorExpressionTranslator
import compiler.translator.impl.internal.IConditionalGotoCodeGenerator
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IGotoCodeGenerator

internal class BinaryRelationalOperatorExpressionTranslator(
    private val expressionTranslator: IExpressionTranslator,
    private val conditionalGotoCodeGenerator: IConditionalGotoCodeGenerator,
    private val gotoCodeGenerator: IGotoCodeGenerator
): IBinaryRelationalOperatorExpressionTranslator {
    override fun translate(
        node: ParsedBinaryRelationalOperatorExpressionNode,
        trueLabel: String,
        falseLabel: String,
        tempCounter: Int,
        variableToTypeMap: Map<String,String>,
        stack: Stack<BooleanExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedBooleanExpressionNode>
    ): Int {
        val (leftExpression, tempAfterLeft) = expressionTranslator.translate(node.leftExpression, variableToTypeMap, tempCounter)
        val (rightExpression, tempAfterRight) = expressionTranslator.translate(node.rightExpression, variableToTypeMap, tempAfterLeft)

        val conditionalGotoCode = conditionalGotoCodeGenerator.generateConditionalGotoCode(
            leftExpression.address,
            node.operator,
            rightExpression.address,
            trueLabel
        )
        val gotoFalseLabelCode = gotoCodeGenerator.generateGotoCode(falseLabel)
        val code = leftExpression.code + rightExpression.code + listOf(conditionalGotoCode, gotoFalseLabelCode)
        val translatedBooleanExpressionNode = TranslatedBooleanExpressionNode(code)
        resultStack.push(translatedBooleanExpressionNode)
        return tempAfterRight
    }
}