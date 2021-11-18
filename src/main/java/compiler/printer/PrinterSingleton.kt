package compiler.printer

import compiler.printer.impl.*
import compiler.printer.impl.DeclarationStatementPrinter
import compiler.printer.impl.FunctionDeclarationPrinter
import compiler.printer.impl.Printer

enum class PrinterSingleton {
    INSTANCE;

    private val expressionPrinterIterative = ExpressionPrinterIterative()
    private val expressionPrinterRecursive = ExpressionPrinterRecursive()
    private val arrayPrinter = ArrayPrinter(expressionPrinterIterative)
    private val assignPrinter = AssignPrinter(expressionPrinterIterative)
    private val variableDeclarationPrinter = VariableDeclarationPrinter(
        arrayPrinter,
        assignPrinter
    )
    private val variableDeclarationListPrinter = VariableDeclarationListPrinter(
        variableDeclarationPrinter
    )
    private val expressionStatementPrinter = ExpressionStatementPrinter(
        expressionPrinterIterative
    )
    private val returnStatementPrinter = ReturnStatementPrinter(
        expressionStatementPrinter
    )

    private val statementPrinterStackItemGenerator = StatementPrinterStackItemGenerator()

    private val statementPrinterResultGenerator = StatementPrinterResultGenerator(
        variableDeclarationListPrinter,
        returnStatementPrinter,
        expressionStatementPrinter,
        expressionPrinterIterative
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