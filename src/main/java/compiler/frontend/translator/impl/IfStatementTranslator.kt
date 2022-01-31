package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedIfNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedIfNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.frontend.translator.impl.internal.IBooleanExpressionTranslatorOrchestrator
import compiler.frontend.translator.impl.internal.ILabelGenerator
import compiler.frontend.translator.impl.internal.IStatementTranslator

internal class IfStatementTranslator(
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
        node as ParsedIfNode
        when(location) {
            StatementTranslatorLocation.START -> {
                val (nextLabel, labelCountAfterNext) = labelGenerator.generateLabel(labelCounter)
                val (trueLabel, labelCountAfterTrue) = labelGenerator.generateLabel(labelCountAfterNext)
                stack.push(StatementTranslatorStackItem(StatementTranslatorLocation.END, node))
                val (expression, labelAfterExpression, tempAfterExpression) = if (node.elseBody == null) {
                    booleanExpressionTranslatorOrchestrator.translate(
                        node.booleanExpression,
                        trueLabel,
                        nextLabel,
                        labelCountAfterTrue,
                        tempCounter,
                        variableToTypeMap
                    )
                } else {
                    val (falseLabel, labelCountAfterFalse) = labelGenerator.generateLabel(labelCountAfterTrue)
                    labelStack.push(falseLabel)
                    stack.push(StatementTranslatorStackItem(StatementTranslatorLocation.START, node.elseBody))
                    booleanExpressionTranslatorOrchestrator.translate(
                        node.booleanExpression,
                        trueLabel,
                        falseLabel,
                        labelCountAfterFalse,
                        tempCounter,
                        variableToTypeMap
                    )
                }
                labelStack.push(trueLabel)
                labelStack.push(nextLabel)
                expressionStack.push(expression)
                stack.push(StatementTranslatorStackItem(StatementTranslatorLocation.START, node.ifBody))
                return Pair(tempAfterExpression, labelAfterExpression)
            }
            else -> {
                val nextLabel = labelStack.pop()
                val trueLabel = labelStack.pop()
                val expression = expressionStack.pop()
                val body = resultStack.pop()
                val ifNode = if (node.elseBody == null) {
                    TranslatedIfNode(
                        expression,
                        body,
                        null,
                        nextLabel,
                        trueLabel,
                        nextLabel
                    )
                } else {
                    val falseLabel = labelStack.pop()
                    val ifBody = resultStack.pop()
                    TranslatedIfNode(
                        expression,
                        ifBody,
                        body,
                        nextLabel,
                        trueLabel,
                        falseLabel
                    )
                }
                resultStack.push(ifNode)
                return Pair(tempCounter, labelCounter)
            }
        }
    }
}