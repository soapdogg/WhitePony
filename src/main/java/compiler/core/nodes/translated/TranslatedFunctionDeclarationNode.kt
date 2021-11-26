package compiler.core.nodes.translated

import compiler.core.nodes.IFunctionDeclarationNode

data class TranslatedFunctionDeclarationNode(
    override val type: String,
    override val functionName: String,
    override val basicBlockNode: TranslatedBasicBlockNode
) : ITranslatedDeclarationStatementNode, IFunctionDeclarationNode