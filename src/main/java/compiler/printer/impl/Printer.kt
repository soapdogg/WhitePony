package compiler.printer.impl

import compiler.core.nodes.IProgramRootNode
import compiler.printer.IPrinter
import compiler.printer.impl.internal.IDeclarationStatementPrinter

internal class Printer(
    private val declarationStatementPrinter: IDeclarationStatementPrinter
): IPrinter {
    override fun printNode(node: IProgramRootNode): String {
        val declarationStatements = node.declarationStatements.map {
            declarationStatementPrinter.printNode(it)
        }
        return declarationStatements.joinToString("\n")
    }
}