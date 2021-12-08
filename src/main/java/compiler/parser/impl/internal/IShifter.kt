package compiler.parser.impl.internal

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token

internal interface IShifter {
    fun shift(
        tokens: List<Token>,
        currentPosition: Int,
        parseStack: Stack<IShiftReduceStackItem>
    ): Int
}