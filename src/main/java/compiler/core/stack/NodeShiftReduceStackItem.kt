package compiler.core.stack

import compiler.core.nodes.parsed.IParsedExpressionNode

data class NodeShiftReduceStackItem(
    val node: IParsedExpressionNode
): IShiftReduceStackItem
