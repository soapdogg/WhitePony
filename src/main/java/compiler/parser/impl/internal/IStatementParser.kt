package compiler.parser.impl.internal

import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.tokenizer.Token

internal interface IStatementParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ParsedBasicBlockNode, Int>
}