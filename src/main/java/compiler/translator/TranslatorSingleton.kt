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
    private val typeDeterminer = TypeDeterminer()
    private val tempDeclarationCodeGenerator = TempDeclarationCodeGenerator()

    private val innerExpressionTranslator = InnerExpressionTranslator()
    private val variableExpressionTranslator = VariableExpressionTranslator(
        tempGenerator,
        tempDeclarationCodeGenerator
    )
    private val constantExpressionTranslator = ConstantExpressionTranslator()

    private val expressionTranslator = ExpressionTranslator(
        innerExpressionTranslator,
        variableExpressionTranslator,
        constantExpressionTranslator,
        tempGenerator,
        typeDeterminer,
        tempDeclarationCodeGenerator
    )

    private val booleanExpressionTranslator = BooleanExpressionTranslator(expressionTranslator)

    private val expressionStatementTranslator = ExpressionStatementTranslator(expressionTranslator)

    private val returnStatementTranslator = ReturnStatementTranslator(expressionStatementTranslator)

    private val statementTranslator = StatementTranslator(
        variableTypeRecorder,
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