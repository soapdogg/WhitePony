package compiler.parser.impl.internal

import compiler.core.nodes.AssignNode
import compiler.core.tokenizer.Token

internal interface IAssignParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<AssignNode, Int>
}