package compiler.parser.impl.internal

import compiler.core.Token
import compiler.core.ParsedVariableDeclarationNode

internal interface IVariableDeclarationParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ParsedVariableDeclarationNode, Int>
}