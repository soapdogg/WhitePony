package compiler.parser.impl.internal

import compiler.core.nodes.ArrayNode
import compiler.core.tokenizer.Token

internal interface IArrayParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ArrayNode, Int>
}