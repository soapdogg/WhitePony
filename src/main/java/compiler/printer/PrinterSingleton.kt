package compiler.printer

import compiler.core.nodes.IStatementNode
import compiler.core.nodes.parsed.ParsedDoWhileNode
import compiler.printer.impl.*
import compiler.printer.impl.DeclarationStatementPrinter
import compiler.printer.impl.FunctionDeclarationPrinter
import compiler.printer.impl.Printer
import compiler.printer.impl.internal.IStatementPrinter

enum class PrinterSingleton {
    INSTANCE;

    private val expressionPrinter = ExpressionPrinter()
    private val arrayPrinter = ArrayPrinter(expressionPrinter)
    private val assignPrinter = AssignPrinter(expressionPrinter)
    private val variableDeclarationPrinter = VariableDeclarationPrinter(
        arrayPrinter,
        assignPrinter
    )
    private val variableDeclarationListPrinter = VariableDeclarationListPrinter(
        variableDeclarationPrinter
    )
    private val expressionStatementPrinter = ExpressionStatementPrinter(
        expressionPrinter
    )
    private val returnStatementPrinter = ReturnStatementPrinter(
        expressionStatementPrinter
    )

    private val statementPrinterStackPusher = StatementPrinterStackPusher()

    private val parsedDoWhileStatementPrinter = ParsedDoWhileStatementPrinter(
        statementPrinterStackPusher,
        expressionPrinter
    )

    private val printerMap = mapOf<Class<out IStatementNode>, IStatementPrinter>(
        ParsedDoWhileNode::class.java to parsedDoWhileStatementPrinter
    )

    private val statementPrinter = StatementPrinterOrchestrator(
        printerMap,
        variableDeclarationListPrinter,
        returnStatementPrinter,
        expressionStatementPrinter,
        expressionPrinter
    )
    private val functionDeclarationPrinter = FunctionDeclarationPrinter(statementPrinter)

    private val declarationStatementPrinter = DeclarationStatementPrinter(
        functionDeclarationPrinter,
        variableDeclarationListPrinter,
    )
    val printer: IPrinter = Printer(declarationStatementPrinter)
}