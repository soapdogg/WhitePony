package compiler.core.nodes.parsed

import compiler.core.nodes.IFunctionDeclarationNode

data class ParsedFunctionDeclarationNode(
    override val functionName: String,
    override val type: String,
    override val basicBlockNode: ParsedBasicBlockNode
): IParsedDeclarationStatementNode, IFunctionDeclarationNode