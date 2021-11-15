package compiler.core

data class WhileNode(
    val expression: IParsedExpressionNode,
    val body: IStatementNode
): IStatementNode
