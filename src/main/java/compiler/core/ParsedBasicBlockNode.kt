package compiler.core

data class ParsedBasicBlockNode (
    val statements: List<IParsedStatementNode>
): IParsedStatementNode