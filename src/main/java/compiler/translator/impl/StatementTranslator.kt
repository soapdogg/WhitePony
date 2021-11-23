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
    override fun translate(statementNode: IParsedStatementNode): ITranslatedStatementNode {

        val stack = Stack<Pair<Int, IParsedStatementNode>>()
        stack.push(Pair(1, statementNode))
        val resultStack = Stack<ITranslatedStatementNode>()
        val expressionStack = Stack<ITranslatedExpressionNode>()
        var tempCounter = 0
        var labelCounter = 0

        while(stack.isNotEmpty()) {
            val top = stack.pop()
            when(top.first) {
                1 -> {
                    stack.push(Pair(2, top.second))
                    when (top.second) {
                        is ParsedBasicBlockNode -> {
                            (top.second as ParsedBasicBlockNode).statements.reversed().forEach {
                                stack.push(Pair(1, it))
                            }
                        }
                        is ParsedDoWhileNode -> {
                            val expression = expressionTranslator.translate((top.second as ParsedDoWhileNode).expression, labelCounter, tempCounter)
                            expressionStack.push(expression)
                            stack.push(Pair(1, (top.second as ParsedDoWhileNode).body))
                        }
                        is ParsedWhileNode -> {
                            val expression = expressionTranslator.translate((top.second as ParsedWhileNode).expression, labelCounter, tempCounter)
                            expressionStack.push(expression)
                            stack.push(Pair(1, (top.second as ParsedWhileNode).body))
                        }
                        is ParsedForNode -> {
                            val initExpression = expressionTranslator.translate((top.second as ParsedForNode).initExpression, labelCounter, tempCounter)
                            val testExpression = expressionTranslator.translate((top.second as ParsedForNode).testExpression, labelCounter, tempCounter)
                            val incrementExpression = expressionTranslator.translate((top.second as ParsedForNode).incrementExpression, labelCounter, tempCounter)
                            expressionStack.push(incrementExpression)
                            expressionStack.push(testExpression)
                            expressionStack.push(initExpression)
                            stack.push(Pair(1, (top.second as ParsedForNode).body))
                        }
                        is ParsedIfNode -> {
                            val expression = expressionTranslator.translate((top.second as ParsedIfNode).booleanExpression, labelCounter, tempCounter)
                            expressionStack.push(expression)
                            stack.push(Pair(1, (top.second as ParsedIfNode).ifBody))
                        }
                        is ParsedElseNode -> {
                            stack.push(Pair(1, (top.second as ParsedElseNode).elseBody))
                        }

                    }
                }
                2 -> {
                    val translatedNodes = mutableListOf<ITranslatedStatementNode>()
                    for (i in 0 until top.second.getNumberOfStatements()) {
                        translatedNodes.add(resultStack.pop())
                    }


                    when(top.second) {
                        is ParsedBasicBlockNode -> {
                            val translatedBasicBlock = TranslatedBasicBlockNode(translatedNodes)
                            resultStack.push(translatedBasicBlock)
                        }
                        is ParsedDoWhileNode -> {
                            val expression = expressionStack.pop()
                            val body = translatedNodes[0]
                            val doWhile = TranslatedDoWhileNode(
                                expression,
                                body
                            )
                            resultStack.push(doWhile)
                        }
                        is ParsedWhileNode -> {
                            val expression = expressionStack.pop()
                            val body = translatedNodes[0]
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
                            val body = translatedNodes[0]
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
                            val body = translatedNodes[0]
                            val ifNode = TranslatedIfNode(
                                expression,
                                body
                            )
                            resultStack.push(ifNode)
                        }
                        is ParsedElseNode -> {
                            val body = translatedNodes[0]
                            val elseNode = TranslatedElseNode(body)
                            resultStack.push(elseNode)
                        }
                        is VariableDeclarationListNode -> {
                            resultStack.push(top.second as ITranslatedStatementNode)
                        }
                        is ParsedReturnNode -> {
                            val returnStatement = returnStatementTranslator.translate(
                                top.second as ParsedReturnNode,
                                labelCounter,
                                tempCounter
                            )
                            resultStack.push(returnStatement)
                        }
                        is ParsedExpressionStatementNode -> {
                            val expressionStatement = expressionStatementTranslator.translate(
                                top.second as ParsedExpressionStatementNode,
                                labelCounter,
                                tempCounter
                            )
                            resultStack.push(expressionStatement)
                        }
                    }
                }
            }
        }


        return resultStack.pop()

    }
}