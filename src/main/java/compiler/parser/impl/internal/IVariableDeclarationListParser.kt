package compiler.parser.impl.internal

import compiler.core.Token
import compiler.core.VariableDeclarationListNode

internal interface IVariableDeclarationListParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<VariableDeclarationListNode, Int>
}