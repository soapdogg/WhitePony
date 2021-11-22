package compiler.translator.impl

import compiler.core.*
import compiler.translator.impl.internal.IArrayTranslator
import compiler.translator.impl.internal.IAssignTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class VariableDeclarationTranslatorTest {
    private val arrayTranslator = Mockito.mock(IArrayTranslator::class.java)
    private val assignTranslator = Mockito.mock(IAssignTranslator::class.java)

    private val variableDeclarationTranslator = VariableDeclarationTranslator(
        arrayTranslator,
        assignTranslator
    )

    @Test
    fun bothPresentTest() {
        val variableDeclarationNode = Mockito.mock(ParsedVariableDeclarationNode::class.java)
        val labelCounter = 0
        val tempCounter = 0

        val parsedArrayNode = Mockito.mock(ParsedArrayNode::class.java)
        Mockito.`when`(variableDeclarationNode.arrayNode).thenReturn(parsedArrayNode)

        val translatedArrayNode = Mockito.mock(TranslatedArrayNode::class.java)
        Mockito.`when`(arrayTranslator.translate(parsedArrayNode, labelCounter, tempCounter)).thenReturn(translatedArrayNode)

        val parsedAssignNode = Mockito.mock(ParsedAssignNode::class.java)
        Mockito.`when`(variableDeclarationNode.assignNode).thenReturn(parsedAssignNode)

        val translatedAssignNode = Mockito.mock(TranslatedAssignNode::class.java)
        Mockito.`when`(assignTranslator.translate(parsedAssignNode, labelCounter, tempCounter)).thenReturn(translatedAssignNode)

        val id = "id"
        Mockito.`when`(variableDeclarationNode.id).thenReturn(id)

        val actual = variableDeclarationTranslator.translate(variableDeclarationNode, labelCounter, tempCounter)

        Assertions.assertEquals(id, actual.id)
        Assertions.assertEquals(translatedArrayNode, actual.arrayNode)
        Assertions.assertEquals(translatedAssignNode, actual.assignNode)
    }

    @Test
    fun neitherPresentTest() {
        val variableDeclarationNode = Mockito.mock(ParsedVariableDeclarationNode::class.java)
        val labelCounter = 0
        val tempCounter = 0

        val id = "id"
        Mockito.`when`(variableDeclarationNode.id).thenReturn(id)

        val actual = variableDeclarationTranslator.translate(variableDeclarationNode, labelCounter, tempCounter)

        Assertions.assertEquals(id, actual.id)
        Assertions.assertNull(actual.arrayNode)
        Assertions.assertNull(actual.assignNode)
    }
}