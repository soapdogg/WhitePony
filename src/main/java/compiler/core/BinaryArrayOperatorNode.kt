package compiler.core

data class BinaryArrayOperatorNode(
    val variableExpression: VariableExpressionNode,
    val insideExpression: IExpressionNode
): IExpressionNode
