package compiler.parser.impl.internal

import compiler.core.DeclarationStatementNode
import compiler.core.Token

internal interface IDeclarationStatementParser {
    fun parse(tokens: List<Token>, startingPosition: Int): Pair<DeclarationStatementNode, Int>
}