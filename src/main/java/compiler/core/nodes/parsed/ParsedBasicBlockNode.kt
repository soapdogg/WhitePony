package compiler.core.nodes.parsed

import compiler.core.nodes.IBasicBlockNode

data class ParsedBasicBlockNode (
    override val statements: List<IParsedStatementNode>
): IParsedStatementNode, IBasicBlockNode