package compiler.parser.impl.internal

import compiler.core.BasicBlockNode
import compiler.core.Token

internal interface IBasicBlockParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<BasicBlockNode, Int>
}