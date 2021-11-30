package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedForNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedForNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.translator.impl.internal.IBooleanExpressionTranslatorOrchestrator
import compiler.translator.impl.internal.IExpressionTranslatorOrchestrator
import compiler.translator.impl.internal.ILabelGenerator
import compiler.translator.impl.internal.IStatementTranslator

internal class ForStatementTranslator(
    private val labelGenerator: ILabelGenerator,
    private val expressionTranslatorOrchestrator: IExpressionTranslatorOrchestrator,
    private val booleanExpressionTranslatorOrchestrator: IBooleanExpressionTranslatorOrchestrator
): IStatementTranslator {
    override fun translate(
        node: IParsedStatementNode,
        location: StatementTranslatorLocation,
        tempCounter: Int,
        labelCounter: Int,
        variableToTypeMap: Map<String, String>,
        stack: Stack<StatementTranslatorStackItem>,
        resultStack: Stack<ITranslatedStatementNode>,
        expressionStack: Stack<ITranslatedExpressionNode>,
        labelStack: Stack<String>
    ): Pair<Int, Int> {
        node as ParsedForNode
        when (location) {
            StatementTranslatorLocation.START -> {
                val (falseLabel, labelCountAfterFalse) = labelGenerator.generateLabel(labelCounter)
                val (beginLabel, labelCountAfterBegin) = labelGenerator.generateLabel(labelCountAfterFalse)
                val (trueLabel, labelCountAfterTrue) = labelGenerator.generateLabel(labelCountAfterBegin)
                val (initExpression, tempAfterInit) = expressionTranslatorOrchestrator.translate(
                    node.initExpression,
                    variableToTypeMap,
                    tempCounter
                )
                val (testExpression, labelAfterTest, tempAfterTest) = booleanExpressionTranslatorOrchestrator.translate(
                    node.testExpression,
                    trueLabel,
                    falseLabel,
                    labelCountAfterTrue,
                    tempAfterInit,
                    variableToTypeMap
                )
                val (incrementExpression, tempAfterIncrement) = expressionTranslatorOrchestrator.translate(
                    node.incrementExpression,
                    variableToTypeMap,
                    tempAfterTest
                )
                labelStack.push(trueLabel)
                labelStack.push(beginLabel)
                labelStack.push(falseLabel)
                expressionStack.push(incrementExpression)
                expressionStack.push(testExpression)
                expressionStack.push(initExpression)
                stack.push(StatementTranslatorStackItem(StatementTranslatorLocation.END, node))
                stack.push(StatementTranslatorStackItem(StatementTranslatorLocation.START, node.body))
                return Pair(tempAfterIncrement, labelAfterTest)
            }
            else -> {
                val falseLabel = labelStack.pop()
                val beginLabel = labelStack.pop()
                val trueLabel = labelStack.pop()
                val initExpression = expressionStack.pop()
                val testExpression = expressionStack.pop()
                val incrementExpression = expressionStack.pop()
                val body = resultStack.pop()

                val forNode = TranslatedForNode(
                    initExpression,
                    testExpression,
                    incrementExpression,
                    body,
                    falseLabel,
                    beginLabel,
                    trueLabel
                )
                resultStack.push(forNode)
                return Pair(tempCounter, labelCounter)
            }
        }
    }
}