package compiler.core

data class TranslatedProgramRootNode(
    override val declarationStatements: List<ITranslatedDeclarationStatementNode>
):IProgramRootNode
