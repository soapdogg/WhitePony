package compiler.parser.impl.internal

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack

internal interface IExpressionNodeReductionOrchestrator {
    fun reduce(
        lookAheadValue: String,
        leftItem: NodeShiftReduceStackItem,
        operatorItem: OperatorShiftReduceStackItem,
        parseStack: Stack<IShiftReduceStackItem>
    ): Boolean
}