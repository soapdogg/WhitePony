package compiler.core

data class ParsedProgramRootNode(
    override val declarationStatements: List<IParsedDeclarationStatementNode>
):IProgramRootNode