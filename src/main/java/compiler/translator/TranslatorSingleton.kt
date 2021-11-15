package compiler.translator

import compiler.translator.impl.Translator
import compiler.translator.impl.internal.IDeclarationStatementTranslator
import org.mockito.Mock
import org.mockito.Mockito

enum class TranslatorSingleton {
    INSTANCE;

    private val declarationStatementTranslator = Mockito.mock(IDeclarationStatementTranslator::class.java)

    val translator: ITranslator = Translator(declarationStatementTranslator)
}