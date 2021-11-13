package compiler.parser.impl

import compiler.core.*
import compiler.parser.impl.internal.IStatementParser

class StatementParserIterative(

): IStatementParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IStatementNode, Int> {

        val statementStack = Stack<StatementType>()

        statementStack.push(StatementType.BLOCK_STATEMENT)

        var latestStatement: IStatementNode? = null
        while(
            statementStack.isNotEmpty()
        ) {
            val top = statementStack.pop()

            when {
                top == StatementType.BLOCK_STATEMENT -> {
                    latestStatement = BasicBlockNode(listOf())
                }
            }
        }

        return Pair(latestStatement!!, tokens.size)
    }

}