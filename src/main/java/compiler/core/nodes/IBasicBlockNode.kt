package compiler.core.nodes

interface IBasicBlockNode: IStatementNode {
    val statements: List<IStatementNode>
}