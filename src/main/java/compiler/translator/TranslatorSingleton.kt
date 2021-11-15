package compiler.translator

import compiler.translator.impl.ArrayTranslator
import compiler.translator.impl.AssignTranslator
import compiler.translator.impl.DeclarationStatementTranslator
import compiler.translator.impl.Translator
import compiler.translator.impl.VariableDeclarationListTranslator
import compiler.translator.impl.VariableDeclarationTranslator
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IFunctionDeclarationTranslator
import org.mockito.Mockito

enum class TranslatorSingleton {
    INSTANCE;

    private val expressionTranslator = Mockito.mock(IExpressionTranslator::class.java)

    private val arrayTranslator = ArrayTranslator(expressionTranslator)
    private val assignTranslator = AssignTranslator(expressionTranslator)

    private val variableDeclarationTranslator = VariableDeclarationTranslator(
        arrayTranslator,
        assignTranslator
    )

    private val variableDeclarationListTranslator = VariableDeclarationListTranslator(variableDeclarationTranslator)

    private val functionDeclarationTranslator = Mockito.mock(IFunctionDeclarationTranslator::class.java)

    private val declarationStatementTranslator = DeclarationStatementTranslator(
        functionDeclarationTranslator,
        variableDeclarationListTranslator
    )

    val translator: ITranslator = Translator(declarationStatementTranslator)
}