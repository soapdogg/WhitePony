package compiler.parser.impl.internal

import compiler.core.FunctionDeclarationNode
import compiler.core.Token

internal interface IFunctionDeclarationParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<FunctionDeclarationNode, Int>
}