package compiler.frontend.parser.impl

import compiler.frontend.parser.impl.internal.IOperatorPrecedenceDeterminer

internal class OperatorPrecedenceDeterminer(
    private val operatorPrecedenceMap: Map<String, Int>,
    private val assignOperatorSet: Set<String>
): IOperatorPrecedenceDeterminer {
    override fun determinerIfLookaheadIsLowerPrecedenceThanCurrent(
        current: String,
        lookahead: String
    ): Boolean {
        if (!operatorPrecedenceMap.containsKey(lookahead)) return false
        if (assignOperatorSet.contains(lookahead)) return true
        val currentPrecedence = operatorPrecedenceMap.getValue(current)
        val lookaheadPrecedence = operatorPrecedenceMap.getValue(lookahead)
        return currentPrecedence > lookaheadPrecedence
    }
}