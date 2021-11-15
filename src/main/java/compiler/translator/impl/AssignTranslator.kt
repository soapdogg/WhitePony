package compiler.translator.impl

import compiler.core.ParsedAssignNode
import compiler.core.TranslatedAssignNode
import compiler.translator.impl.internal.IAssignTranslator
import compiler.translator.impl.internal.IExpressionTranslator

internal class AssignTranslator(
    private val expressionTranslator: IExpressionTranslator
): IAssignTranslator {
    override fun translate(
        assignNode: ParsedAssignNode
    ): TranslatedAssignNode {

        val translatedExpressionNode = expressionTranslator.translate(assignNode.expressionNode)

        return TranslatedAssignNode(translatedExpressionNode)
    }
}