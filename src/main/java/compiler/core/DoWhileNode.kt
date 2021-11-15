package compiler.core

data class DoWhileNode(
    val expression: IParsedExpressionNode,
    val body: IStatementNode
): IStatementNode
