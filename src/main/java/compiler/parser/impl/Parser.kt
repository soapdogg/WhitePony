package compiler.parser.impl

import compiler.core.DeclarationStatementNode
import compiler.core.ProgramRootNode
import compiler.core.Token
import compiler.parser.IParser
import compiler.parser.impl.internal.IDeclarationStatementParser

internal class Parser(
    private val declarationStatementParser: IDeclarationStatementParser
) : IParser {

    override fun parse(tokens: List<Token>): ProgramRootNode {
        val declarationStatementNodes = mutableListOf<DeclarationStatementNode>()
        var startingPosition = 0
        while (startingPosition < tokens.size) {
            val (declarationStatement, s) = declarationStatementParser.parse(tokens, startingPosition)
            declarationStatementNodes.add(declarationStatement)
            startingPosition = s
        }
        return ProgramRootNode(declarationStatementNodes)
    }
}