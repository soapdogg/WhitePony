package compiler.translator.impl

import compiler.core.*
import compiler.translator.impl.internal.IExpressionStatementTranslator
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IReturnStatementTranslator
import compiler.translator.impl.internal.IStatementTranslator
import compiler.translator.impl.internal.IVariableDeclarationListTranslator
import java.lang.Exception

internal class StatementTranslatorRecursive(
    private val expressionTranslator:IExpressionTranslator,
    private val variableDeclarationListTranslator: IVariableDeclarationListTranslator,
    private val returnStatementTranslator: IReturnStatementTranslator,
    private val expressionStatementTranslator: IExpressionStatementTranslator,
): IStatementTranslator {
    override fun translate(statementNode: IParsedStatementNode): ITranslatedStatementNode {
        return when (statementNode) {
            is ParsedBasicBlockNode -> {
                val translatedStatements = statementNode.statements.map {
                    translate(it)
                }
                TranslatedBasicBlockNode(translatedStatements)
            }
            is ParsedDoWhileNode -> {
                val translatedExpression = expressionTranslator.translate(statementNode.expression)
                val translatedStatement = translate(statementNode.body)
                TranslatedDoWhileNode(
                    translatedExpression,
                    translatedStatement
                )
            }
            is ParsedForNode -> {
                val translatedInitExpression = expressionTranslator.translate(statementNode.initExpression)
                val translatedTestExpression = expressionTranslator.translate(statementNode.testExpression)
                val translatedIncrementExpression = expressionTranslator.translate(statementNode.incrementExpression)
                val translatedBody = translate(statementNode.body)
                TranslatedForNode(
                    translatedInitExpression,
                    translatedTestExpression,
                    translatedIncrementExpression,
                    translatedBody
                )
            }
            is ParsedWhileNode -> {
                val translatedExpression = expressionTranslator.translate(statementNode.expression)
                //GetNextLabel
                val translatedBody = translate(statementNode.body)
                TranslatedWhileNode(
                    translatedExpression,
                    translatedBody
                )
            }
            is ParsedIfNode -> {
                //GetNextLabelFalse
                //GetNextLabelTrue
                val translatedExpression = expressionTranslator.translate(statementNode.booleanExpression)
                val translatedBody = translate(statementNode.ifBody)
                TranslatedIfNode(
                    translatedExpression,
                    translatedBody
                )
            }
            is ParsedElseNode -> {
                //GetNextLabel
                val translatedBody = translate(statementNode.elseBody)
                TranslatedElseNode(
                    translatedBody
                )
            }
            is ParsedVariableDeclarationListNode -> {
                variableDeclarationListTranslator.translate(statementNode)
            }
            is ParsedReturnNode -> {
                returnStatementTranslator.translate(statementNode)
            }
            is ParsedExpressionStatementNode -> {
                expressionStatementTranslator.translate(statementNode)
            }
            else -> {
                throw Exception("Impossible")
            }
        }
    }
}