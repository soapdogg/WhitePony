package compiler.translator.impl

import compiler.core.ParsedFunctionDeclarationNode
import compiler.core.TranslatedFunctionDeclarationNode
import compiler.core.VariableDeclarationListNode
import compiler.translator.impl.internal.IFunctionDeclarationTranslator
import compiler.translator.impl.internal.IVariableTypeRecorder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class DeclarationStatementTranslatorTest {
    private val functionDeclarationTranslator = Mockito.mock(IFunctionDeclarationTranslator::class.java)
    private val variableTypeRecorder = Mockito.mock(IVariableTypeRecorder::class.java)

    private val declarationStatementTranslator = DeclarationStatementTranslator(
        functionDeclarationTranslator,
        variableTypeRecorder
    )

    @Test
    fun translateVariableListTest() {
        val parsedDeclarationStatementNode = Mockito.mock(VariableDeclarationListNode::class.java)
        val variableToTypeMap = mutableMapOf<String, String>()

        val actual = declarationStatementTranslator.translate(parsedDeclarationStatementNode, variableToTypeMap)
        Assertions.assertEquals(parsedDeclarationStatementNode, actual)
        Mockito.verify(variableTypeRecorder).recordVariableTypes(parsedDeclarationStatementNode, variableToTypeMap)
    }

    @Test
    fun translateFunctionTest() {
        val parsedDeclarationStatementNode = Mockito.mock(ParsedFunctionDeclarationNode::class.java)
        val variableToTypeMap = mutableMapOf<String, String>()

        val translatedDeclarationStatementNode = Mockito.mock(TranslatedFunctionDeclarationNode::class.java)
        Mockito.`when`(functionDeclarationTranslator.translate(parsedDeclarationStatementNode, variableToTypeMap)).thenReturn(translatedDeclarationStatementNode)

        val actual = declarationStatementTranslator.translate(parsedDeclarationStatementNode, variableToTypeMap)

        Assertions.assertEquals(translatedDeclarationStatementNode, actual)
    }
}