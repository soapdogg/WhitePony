package compiler.core

data class ParsedFunctionDeclarationNode(
    val functionName: String,
    val type: String,
    val basicBlockNode: BasicBlockNode
): IParsedDeclarationStatementNode