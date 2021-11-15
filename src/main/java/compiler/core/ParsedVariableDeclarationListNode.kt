package compiler.core

data class ParsedVariableDeclarationListNode(
    val type: String,
    val variableDeclarations: List<ParsedVariableDeclarationNode>
) : IParsedDeclarationStatementNode, IParsedStatementNode