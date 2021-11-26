package compiler.parser.impl.internal

import compiler.core.tokenizer.Token
import compiler.core.nodes.VariableDeclarationNode

internal interface IVariableDeclarationParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<VariableDeclarationNode, Int>
}