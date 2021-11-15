package compiler.core

data class TranslatedFunctionDeclarationNode(
    val type: String,
    val functionName: String,
    val basicBlockNode: TranslatedBasicBlockNode
) : ITranslatedDeclarationStatementNode