package compiler.frontend.parser.impl.internal

import compiler.core.nodes.parsed.ParsedFunctionDeclarationNode
import compiler.core.tokenizer.Token

internal interface IFunctionDeclarationParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<ParsedFunctionDeclarationNode, Int>
}