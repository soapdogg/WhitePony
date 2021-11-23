package compiler.core

data class TranslatedFunctionDeclarationNode(
    override val type: String,
    override val functionName: String,
    override val basicBlockNode: TranslatedBasicBlockNode
) : ITranslatedDeclarationStatementNode, IFunctionDeclarationNode