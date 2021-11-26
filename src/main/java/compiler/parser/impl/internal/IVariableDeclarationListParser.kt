package compiler.parser.impl.internal

import compiler.core.tokenizer.Token
import compiler.core.nodes.VariableDeclarationListNode

internal interface IVariableDeclarationListParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<VariableDeclarationListNode, Int>
}