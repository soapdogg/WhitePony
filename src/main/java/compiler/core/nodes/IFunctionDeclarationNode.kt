package compiler.core.nodes

interface IFunctionDeclarationNode{
    val type: String
    val functionName: String
    val basicBlockNode: IBasicBlockNode
}