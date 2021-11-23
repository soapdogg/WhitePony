package compiler.printer.impl.internal

import compiler.core.IStatementNode

internal interface IStatementPrinterResultGenerator {
    fun generateResult(
        node: IStatementNode,
        numberOfTabs: Int,
        statementStrings: List<String>,
    ): String
}