package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedDoWhileNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedDoWhileNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.frontend.translator.impl.internal.IBooleanExpressionTranslatorOrchestrator
import compiler.frontend.translator.impl.internal.ILabelGenerator
import compiler.frontend.translator.impl.internal.IStatementTranslator

internal class DoWhileStatementTranslator(
    private val labelGenerator: ILabelGenerator,
    private val booleanExpressionTranslatorOrchestrator: IBooleanExpressionTranslatorOrchestrator
): IStatementTranslator {
    override fun translate(
        node: IParsedStatementNode,
        location: StatementTranslatorLocation,
        tempCounter: Int,
        labelCounter: Int,
        variableToTypeMap: MutableMap<String, String>,
        stack: Stack<StatementTranslatorStackItem>,
        resultStack: Stack<ITranslatedStatementNode>,
        expressionStack: Stack<ITranslatedExpressionNode>,
        labelStack: Stack<String>
    ): Pair<Int, Int> {
        node as ParsedDoWhileNode
        when(location) {
            StatementTranslatorLocation.START -> {
                val (falseLabel, labelCountAfterFalse) = labelGenerator.generateLabel(labelCounter)
                val (trueLabel, labelCountAfterTrue) = labelGenerator.generateLabel(labelCountAfterFalse)
                labelStack.push(trueLabel)
                labelStack.push(falseLabel)

                stack.push(StatementTranslatorStackItem(StatementTranslatorLocation.END, node))
                stack.push(StatementTranslatorStackItem(StatementTranslatorLocation.START, node.body))
                return Pair(tempCounter, labelCountAfterTrue)
            } else -> {
                val falseLabel = labelStack.pop()
                val trueLabel = labelStack.pop()
                val (expression, labelCountAfterExpression, tempCountAfterExpression) = booleanExpressionTranslatorOrchestrator.translate(
                    node.expression,
                    trueLabel,
                    falseLabel,
                    labelCounter,
                    tempCounter,
                    variableToTypeMap
                )
                val body = resultStack.pop()
                val doWhile = TranslatedDoWhileNode(
                    expression,
                    body,
                    falseLabel,
                    trueLabel
                )
                resultStack.push(doWhile)
                return Pair(tempCountAfterExpression, labelCountAfterExpression)
            }
        }
    }
}