package compiler.translator

import compiler.translator.impl.DeclarationStatementTranslator
import compiler.translator.impl.Translator
import compiler.translator.impl.VariableDeclarationListTranslator
import compiler.translator.impl.VariableDeclarationTranslator
import compiler.translator.impl.internal.IArrayTranslator
import compiler.translator.impl.internal.IAssignTranslator
import compiler.translator.impl.internal.IFunctionDeclarationTranslator
import org.mockito.Mockito

enum class TranslatorSingleton {
    INSTANCE;

    private val arrayTranslator = Mockito.mock(IArrayTranslator::class.java)
    private val assignTranslator = Mockito.mock(IAssignTranslator::class.java)

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