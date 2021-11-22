package compiler.translator

import compiler.translator.impl.*
import compiler.translator.impl.ArrayTranslator
import compiler.translator.impl.AssignTranslator
import compiler.translator.impl.DeclarationStatementTranslator
import compiler.translator.impl.ExpressionStatementTranslator
import compiler.translator.impl.FunctionDeclarationTranslator
import compiler.translator.impl.ReturnStatementTranslator
import compiler.translator.impl.Translator
import compiler.translator.impl.VariableDeclarationListTranslator
import compiler.translator.impl.VariableDeclarationTranslator

enum class TranslatorSingleton {
    INSTANCE;

    private val expressionTranslator = FakeExpressionTranslator()

    private val arrayTranslator = ArrayTranslator(expressionTranslator)
    private val assignTranslator = AssignTranslator(expressionTranslator)

    private val variableDeclarationTranslator = VariableDeclarationTranslator(
        arrayTranslator,
        assignTranslator
    )

    private val variableDeclarationListTranslator = VariableDeclarationListTranslator(variableDeclarationTranslator)

    private val expressionStatementTranslator = ExpressionStatementTranslator(expressionTranslator)

    private val returnStatementTranslator = ReturnStatementTranslator(expressionStatementTranslator)

    private val statementTranslator = StatementTranslator(
        expressionTranslator,
        variableDeclarationListTranslator,
        returnStatementTranslator,
        expressionStatementTranslator
    )

    private val functionDeclarationTranslator = FunctionDeclarationTranslator(
        statementTranslator
    )

    private val declarationStatementTranslator = DeclarationStatementTranslator(
        functionDeclarationTranslator,
        variableDeclarationListTranslator
    )

    val translator: ITranslator = Translator(declarationStatementTranslator)
}