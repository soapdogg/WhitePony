package compiler.printer.impl

import compiler.core.ParsedProgramRootNode
import compiler.printer.IPrinter
import compiler.printer.impl.internal.IDeclarationStatementPrinter

internal class Printer(
    private val declarationStatementPrinter: IDeclarationStatementPrinter
): IPrinter {
    override fun printParsedNode(node: ParsedProgramRootNode): String {
        val declarationStatements = node.declarationStatements.map {
            declarationStatementPrinter.printParsedNode(it)
        }
        return declarationStatements.joinToString("\n")
    }
}