package compiler.translator.impl

import compiler.core.*
import compiler.translator.impl.internal.IExpressionStatementTranslator
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IReturnStatementTranslator
import compiler.translator.impl.internal.IStatementTranslator

internal class StatementTranslator (
    private val expressionTranslator: IExpressionTranslator,
    private val returnStatementTranslator: IReturnStatementTranslator,
    private val expressionStatementTranslator: IExpressionStatementTranslator,
): IStatementTranslator {
    override fun translate(statementNode: IParsedStatementNode): TranslatedBasicBlockNode {

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
                            top.node.statements.forEach {
                                stack.push(StatementTranslatorStackItem(1, it))
                            }
                        }
                        is ParsedDoWhileNode -> {
                            val expression = expressionTranslator.translate(top.node.expression, labelCounter, tempCounter)
                            expressionStack.push(expression)
                            stack.push(StatementTranslatorStackItem(1, top.node.body))
                        }
                        is ParsedWhileNode -> {
                            val expression = expressionTranslator.translate(top.node.expression, labelCounter, tempCounter)
                            expressionStack.push(expression)
                            stack.push(StatementTranslatorStackItem(1, top.node.body))
                        }
                        is ParsedForNode -> {
                            val initExpression = expressionTranslator.translate(top.node.initExpression, labelCounter, tempCounter)
                            val testExpression = expressionTranslator.translate(top.node.testExpression, labelCounter, tempCounter)
                            val incrementExpression = expressionTranslator.translate(top.node.incrementExpression, labelCounter, tempCounter)
                            expressionStack.push(incrementExpression)
                            expressionStack.push(testExpression)
                            expressionStack.push(initExpression)
                            stack.push(StatementTranslatorStackItem(1, top.node.body))
                        }
                        is ParsedIfNode -> {
                            val expression = expressionTranslator.translate(top.node.booleanExpression, labelCounter, tempCounter)
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
                            resultStack.push(top.node as ITranslatedStatementNode)
                        }
                        is ParsedReturnNode -> {
                            val returnStatement = returnStatementTranslator.translate(
                                top.node,
                                labelCounter,
                                tempCounter
                            )
                            resultStack.push(returnStatement)
                        }
                        is ParsedExpressionStatementNode -> {
                            val expressionStatement = expressionStatementTranslator.translate(
                                top.node,
                                labelCounter,
                                tempCounter
                            )
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

        return TranslatedBasicBlockNode(translatedNodes)
    }
}