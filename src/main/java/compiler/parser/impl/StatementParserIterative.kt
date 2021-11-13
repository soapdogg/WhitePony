package compiler.parser.impl

import compiler.core.IStatementNode
import compiler.core.Token
import compiler.parser.impl.internal.IStatementParser

class StatementParserIterative(

): IStatementParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IStatementNode, Int> {


        TODO("Not yet implemented")
    }

}