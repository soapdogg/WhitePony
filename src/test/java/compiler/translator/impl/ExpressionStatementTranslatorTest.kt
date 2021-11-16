package compiler.translator.impl

import compiler.core.IParsedExpressionNode
import compiler.core.ITranslatedExpressionNode
import compiler.core.ParsedExpressionStatementNode
import compiler.translator.impl.internal.IExpressionTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ExpressionStatementTranslatorTest {
    private val expressionTranslator = Mockito.mock(IExpressionTranslator::class.java)

    private val expressionStatementTranslator = ExpressionStatementTranslator(expressionTranslator)

    @Test
    fun translateTest() {
        val expressionStatement = Mockito.mock(ParsedExpressionStatementNode::class.java)

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(expressionStatement.expressionNode).thenReturn(expression)

        val translatedExpression = Mockito.mock(ITranslatedExpressionNode::class.java)
        Mockito.`when`(expressionTranslator.translate(expression)).thenReturn(translatedExpression)

        val actual = expressionStatementTranslator.translate(expressionStatement)
        Assertions.assertEquals(translatedExpression, actual.expression)
    }
}