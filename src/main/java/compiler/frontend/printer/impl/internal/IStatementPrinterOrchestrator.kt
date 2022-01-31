package compiler.frontend.printer.impl.internal

import compiler.core.nodes.IStatementNode

internal interface IStatementPrinterOrchestrator {
    fun printNode(node: IStatementNode, numberOfTabs: Int, appendSemicolon: Boolean): String
}