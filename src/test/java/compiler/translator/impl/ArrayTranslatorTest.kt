package compiler.translator.impl

import compiler.core.IParsedExpressionNode
import compiler.core.ITranslatedExpressionNode
import compiler.core.ParsedArrayNode
import compiler.translator.impl.internal.IExpressionTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ArrayTranslatorTest {
    private val expressionTranslator = Mockito.mock(IExpressionTranslator::class.java)

    private val arrayTranslator = ArrayTranslator(expressionTranslator)

    @Test
    fun indexExpressionPresentTest() {
        val arrayNode = Mockito.mock(ParsedArrayNode::class.java)
        val labelCounter = 0
        val tempCounter = 0

        val parsedExpressionNode = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(arrayNode.indexExpressionNode).thenReturn(parsedExpressionNode)

        val translatedExpressionNode = Mockito.mock(ITranslatedExpressionNode::class.java)
        Mockito.`when`(expressionTranslator.translate(parsedExpressionNode, labelCounter, tempCounter)).thenReturn(translatedExpressionNode)

        val actual = arrayTranslator.translate(arrayNode, labelCounter, tempCounter)
        Assertions.assertEquals(translatedExpressionNode, actual.indexExpressionNode)
    }

    @Test
    fun indexExpressionNotPresentTest() {
        val arrayNode = Mockito.mock(ParsedArrayNode::class.java)
        val labelCounter = 0
        val tempCounter = 0

        val actual = arrayTranslator.translate(arrayNode, labelCounter, tempCounter)
        Assertions.assertNull(actual.indexExpressionNode)
    }
}