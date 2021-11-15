package compiler.translator.impl

import compiler.core.ParsedFunctionDeclarationNode
import compiler.core.ParsedVariableDeclarationListNode
import compiler.core.TranslatedFunctionDeclarationNode
import compiler.core.TranslatedVariableDeclarationListNode
import compiler.translator.impl.internal.IFunctionDeclarationTranslator
import compiler.translator.impl.internal.IVariableDeclarationListTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class DeclarationStatementTranslatorTest {
    private val functionDeclarationTranslator = Mockito.mock(IFunctionDeclarationTranslator::class.java)
    private val variableDeclarationListTranslator = Mockito.mock(IVariableDeclarationListTranslator::class.java)

    private val declarationStatementTranslator = DeclarationStatementTranslator(
        functionDeclarationTranslator,
        variableDeclarationListTranslator
    )

    @Test
    fun translateVariableListTest() {
        val parsedDeclarationStatementNode = Mockito.mock(ParsedVariableDeclarationListNode::class.java)

        val translatedDeclarationStatementNode = Mockito.mock(TranslatedVariableDeclarationListNode::class.java)
        Mockito.`when`(variableDeclarationListTranslator.translate(parsedDeclarationStatementNode)).thenReturn(translatedDeclarationStatementNode)

        val actual = declarationStatementTranslator.translate(parsedDeclarationStatementNode)

        Assertions.assertEquals(translatedDeclarationStatementNode, actual)
    }

    @Test
    fun translateFunctionTest() {
        val parsedDeclarationStatementNode = Mockito.mock(ParsedFunctionDeclarationNode::class.java)

        val translatedDeclarationStatementNode = Mockito.mock(TranslatedFunctionDeclarationNode::class.java)
        Mockito.`when`(functionDeclarationTranslator.translate(parsedDeclarationStatementNode)).thenReturn(translatedDeclarationStatementNode)

        val actual = declarationStatementTranslator.translate(parsedDeclarationStatementNode)

        Assertions.assertEquals(translatedDeclarationStatementNode, actual)
    }
}