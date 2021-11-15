package compiler.core

data class BinaryArrayOperatorNode(
    val variableExpression: VariableExpressionNode,
    val insideExpression: IParsedExpressionNode
): IParsedExpressionNode
