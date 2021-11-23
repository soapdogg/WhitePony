package compiler.core

data class ParsedAssignNode(
    override val expressionNode: IParsedExpressionNode
): IAssignNode
