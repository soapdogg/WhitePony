package compiler.core

data class ParsedBinaryArrayOperatorNode(
    val variableExpression: ParsedVariableExpressionNode,
    val insideExpression: IParsedExpressionNode
): IParsedExpressionNode
