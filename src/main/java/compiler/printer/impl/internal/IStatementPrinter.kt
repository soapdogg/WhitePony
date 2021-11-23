package compiler.printer.impl.internal

import compiler.core.IStatementNode

internal interface IStatementPrinter {
    fun printNode(node: IStatementNode, numberOfTabs: Int): String
}