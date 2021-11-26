package compiler.printer.impl

import compiler.core.nodes.IFunctionDeclarationNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IFunctionDeclarationPrinter
import compiler.printer.impl.internal.IStatementPrinter

internal class FunctionDeclarationPrinter(
    private val statementPrinter: IStatementPrinter
): IFunctionDeclarationPrinter {
    override fun printNode(node: IFunctionDeclarationNode): String {
        return node.type +
                PrinterConstants.SPACE +
                node.functionName +
                PrinterConstants.LEFT_PARENTHESES +
                PrinterConstants.RIGHT_PARENTHESES +
                PrinterConstants.SPACE +
                statementPrinter.printNode(node.basicBlockNode, PrinterConstants.INITIAL_NUMBER_OF_TABS)
    }
}