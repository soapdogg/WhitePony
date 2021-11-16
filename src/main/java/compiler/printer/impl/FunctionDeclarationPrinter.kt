package compiler.printer.impl

import compiler.core.ParsedFunctionDeclarationNode
import compiler.printer.impl.internal.IFunctionDeclarationPrinter
import compiler.printer.impl.internal.IStatementPrinter

internal class FunctionDeclarationPrinter(
    private val statementPrinter: IStatementPrinter
): IFunctionDeclarationPrinter {
    override fun printParsedNode(node: ParsedFunctionDeclarationNode): String {
        return node.type + " " + node.functionName + "() " + statementPrinter.printParsedNode(node.basicBlockNode, 0)
    }
}