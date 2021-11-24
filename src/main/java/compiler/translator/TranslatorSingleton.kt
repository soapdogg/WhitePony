package compiler.translator

import compiler.translator.impl.*
import compiler.translator.impl.DeclarationStatementTranslator
import compiler.translator.impl.ExpressionStatementTranslator
import compiler.translator.impl.FunctionDeclarationTranslator
import compiler.translator.impl.ReturnStatementTranslator
import compiler.translator.impl.Translator

enum class TranslatorSingleton {
    INSTANCE;

    private val variableTypeRecorder = VariableTypeRecorder()

    private val tempGenerator = TempGenerator()

    private val expressionTranslator = ExpressionTranslator(tempGenerator)

    private val booleanExpressionTranslator = BooleanExpressionTranslator()

    private val expressionStatementTranslator = ExpressionStatementTranslator(expressionTranslator)

    private val returnStatementTranslator = ReturnStatementTranslator(expressionStatementTranslator)

    private val statementTranslator = StatementTranslator(
        expressionTranslator,
        booleanExpressionTranslator,
        returnStatementTranslator,
        expressionStatementTranslator
    )

    private val functionDeclarationTranslator = FunctionDeclarationTranslator(
        statementTranslator
    )

    private val declarationStatementTranslator = DeclarationStatementTranslator(
        functionDeclarationTranslator,
        variableTypeRecorder
    )

    val translator: ITranslator = Translator(declarationStatementTranslator)
}