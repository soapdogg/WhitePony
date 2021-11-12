package compiler.parser.impl.internal

import compiler.core.Token
import compiler.core.VariableDeclarationNode

internal interface IVariableDeclarationParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<VariableDeclarationNode, Int>
}