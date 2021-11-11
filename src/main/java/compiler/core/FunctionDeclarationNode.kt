package compiler.core

data class FunctionDeclarationNode(
    val functionName: String,
    val type: String,
    val basicBlockNode: BasicBlockNode
): IDeclarationChildNode