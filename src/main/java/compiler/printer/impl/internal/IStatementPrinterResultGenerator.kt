package compiler.printer.impl.internal

import compiler.core.IParsedStatementNode

internal interface IStatementPrinterResultGenerator {
    fun generateResult(
        node: IParsedStatementNode,
        numberOfTabs: Int,
        statementStrings: List<String>,
    ): String
}