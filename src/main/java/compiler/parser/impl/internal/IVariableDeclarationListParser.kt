package compiler.parser.impl.internal

import compiler.core.Token
import compiler.core.ParsedVariableDeclarationListNode

internal interface IVariableDeclarationListParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<ParsedVariableDeclarationListNode, Int>
}