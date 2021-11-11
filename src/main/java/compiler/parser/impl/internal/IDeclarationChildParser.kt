package compiler.parser.impl.internal

import compiler.core.IDeclarationChildNode
import compiler.core.Token

interface IDeclarationChildParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int,
        type: String,
        identifierValue: String,
    ): Pair<IDeclarationChildNode, Int>
}