package compiler.printer

import compiler.printer.impl.*
import compiler.printer.impl.DeclarationStatementPrinter
import compiler.printer.impl.FunctionDeclarationPrinter
import compiler.printer.impl.Printer

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

    private val statementPrinterStackItemGenerator = StatementPrinterStackItemGenerator()

    private val statementPrinterResultGenerator = StatementPrinterResultGenerator(
        variableDeclarationListPrinter,
        returnStatementPrinter,
        expressionStatementPrinter,
        expressionPrinter
    )

    private val statementPrinter = StatementPrinter(
        statementPrinterStackItemGenerator,
        statementPrinterResultGenerator
    )
    private val functionDeclarationPrinter = FunctionDeclarationPrinter(statementPrinter)

    private val declarationStatementPrinter = DeclarationStatementPrinter(
        functionDeclarationPrinter,
        variableDeclarationListPrinter,
    )
    val printer: IPrinter = Printer(declarationStatementPrinter)
}