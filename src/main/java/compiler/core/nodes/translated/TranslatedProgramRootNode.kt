package compiler.core.nodes.translated

import compiler.core.nodes.IProgramRootNode

data class TranslatedProgramRootNode(
    override val declarationStatements: List<ITranslatedDeclarationStatementNode>
): IProgramRootNode
