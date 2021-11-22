package compiler.translator.impl

import compiler.core.ParsedVariableDeclarationListNode
import compiler.core.ParsedVariableDeclarationNode
import compiler.core.TranslatedVariableDeclarationNode
import compiler.translator.impl.internal.IVariableDeclarationTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito

class VariableDeclarationListTranslatorTest {

    private val variableDeclarationTranslator = Mockito.mock(IVariableDeclarationTranslator::class.java)

    private val variableDeclarationListTranslator = VariableDeclarationListTranslator(variableDeclarationTranslator)

    @Test
    fun translateTest() {
        val variableDeclarationListNode = Mockito.mock(ParsedVariableDeclarationListNode::class.java)
        val labelCounter = 0
        val tempCounter = 0

        val parsedVariableDeclarationNode = Mockito.mock(ParsedVariableDeclarationNode::class.java)
        Mockito.`when`(variableDeclarationListNode.variableDeclarations).thenReturn(listOf(parsedVariableDeclarationNode))

        val translatedVariableDeclarationNode = Mockito.mock(TranslatedVariableDeclarationNode::class.java)
        Mockito.`when`(variableDeclarationTranslator.translate(parsedVariableDeclarationNode, labelCounter, tempCounter)).thenReturn(translatedVariableDeclarationNode)

        val type = "type"
        Mockito.`when`(variableDeclarationListNode.type).thenReturn(type)

        val actual = variableDeclarationListTranslator.translate(variableDeclarationListNode, labelCounter, tempCounter)

        Assertions.assertEquals(type, actual.type)
        Assertions.assertEquals(translatedVariableDeclarationNode, actual.variableDeclarations[0])
    }
}