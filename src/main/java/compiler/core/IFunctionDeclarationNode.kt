package compiler.core

interface IFunctionDeclarationNode{
    val type: String
    val functionName: String
    val basicBlockNode: IBasicBlockNode
}