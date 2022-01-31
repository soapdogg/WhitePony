package compiler.frontend.printer.impl

import compiler.core.nodes.IProgramRootNode
import compiler.frontend.printer.IPrinter
import compiler.frontend.printer.impl.internal.IDeclarationStatementPrinter

internal class Printer(
    private val declarationStatementPrinter: IDeclarationStatementPrinter
): IPrinter {
    override fun printNode(node: IProgramRootNode, appendSemicolon: Boolean): String {
        val declarationStatements = node.declarationStatements.map {
            declarationStatementPrinter.printNode(it, appendSemicolon)
        }
        return declarationStatements.joinToString("\n")
    }
}