package compiler.parser.impl.internal

import compiler.core.AssignNode
import compiler.core.Token

internal interface IAssignParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<AssignNode, Int>
}