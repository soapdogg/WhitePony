package compiler.core

data class BasicBlockNode (
    val statements: List<IStatementNode>
): IStatementNode