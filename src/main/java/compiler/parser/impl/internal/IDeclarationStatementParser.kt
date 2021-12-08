package compiler.parser.impl.internal

import compiler.core.nodes.parsed.IParsedDeclarationStatementNode
import compiler.core.tokenizer.Token

internal interface IDeclarationStatementParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedDeclarationStatementNode, Int>
}