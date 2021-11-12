package compiler.parser.impl.internal

import compiler.core.IDeclarationStatementNode
import compiler.core.Token

internal interface IDeclarationStatementParser {
    fun parse(tokens: List<Token>, startingPosition: Int): Pair<IDeclarationStatementNode, Int>
}