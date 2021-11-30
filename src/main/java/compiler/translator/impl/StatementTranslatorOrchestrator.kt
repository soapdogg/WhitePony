package compiler.translator.impl

import compiler.core.nodes.*
import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.*
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.translator.impl.internal.*
import compiler.translator.impl.internal.IExpressionStatementTranslator
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IReturnStatementTranslator
import compiler.translator.impl.internal.IStatementTranslatorOrchestrator

internal class StatementTranslatorOrchestrator (
    private val stackGenerator: IStackGenerator,
    private val translatorMap: Map<Class<out IParsedStatementNode>, IStatementTranslator>,
    private val labelGenerator: ILabelGenerator,
    private val variableTypeRecorder: IVariableTypeRecorder,
    private val expressionTranslator: IExpressionTranslator,
    private val booleanExpressionTranslator: IBooleanExpressionTranslatorOrchestrator,
    private val returnStatementTranslator: IReturnStatementTranslator,
    private val expressionStatementTranslator: IExpressionStatementTranslator,
): IStatementTranslatorOrchestrator {
    override fun translate(
        statementNode: IParsedStatementNode,
        variableToTypeMap: MutableMap<String, String>
    ): TranslatedBasicBlockNode {

        val stack = stackGenerator.generateNewStack(StatementTranslatorStackItem::class.java)
        stack.push(StatementTranslatorStackItem(StatementTranslatorLocation.START, statementNode))
        val resultStack = stackGenerator.generateNewStack(ITranslatedStatementNode::class.java)
        val expressionStack = stackGenerator.generateNewStack(ITranslatedExpressionNode::class.java)
        val labelStack = stackGenerator.generateNewStack(String::class.java)

        var tempCounter = 0
        var labelCounter = 0

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            if (translatorMap.containsKey(top.node.javaClass)) {
                val translator = translatorMap.getValue(top.node.javaClass)
                val (tempAfter, labelAfter) = translator.translate(
                    top.node,
                    top.location,
                    tempCounter,
                    labelCounter,
                    variableToTypeMap,
                    stack,
                    resultStack,
                    expressionStack,
                    labelStack
                )
                tempCounter = tempAfter
                labelCounter = labelAfter
            } else {
                when (top.location) {
                    StatementTranslatorLocation.START -> {
                        stack.push(
                            StatementTranslatorStackItem(
                                StatementTranslatorLocation.END,
                                top.node
                            )
                        )
                        when (top.node) {
                            is ParsedWhileNode -> {
                                val (falseLabel, labelCountAfterFalse) = labelGenerator.generateLabel(labelCounter)
                                val (beginLabel, labelCountAfterBegin) = labelGenerator.generateLabel(labelCountAfterFalse)
                                val (trueLabel, labelCountAfterTrue) = labelGenerator.generateLabel(labelCountAfterBegin)
                                val (expression, l, t) = booleanExpressionTranslator.translate(
                                    top.node.expression,
                                    trueLabel,
                                    falseLabel,
                                    labelCountAfterTrue,
                                    tempCounter,
                                    variableToTypeMap
                                )
                                labelCounter = l
                                tempCounter = t
                                labelStack.push(trueLabel)
                                labelStack.push(beginLabel)
                                labelStack.push(falseLabel)
                                expressionStack.push(expression)
                                stack.push(
                                    StatementTranslatorStackItem(
                                        StatementTranslatorLocation.START,
                                        top.node.body
                                    )
                                )
                            }
                            is ParsedForNode -> {
                                val (falseLabel, labelCountAfterFalse) = labelGenerator.generateLabel(labelCounter)
                                val (beginLabel, labelCountAfterBegin) = labelGenerator.generateLabel(labelCountAfterFalse)
                                val (trueLabel, labelCountAfterTrue) = labelGenerator.generateLabel(labelCountAfterBegin)
                                val (initExpression, tempAfterInit) = expressionTranslator.translate(
                                    top.node.initExpression,
                                    variableToTypeMap,
                                    tempCounter
                                )
                                val (testExpression, l, tempAfterTest) = booleanExpressionTranslator.translate(
                                    top.node.testExpression,
                                    trueLabel,
                                    falseLabel,
                                    labelCountAfterTrue,
                                    tempAfterInit,
                                    variableToTypeMap
                                )
                                val (incrementExpression, t) = expressionTranslator.translate(
                                    top.node.incrementExpression,
                                    variableToTypeMap,
                                    tempAfterTest
                                )
                                labelCounter = l
                                tempCounter = t
                                labelStack.push(trueLabel)
                                labelStack.push(beginLabel)
                                labelStack.push(falseLabel)
                                expressionStack.push(incrementExpression)
                                expressionStack.push(testExpression)
                                expressionStack.push(initExpression)
                                stack.push(
                                    StatementTranslatorStackItem(
                                        StatementTranslatorLocation.START,
                                        top.node.body
                                    )
                                )
                            }
                            is ParsedIfNode -> {
                                if (top.node.elseBody == null) {
                                    val (nextLabel, labelCountAfterNext) = labelGenerator.generateLabel(labelCounter)
                                    val (trueLabel, labelCountAfterTrue) = labelGenerator.generateLabel(labelCountAfterNext)
                                    val (expression, l, t) = booleanExpressionTranslator.translate(
                                        top.node.booleanExpression,
                                        trueLabel,
                                        nextLabel,
                                        labelCountAfterTrue,
                                        tempCounter,
                                        variableToTypeMap
                                    )
                                    labelCounter = l
                                    tempCounter = t
                                    labelStack.push(trueLabel)
                                    labelStack.push(nextLabel)
                                    expressionStack.push(expression)
                                    stack.push(
                                        StatementTranslatorStackItem(
                                            StatementTranslatorLocation.START,
                                            top.node.ifBody
                                        )
                                    )
                                } else {
                                    val (nextLabel, labelCountAfterNext) = labelGenerator.generateLabel(labelCounter)
                                    val (trueLabel, labelCountAfterTrue) = labelGenerator.generateLabel(labelCountAfterNext)
                                    val (falseLabel, labelCountAfterFalse) = labelGenerator.generateLabel(labelCountAfterTrue)
                                    val (expression, l, t) = booleanExpressionTranslator.translate(
                                        top.node.booleanExpression,
                                        trueLabel,
                                        falseLabel,
                                        labelCountAfterFalse,
                                        tempCounter,
                                        variableToTypeMap
                                    )
                                    labelCounter = l
                                    tempCounter = t
                                    labelStack.push(falseLabel)
                                    labelStack.push(trueLabel)
                                    labelStack.push(nextLabel)
                                    expressionStack.push(expression)
                                    stack.push(
                                        StatementTranslatorStackItem(
                                            StatementTranslatorLocation.START,
                                            top.node.elseBody
                                        )
                                    )
                                    stack.push(
                                        StatementTranslatorStackItem(
                                            StatementTranslatorLocation.START,
                                            top.node.ifBody
                                        )
                                    )
                                }
                            }

                        }
                    }
                    StatementTranslatorLocation.END -> {
                        when (top.node) {
                            is ParsedWhileNode -> {
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
                            }
                            is ParsedForNode -> {
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
                            }
                            is ParsedIfNode -> {
                                if (top.node.elseBody == null) {
                                    val nextLabel = labelStack.pop()
                                    val trueLabel = labelStack.pop()
                                    val expression = expressionStack.pop()
                                    val body = resultStack.pop()

                                    val ifNode = TranslatedIfNode(
                                        expression,
                                        body,
                                        null,
                                        nextLabel,
                                        trueLabel,
                                        nextLabel
                                    )
                                    resultStack.push(ifNode)
                                } else {
                                    val nextLabel = labelStack.pop()
                                    val trueLabel = labelStack.pop()
                                    val falseLabel = labelStack.pop()
                                    val expression = expressionStack.pop()
                                    val elseBody = resultStack.pop()
                                    val ifBody = resultStack.pop()

                                    val ifNode = TranslatedIfNode(
                                        expression,
                                        ifBody,
                                        elseBody,
                                        nextLabel,
                                        trueLabel,
                                        falseLabel
                                    )
                                    resultStack.push(ifNode)
                                }
                            }
                            is VariableDeclarationListNode -> {
                                variableTypeRecorder.recordVariableTypes(
                                    top.node,
                                    variableToTypeMap
                                )
                                resultStack.push(top.node)
                            }
                            is ParsedReturnNode -> {
                                val (returnStatement, t) = returnStatementTranslator.translate(
                                    top.node,
                                    variableToTypeMap,
                                    tempCounter
                                )
                                tempCounter = t
                                resultStack.push(returnStatement)
                            }
                            is ParsedExpressionStatementNode -> {
                                val (expressionStatement, t) = expressionStatementTranslator.translate(
                                    top.node,
                                    variableToTypeMap,
                                    tempCounter
                                )
                                tempCounter = t
                                resultStack.push(expressionStatement)
                            }
                        }
                    }
                }
            }
        }

        return resultStack.pop() as TranslatedBasicBlockNode
    }
}