package compiler.core

interface IBasicBlockNode: IStatementNode {
    val statements: List<IStatementNode>
}