package compiler.parser.impl.internal

import compiler.core.ParsedFunctionDeclarationNode
import compiler.core.Token

internal interface IFunctionDeclarationParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<ParsedFunctionDeclarationNode, Int>
}