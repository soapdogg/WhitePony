package compiler.parser.impl

import compiler.core.IParsedDeclarationStatementNode
import compiler.core.ParsedProgramRootNode
import compiler.core.Token
import compiler.parser.IParser
import compiler.parser.impl.internal.IDeclarationStatementParser

internal class Parser(
    private val declarationStatementParser: IDeclarationStatementParser
) : IParser {

    override fun parse(tokens: List<Token>): ParsedProgramRootNode {
        val declarationStatementNodes = mutableListOf<IParsedDeclarationStatementNode>()
        var startingPosition = 0
        while (startingPosition < tokens.size) {
            val (declarationStatement, s) = declarationStatementParser.parse(tokens, startingPosition)
            declarationStatementNodes.add(declarationStatement)
            startingPosition = s
        }
        return ParsedProgramRootNode(declarationStatementNodes)
    }
}