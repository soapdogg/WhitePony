package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedWhileNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedWhileNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.translator.impl.internal.IBooleanExpressionTranslatorOrchestrator
import compiler.translator.impl.internal.ILabelGenerator
import compiler.translator.impl.internal.IStatementTranslator

internal class WhileStatementTranslator(
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
        node as ParsedWhileNode
        when(location) {
            StatementTranslatorLocation.START -> {
                val (falseLabel, labelCountAfterFalse) = labelGenerator.generateLabel(labelCounter)
                val (beginLabel, labelCountAfterBegin) = labelGenerator.generateLabel(labelCountAfterFalse)
                val (trueLabel, labelCountAfterTrue) = labelGenerator.generateLabel(labelCountAfterBegin)
                val (expression, labelAfterExpression, tempAfterExpression) = booleanExpressionTranslatorOrchestrator.translate(
                    node.expression,
                    trueLabel,
                    falseLabel,
                    labelCountAfterTrue,
                    tempCounter,
                    variableToTypeMap
                )
                labelStack.push(trueLabel)
                labelStack.push(beginLabel)
                labelStack.push(falseLabel)
                expressionStack.push(expression)
                stack.push(StatementTranslatorStackItem(StatementTranslatorLocation.END, node))
                stack.push(StatementTranslatorStackItem(StatementTranslatorLocation.START, node.body))
                return Pair(tempAfterExpression, labelAfterExpression)
            }
            else -> {
                val falseLabel = labelStack.pop()
                val beginLabel = labelStack.pop()
                val trueLabel = labelStack.pop()
                val expression = expressionStack.pop()
                val body = resultStack.pop()

                val whileNode = TranslatedWhileNode(
                    expression,
                    body,
                    falseLabel,
                    beginLabel,
                    trueLabel
                )
                resultStack.push(whileNode)
                return Pair(tempCounter, labelCounter)
            }
        }
    }
}