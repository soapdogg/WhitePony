package compiler.translator.impl

import compiler.core.*
import compiler.translator.impl.internal.*
import compiler.translator.impl.internal.IExpressionStatementTranslator
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IReturnStatementTranslator
import compiler.translator.impl.internal.IStatementTranslator

internal class StatementTranslator (
    private val variableTypeRecorder: IVariableTypeRecorder,
    private val expressionTranslator: IExpressionTranslator,
    private val booleanExpressionTranslator: IBooleanExpressionTranslator,
    private val returnStatementTranslator: IReturnStatementTranslator,
    private val expressionStatementTranslator: IExpressionStatementTranslator,
): IStatementTranslator {
    override fun translate(
        statementNode: IParsedStatementNode,
        variableToTypeMap: MutableMap<String, String>
    ): TranslatedBasicBlockNode {

        val stack = Stack<StatementTranslatorStackItem>()
        stack.push(StatementTranslatorStackItem(1, statementNode))
        val resultStack = Stack<ITranslatedStatementNode>()
        val expressionStack = Stack<ITranslatedExpressionNode>()
        var tempCounter = 0
        var labelCounter = 0

        while(stack.isNotEmpty()) {
            val top = stack.pop()
            when(top.location) {
                1 -> {
                    stack.push(StatementTranslatorStackItem(2, top.node))
                    when (top.node) {
                        is ParsedBasicBlockNode -> {
                            top.node.statements.reversed().forEach {
                                stack.push(StatementTranslatorStackItem(1, it))
                            }
                        }
                        is ParsedDoWhileNode -> {
                            val (expression, l, t) = booleanExpressionTranslator.translate(top.node.expression, labelCounter, tempCounter)
                            labelCounter = l
                            tempCounter = t
                            expressionStack.push(expression)
                            stack.push(StatementTranslatorStackItem(1, top.node.body))
                        }
                        is ParsedWhileNode -> {
                            val (expression, l, t)  = booleanExpressionTranslator.translate(top.node.expression, labelCounter, tempCounter)
                            labelCounter = l
                            tempCounter = t
                            expressionStack.push(expression)
                            stack.push(StatementTranslatorStackItem(1, top.node.body))
                        }
                        is ParsedForNode -> {
                            val (initExpression, tempAfterInit) = expressionTranslator.translate(top.node.initExpression, variableToTypeMap, tempCounter)
                            val (testExpression, l, tempAfterTest) = booleanExpressionTranslator.translate(top.node.testExpression, labelCounter, tempAfterInit)
                            val (incrementExpression, t) = expressionTranslator.translate(top.node.incrementExpression, variableToTypeMap, tempAfterTest)
                            labelCounter = l
                            tempCounter = t
                            expressionStack.push(incrementExpression)
                            expressionStack.push(testExpression)
                            expressionStack.push(initExpression)
                            stack.push(StatementTranslatorStackItem(1, top.node.body))
                        }
                        is ParsedIfNode -> {
                            val (expression, l, t) = booleanExpressionTranslator.translate(top.node.booleanExpression, labelCounter, tempCounter)
                            labelCounter = l
                            tempCounter = t
                            expressionStack.push(expression)
                            stack.push(StatementTranslatorStackItem(1, top.node.ifBody))
                        }
                        is ParsedElseNode -> {
                            stack.push(StatementTranslatorStackItem(1, top.node.elseBody))
                        }

                    }
                }
                2 -> {

                    when(top.node) {
                        is ParsedDoWhileNode -> {
                            val expression = expressionStack.pop()
                            val body = resultStack.pop()
                            val doWhile = TranslatedDoWhileNode(
                                expression,
                                body
                            )
                            resultStack.push(doWhile)
                        }
                        is ParsedWhileNode -> {
                            val expression = expressionStack.pop()
                            val body = resultStack.pop()
                            val whileNode = TranslatedWhileNode(
                                expression,
                                body
                            )
                            resultStack.push(whileNode)
                        }
                        is ParsedForNode -> {
                            val initExpression = expressionStack.pop()
                            val testExpression = expressionStack.pop()
                            val incrementExpression = expressionStack.pop()
                            val body = resultStack.pop()
                            val forNode = TranslatedForNode(
                                initExpression,
                                testExpression,
                                incrementExpression,
                                body
                            )
                            resultStack.push(forNode)
                        }
                        is ParsedIfNode -> {
                            val expression = expressionStack.pop()
                            val body = resultStack.pop()
                            val ifNode = TranslatedIfNode(
                                expression,
                                body
                            )
                            resultStack.push(ifNode)
                        }
                        is ParsedElseNode -> {
                            val body = resultStack.pop()
                            val elseNode = TranslatedElseNode(body)
                            resultStack.push(elseNode)
                        }
                        is VariableDeclarationListNode -> {
                            variableTypeRecorder.recordVariableTypes(top.node, variableToTypeMap)
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
        val translatedNodes = mutableListOf<ITranslatedStatementNode>()
        while (resultStack.isNotEmpty()) {
            translatedNodes.add(resultStack.pop())
        }

        return TranslatedBasicBlockNode(translatedNodes.reversed())
    }
}