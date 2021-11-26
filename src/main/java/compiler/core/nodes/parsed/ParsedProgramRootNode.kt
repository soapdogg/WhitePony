package compiler.core.nodes.parsed

import compiler.core.nodes.IProgramRootNode

data class ParsedProgramRootNode(
    override val declarationStatements: List<IParsedDeclarationStatementNode>
): IProgramRootNode