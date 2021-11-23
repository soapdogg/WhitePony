package compiler.core

data class ParsedFunctionDeclarationNode(
    override val functionName: String,
    override val type: String,
    override val basicBlockNode: ParsedBasicBlockNode
): IParsedDeclarationStatementNode, IFunctionDeclarationNode