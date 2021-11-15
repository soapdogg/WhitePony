package compiler.translator.impl

import compiler.core.IParsedExpressionNode
import compiler.core.ITranslatedExpressionNode
import compiler.core.ParsedAssignNode
import compiler.translator.impl.internal.IExpressionTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class AssignTranslatorTest {

    private val expressionTranslator = Mockito.mock(IExpressionTranslator::class.java)
    private val assignTranslator = AssignTranslator(expressionTranslator)

    @Test
    fun translateTest() {
        val assignNode = Mockito.mock(ParsedAssignNode::class.java)

        val parsedExpressionNode = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(assignNode.expressionNode).thenReturn(parsedExpressionNode)

        val translatedExpressionNode = Mockito.mock(ITranslatedExpressionNode::class.java)
        Mockito.`when`(expressionTranslator.translate(parsedExpressionNode)).thenReturn(translatedExpressionNode)

        val actual = assignTranslator.translate(assignNode)

        Assertions.assertEquals(translatedExpressionNode, actual.expressionNode)
    }
}