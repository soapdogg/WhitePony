package compiler.translator.impl

import compiler.core.ParsedFunctionDeclarationNode
import compiler.core.TranslatedFunctionDeclarationNode
import compiler.core.VariableDeclarationListNode
import compiler.translator.impl.internal.IFunctionDeclarationTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class DeclarationStatementTranslatorTest {
    private val functionDeclarationTranslator = Mockito.mock(IFunctionDeclarationTranslator::class.java)

    private val declarationStatementTranslator = DeclarationStatementTranslator(
        functionDeclarationTranslator
    )

    @Test
    fun translateVariableListTest() {
        val parsedDeclarationStatementNode = Mockito.mock(VariableDeclarationListNode::class.java)

        val actual = declarationStatementTranslator.translate(parsedDeclarationStatementNode)
        Assertions.assertEquals(parsedDeclarationStatementNode, actual)
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