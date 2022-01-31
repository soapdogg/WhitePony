package compiler.frontend.printer.impl

import compiler.core.nodes.IFunctionDeclarationNode
import compiler.core.constants.PrinterConstants
import compiler.frontend.printer.impl.internal.IFunctionDeclarationPrinter
import compiler.frontend.printer.impl.internal.IStatementPrinterOrchestrator

internal class FunctionDeclarationPrinter(
    private val statementPrinter: IStatementPrinterOrchestrator
): IFunctionDeclarationPrinter {
    override fun printNode(node: IFunctionDeclarationNode, appendSemicolon: Boolean): String {
        return node.type +
                PrinterConstants.SPACE +
                node.functionName +
                PrinterConstants.LEFT_PARENTHESES +
                PrinterConstants.RIGHT_PARENTHESES +
                PrinterConstants.SPACE +
                statementPrinter.printNode(node.basicBlockNode, PrinterConstants.INITIAL_NUMBER_OF_TABS, appendSemicolon)
    }
}