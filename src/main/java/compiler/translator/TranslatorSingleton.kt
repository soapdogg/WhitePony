package compiler.translator

import compiler.translator.impl.DeclarationStatementTranslator
import compiler.translator.impl.Translator
import compiler.translator.impl.internal.IFunctionDeclarationTranslator
import compiler.translator.impl.internal.IVariableDeclarationListTranslator
import org.mockito.Mockito

enum class TranslatorSingleton {
    INSTANCE;

    private val functionDeclarationTranslator = Mockito.mock(IFunctionDeclarationTranslator::class.java)
    private val variableDeclarationListTranslator = Mockito.mock(IVariableDeclarationListTranslator::class.java)

    private val declarationStatementTranslator = DeclarationStatementTranslator(
        functionDeclarationTranslator,
        variableDeclarationListTranslator
    )

    val translator: ITranslator = Translator(declarationStatementTranslator)
}