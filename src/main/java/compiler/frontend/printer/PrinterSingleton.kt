package compiler.frontend.printer

import compiler.core.nodes.VariableDeclarationListNode
import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.*
import compiler.frontend.printer.impl.*
import compiler.frontend.printer.impl.DeclarationStatementPrinter
import compiler.frontend.printer.impl.FunctionDeclarationPrinter
import compiler.frontend.printer.impl.Printer

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
    private val tabsGenerator = TabsGenerator()

    private val parsedBasicBlockStatementPrinter = ParsedBasicBlockStatementPrinter(
        statementPrinterStackPusher,
        tabsGenerator
    )
    private val parsedDoWhileStatementPrinter = ParsedDoWhileStatementPrinter(
        statementPrinterStackPusher,
        expressionPrinter
    )

    private val parsedExpressionStatementPrinter = ParsedExpressionStatementPrinter(
        expressionStatementPrinter
    )

    private val parsedForStatementPrinter = ParsedForStatementPrinter(
        statementPrinterStackPusher,
        expressionPrinter
    )

    private val parsedIfStatementPrinter = ParsedIfStatementPrinter(
        statementPrinterStackPusher,
        expressionPrinter,
        tabsGenerator
    )

    private val parsedReturnStatementPrinter = ParsedReturnStatementPrinter(
        returnStatementPrinter
    )

    private val translatedBasicBlockStatementPrinter = TranslatedBasicBlockStatementPrinter(
        statementPrinterStackPusher,
        codeGenerator
    )

    private val parsedWhileStatementPrinter = ParsedWhileStatementPrinter(
        statementPrinterStackPusher,
        expressionPrinter
    )

    private val translatedDoWhileStatementPrinter = TranslatedDoWhileStatementPrinter(
        statementPrinterStackPusher,
        codeGenerator,
        labelCodeGenerator
    )

    private val translatedExpressionStatementPrinter = TranslatedExpressionStatementPrinter(
        codeGenerator
    )

    private val translatedForStatementPrinter = TranslatedForStatementPrinter(
        statementPrinterStackPusher,
        codeGenerator,
        labelCodeGenerator,
        gotoCodeGenerator
    )

    private val translatedIfStatementPrinter = TranslatedIfStatementPrinter(
        statementPrinterStackPusher,
        codeGenerator,
        labelCodeGenerator,
        gotoCodeGenerator
    )

    private val translatedReturnStatementPrinter = TranslatedReturnStatementPrinter(
        codeGenerator
    )

    private val translatedWhileStatementPrinter = TranslatedWhileStatementPrinter(
        statementPrinterStackPusher,
        codeGenerator,
        labelCodeGenerator,
        gotoCodeGenerator
    )

    private val variableDeclarationListStatementPrinter = VariableDeclarationListStatementPrinter(
        variableDeclarationListPrinter
    )

    private val printerMap = mapOf(
        ParsedBasicBlockNode::class.java to parsedBasicBlockStatementPrinter,
        ParsedDoWhileNode::class.java to parsedDoWhileStatementPrinter,
        ParsedExpressionStatementNode::class.java to parsedExpressionStatementPrinter,
        ParsedForNode::class.java to parsedForStatementPrinter,
        ParsedIfNode::class.java to parsedIfStatementPrinter,
        ParsedReturnNode::class.java to parsedReturnStatementPrinter,
        ParsedWhileNode::class.java to parsedWhileStatementPrinter,
        TranslatedBasicBlockNode::class.java to translatedBasicBlockStatementPrinter,
        TranslatedDoWhileNode::class.java to translatedDoWhileStatementPrinter,
        TranslatedExpressionStatementNode::class.java to translatedExpressionStatementPrinter,
        TranslatedForNode::class.java to translatedForStatementPrinter,
        TranslatedIfNode::class.java to translatedIfStatementPrinter,
        TranslatedReturnNode::class.java to translatedReturnStatementPrinter,
        TranslatedWhileNode::class.java to translatedWhileStatementPrinter,
        VariableDeclarationListNode::class.java to variableDeclarationListStatementPrinter
    )

    private val stackGenerator = StackGenerator()

    private val statementPrinter = StatementPrinterOrchestrator(
        stackGenerator,
        printerMap,
    )
    private val functionDeclarationPrinter = FunctionDeclarationPrinter(statementPrinter)

    private val declarationStatementPrinter = DeclarationStatementPrinter(
        functionDeclarationPrinter,
        variableDeclarationListPrinter,
    )
    val printer: IPrinter = Printer(declarationStatementPrinter)
}