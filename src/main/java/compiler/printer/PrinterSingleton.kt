package compiler.printer

import compiler.core.nodes.VariableDeclarationListNode
import compiler.core.nodes.parsed.ParsedDoWhileNode
import compiler.core.nodes.parsed.ParsedForNode
import compiler.core.nodes.parsed.ParsedWhileNode
import compiler.core.nodes.translated.TranslatedForNode
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

    private val codeGenerator = CodeGenerator()
    private val labelCodeGenerator = LabelCodeGenerator()
    private val gotoCodeGenerator = GotoCodeGenerator()
    private val statementPrinterStackPusher = StatementPrinterStackPusher()

    private val parsedDoWhileStatementPrinter = ParsedDoWhileStatementPrinter(
        statementPrinterStackPusher,
        expressionPrinter
    )

    private val parsedForStatementPrinter = ParsedForStatementPrinter(
        statementPrinterStackPusher,
        expressionPrinter
    )

    private val parsedWhileStatementPrinter = ParsedWhileStatementPrinter(
        statementPrinterStackPusher,
        expressionPrinter
    )

    private val translatedForStatementPrinter = TranslatedForStatementPrinter(
        statementPrinterStackPusher,
        codeGenerator,
        labelCodeGenerator,
        gotoCodeGenerator
    )

    private val variableDeclarationListStatementPrinter = VariableDeclarationListStatementPrinter(
        variableDeclarationListPrinter
    )

    private val printerMap = mapOf(
        ParsedDoWhileNode::class.java to parsedDoWhileStatementPrinter,
        ParsedForNode::class.java to parsedForStatementPrinter,
        ParsedWhileNode::class.java to parsedWhileStatementPrinter,
        TranslatedForNode::class.java to translatedForStatementPrinter,
        VariableDeclarationListNode::class.java to variableDeclarationListStatementPrinter
    )

    private val statementPrinter = StatementPrinterOrchestrator(
        printerMap,
        returnStatementPrinter,
        expressionStatementPrinter,
        expressionPrinter,
        codeGenerator,
        labelCodeGenerator,
        gotoCodeGenerator
    )
    private val functionDeclarationPrinter = FunctionDeclarationPrinter(statementPrinter)

    private val declarationStatementPrinter = DeclarationStatementPrinter(
        functionDeclarationPrinter,
        variableDeclarationListPrinter,
    )
    val printer: IPrinter = Printer(declarationStatementPrinter)
}