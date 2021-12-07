package compiler.parser.impl.internal

import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.tokenizer.Token

internal interface IStatementParserOrchestrator {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int,
        useShiftReduce: Boolean
    ): Pair<ParsedBasicBlockNode, Int>
}