package compiler.printer.impl.internal

import compiler.core.IParsedStatementNode

internal interface IStatementPrinter {
    fun printParsedNode(node: IParsedStatementNode, numberOfTabs: Int): String
}