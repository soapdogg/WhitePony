package compiler.core

data class TranslatedAssignNode(
    override val expressionNode: ITranslatedExpressionNode
): IAssignNode